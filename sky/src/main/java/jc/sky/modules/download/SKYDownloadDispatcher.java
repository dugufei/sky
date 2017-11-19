package jc.sky.modules.download;

import android.os.Process;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.concurrent.BlockingQueue;

import jc.sky.core.SKYHelper;
import jc.sky.common.utils.SKYGsonUtils;
import jc.sky.modules.log.L;
import jc.sky.common.utils.SKYAppUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_MOVED_PERM;
import static java.net.HttpURLConnection.HTTP_MOVED_TEMP;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_SEE_OTHER;
import static java.net.HttpURLConnection.HTTP_UNAVAILABLE;

/**
 * @author sky
 * @version 版本
 */
public class SKYDownloadDispatcher extends Thread {

	/** 下载队列请求服务. */
	private final BlockingQueue<SKYBaseRequest>	mQueue;

	/** 通知调度死亡 */
	private volatile boolean					mQuit									= false;

	/** 请求 */
	private SKYBaseRequest						mRequest;

	/** 数据流缓冲大小 */
	public final int							BUFFER_SIZE								= 4096;

	/** 记录重定向多少次. */
	private int									mRedirectionCount						= 0;

	/** 最大重定向数量 */
	public final int							MAX_REDIRECTS							= 5;

	/** 网络底层协议 **/
	OkHttpClient								okHttpClient;

	/**
	 * 网络响应
	 */
	private final int							HTTP_REQUESTED_RANGE_NOT_SATISFIABLE	= 416;	// 资源范围

	private final int							HTTP_TEMP_REDIRECT						= 307;	// 重定向

	private long								mContentLength;									// 内容长度

	private long								mCurrentBytes;									// 下载字节

	boolean										shouldAllowRedirects					= true;	// 是否开启重定向

	Gson										gson;

	/**
	 * 初始化调度器
	 *
	 * @param queue
	 *            队列
	 * @param okHttpClient
	 *            网路底层协议
	 */
	public SKYDownloadDispatcher(BlockingQueue<SKYBaseRequest> queue, OkHttpClient okHttpClient) {
		mQueue = queue;
		this.okHttpClient = okHttpClient;
		gson = new GsonBuilder().create();
	}

	/**
	 * 执行
	 */
	@Override public void run() {
		Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

		while (true) {
			try {
				mRequest = mQueue.take(); // 从队列中取出指令

				if (mRequest instanceof SKYDownloadRequest) { // 下载请求
					SKYDownloadRequest SKYDownloadRequest = (SKYDownloadRequest) mRequest;
					mRedirectionCount = 0; // 重定向清零
					L.i("请求ID = " + SKYDownloadRequest.getRequestId());
					SKYDownloadRequest.setDownloadState(SKYDownloadManager.STATUS_STARTED);// 设置状态
					executeDownload(SKYDownloadRequest, SKYDownloadRequest.getDownloadUrl().toString());
				} else if (mRequest instanceof SKYUploadRequest) { // 上传请求
					SKYUploadRequest SKYUploadRequest = (SKYUploadRequest) mRequest;
					mRedirectionCount = 0; // 重定向清零
					L.i("请求ID = " + SKYUploadRequest.getRequestId());
					SKYUploadRequest.setDownloadState(SKYDownloadManager.STATUS_STARTED);// 设置状态
					executeUpload(SKYUploadRequest, SKYUploadRequest.getUploadUrl().toString());
				} else {
					L.i("未知指令");
				}

			} catch (InterruptedException e) {
				if (mQuit) {
					if (mRequest instanceof SKYDownloadRequest) { // 下载请求
						SKYDownloadRequest SKYDownloadRequest = (SKYDownloadRequest) mRequest;
						SKYDownloadRequest.finish();
						updateDownloadFailed(SKYDownloadRequest, SKYDownloadManager.ERROR_DOWNLOAD_CANCELLED, "取消下载");
					} else if (mRequest instanceof SKYUploadRequest) { // 上传请求
						SKYUploadRequest SKYUploadRequest = (SKYUploadRequest) mRequest;
						SKYUploadRequest.finish();
						updateUploadFailed(SKYUploadRequest, SKYDownloadManager.ERROR_DOWNLOAD_CANCELLED, "取消上传");

					} else {
						L.i("未知指令");
					}
					return;
				}
				continue;
			}
		}
	}

	/**
	 * *******************************上传****************************************
	 */

	/**
	 * 执行上传
	 *
	 * @param SKYUploadRequest
	 *            下载请求
	 * @param uploadUrl
	 *            请求url
	 */
	private void executeUpload(SKYUploadRequest SKYUploadRequest, String uploadUrl) {
		URL url = null;
		try {
			url = new URL(uploadUrl);
		} catch (MalformedURLException e) {
			updateUploadFailed(SKYUploadRequest, SKYDownloadManager.ERROR_MALFORMED_URI, "异常 : 不正确的地址");
			return;
		}
		updateUploadState(SKYUploadRequest, SKYDownloadManager.STATUS_CONNECTING);
		// 创建文件体
		SKYOkUploadBody SKYOkUploadBody = new SKYOkUploadBody(SKYUploadRequest, SKYUploadRequest.getSKYUploadListener());
		// 请求
		Request request = new Request.Builder().tag(SKYUploadRequest.getRequestTag()).url(url).headers(SKYUploadRequest.getHeaders()).post(SKYOkUploadBody.build()).build();// 创建请求

		try {
			updateUploadState(SKYUploadRequest, SKYDownloadManager.STATUS_RUNNING);
			Response response = okHttpClient.newCall(request).execute();
			final int responseCode = response.code();
			L.i("请求头信息:" + request.headers().toString());
			L.i("请求编号:" + SKYUploadRequest.getRequestId() + ", 响应编号 : " + responseCode);

			switch (responseCode) {
				case HTTP_OK:
					if (SKYUploadRequest.isCanceled()) {
						SKYUploadRequest.finish();
						updateUploadFailed(SKYUploadRequest, SKYDownloadManager.ERROR_DOWNLOAD_CANCELLED, "取消下载");
						return;
					}
					if (!response.isSuccessful()) {
						updateUploadFailed(SKYUploadRequest, SKYDownloadManager.ERROR_HTTP_DATA_ERROR, "成功响应,但上传失败！");
						return;
					}
					shouldAllowRedirects = false;
					postUploadComplete(SKYUploadRequest, response);// 上传成功
					return;
				case HTTP_MOVED_PERM:
				case HTTP_MOVED_TEMP:
				case HTTP_SEE_OTHER:
				case HTTP_TEMP_REDIRECT:
					while (mRedirectionCount++ < MAX_REDIRECTS && shouldAllowRedirects) {
						L.i("重定向 Id " + SKYUploadRequest.getRequestId());
						final String location = response.header("Location");
						executeUpload(SKYUploadRequest, location); // 执行下载
						continue;
					}

					if (mRedirectionCount > MAX_REDIRECTS) {
						updateUploadFailed(SKYUploadRequest, SKYDownloadManager.ERROR_TOO_MANY_REDIRECTS, "重定向太多，导致下载失败,默认最多 5次重定向！");
						return;
					}
					break;
				case HTTP_REQUESTED_RANGE_NOT_SATISFIABLE:
					updateUploadFailed(SKYUploadRequest, HTTP_REQUESTED_RANGE_NOT_SATISFIABLE, response.message());
					break;
				case HTTP_UNAVAILABLE:
					updateUploadFailed(SKYUploadRequest, HTTP_UNAVAILABLE, response.message());
					break;
				case HTTP_INTERNAL_ERROR:
					updateUploadFailed(SKYUploadRequest, HTTP_INTERNAL_ERROR, response.message());
					break;
				default:
					updateUploadFailed(SKYUploadRequest, SKYDownloadManager.ERROR_UNHANDLED_HTTP_CODE, "未处理的响应:" + responseCode + " 信息:" + response.message());
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
			updateUploadFailed(SKYUploadRequest, SKYDownloadManager.ERROR_HTTP_DATA_ERROR, "故障");
		}
	}

	/**
	 * 更新状态 - 上传失败
	 *
	 * @param SKYUploadRequest
	 *            参数
	 * @param errorCode
	 *            参数
	 * @param errorMsg
	 *            参数
	 */
	public void updateUploadFailed(SKYUploadRequest SKYUploadRequest, int errorCode, String errorMsg) {
		shouldAllowRedirects = false;
		SKYUploadRequest.setDownloadState(SKYDownloadManager.STATUS_FAILED);
		if (SKYUploadRequest.getSKYUploadListener() != null) {
			postUploadFailed(SKYUploadRequest, errorCode, errorMsg);
			SKYUploadRequest.finish();
		}
	}

	/**
	 * 更新状态 - 链接
	 *
	 * @param SKYUploadRequest
	 *            参数
	 * @param state
	 *            参数
	 */
	public void updateUploadState(SKYUploadRequest SKYUploadRequest, int state) {
		SKYUploadRequest.setDownloadState(state);
	}

	/**
	 * 上传失败
	 *
	 * @param request
	 *            请求
	 * @param errorCode
	 *            错误编号
	 * @param errorMsg
	 *            错误信息
	 */
	public void postUploadFailed(final SKYUploadRequest request, final int errorCode, final String errorMsg) {
		SKYHelper.mainLooper().execute(new Runnable() {

			@Override public void run() {
				request.getSKYUploadListener().onUploadFailed(request.getRequestId(), errorCode, errorMsg);
			}
		});
	}

	/**
	 * 上传成功
	 *
	 * @param request
	 *            请求
	 * @param response
	 *            参数
	 */
	public void postUploadComplete(final SKYUploadRequest request, final Response response) {
		try {

			final Class clazz = SKYAppUtil.getSuperClassGenricType(request.getSKYUploadListener().getClass(), 0);

			final Object value = SKYGsonUtils.readBody(gson, Charset.forName("UTF-8"), response.body(), clazz);
			SKYHelper.mainLooper().execute(new Runnable() {

				@Override public void run() {
					if (clazz == null) {
						request.getSKYUploadListener().onUploadComplete(request.getRequestId(), response);
					} else {
						request.getSKYUploadListener().onUploadComplete(request.getRequestId(), value);
					}

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			SKYHelper.mainLooper().execute(new Runnable() {

				public void run() {
					request.getSKYUploadListener().onUploadFailed(request.getRequestId(), 0, "上传成功-数据转换失败");
				}
			});
		}
	}

	/**
	 * *******************************下载****************************************
	 */
	/**
	 * 执行下载
	 * 
	 * @param SKYDownloadRequest
	 *            下载请求
	 * @param downloadUrl
	 *            请求url
	 */
	private void executeDownload(SKYDownloadRequest SKYDownloadRequest, String downloadUrl) {

		URL url = null;
		try {
			url = new URL(downloadUrl);
		} catch (MalformedURLException e) {
			updateDownloadFailed(SKYDownloadRequest, SKYDownloadManager.ERROR_MALFORMED_URI, "异常 : 不正确的地址");
			return;
		}

		Request request = new Request.Builder().tag(SKYDownloadRequest.getRequestTag()).url(url).build();// 创建请求
		try {
			Response response = okHttpClient.newCall(request).execute();

			updateDownloadState(SKYDownloadRequest, SKYDownloadManager.STATUS_CONNECTING);

			final int responseCode = response.code();

			L.i("请求编号:" + SKYDownloadRequest.getRequestId() + ", 响应编号 : " + responseCode);

			switch (responseCode) {
				case HTTP_OK:
					shouldAllowRedirects = false;
					if (readResponseHeaders(SKYDownloadRequest, response) == 1) {
						transferData(SKYDownloadRequest, response);
					} else {
						updateDownloadFailed(SKYDownloadRequest, SKYDownloadManager.ERROR_DOWNLOAD_SIZE_UNKNOWN, "服务端没有返回文件长度，长度不确定导致失败！");
					}
					return;
				case HTTP_MOVED_PERM:
				case HTTP_MOVED_TEMP:
				case HTTP_SEE_OTHER:
				case HTTP_TEMP_REDIRECT:
					while (mRedirectionCount++ < MAX_REDIRECTS && shouldAllowRedirects) {
						L.i("重定向 Id " + SKYDownloadRequest.getRequestId());
						final String location = response.header("Location");
						executeDownload(SKYDownloadRequest, location); // 执行下载
						continue;
					}

					if (mRedirectionCount > MAX_REDIRECTS) {
						updateDownloadFailed(SKYDownloadRequest, SKYDownloadManager.ERROR_TOO_MANY_REDIRECTS, "重定向太多，导致下载失败,默认最多 5次重定向！");
						return;
					}
					break;
				case HTTP_REQUESTED_RANGE_NOT_SATISFIABLE:
					updateDownloadFailed(SKYDownloadRequest, HTTP_REQUESTED_RANGE_NOT_SATISFIABLE, response.message());
					break;
				case HTTP_UNAVAILABLE:
					updateDownloadFailed(SKYDownloadRequest, HTTP_UNAVAILABLE, response.message());
					break;
				case HTTP_INTERNAL_ERROR:
					updateDownloadFailed(SKYDownloadRequest, HTTP_INTERNAL_ERROR, response.message());
					break;
				default:
					updateDownloadFailed(SKYDownloadRequest, SKYDownloadManager.ERROR_UNHANDLED_HTTP_CODE, "未处理的响应:" + responseCode + " 信息:" + response.message());
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
			updateDownloadFailed(SKYDownloadRequest, SKYDownloadManager.ERROR_HTTP_DATA_ERROR, "故障");
		}
	}

	/**
	 * 流的读取
	 * 
	 * @param SKYDownloadRequest
	 *            参数
	 * @param conn
	 *            参数
	 */
	private void transferData(SKYDownloadRequest SKYDownloadRequest, Response conn) {
		InputStream in = null;
		OutputStream out = null;
		cleanupDestination(SKYDownloadRequest);
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			in = conn.body().byteStream();

			File destinationFile = new File(SKYDownloadRequest.getDestinationUrl().getPath().toString());

			try {
				out = new FileOutputStream(destinationFile, true);
			} catch (IOException e) {
				e.printStackTrace();
				updateDownloadFailed(SKYDownloadRequest, SKYDownloadManager.ERROR_FILE_ERROR, "路径转换文件时错误");
			}

			bis = new BufferedInputStream(in);
			bos = new BufferedOutputStream(out);

			transferData(SKYDownloadRequest, bis, bos);

		} finally {

			try {
				bis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				if (bos != null) bos.flush();
			} catch (IOException e) {
			} finally {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 流的读取
	 * 
	 * @param SKYDownloadRequest
	 *            参数
	 * @param in
	 *            参数
	 * @param out
	 *            参数
	 */
	private void transferData(SKYDownloadRequest SKYDownloadRequest, BufferedInputStream in, BufferedOutputStream out) {
		final byte data[] = new byte[BUFFER_SIZE];
		mCurrentBytes = 0;
		SKYDownloadRequest.setDownloadState(SKYDownloadManager.STATUS_RUNNING);
		L.i("内容长度: " + mContentLength + " 下载ID : " + SKYDownloadRequest.getRequestId());
		for (;;) {
			if (SKYDownloadRequest.isCanceled()) {
				L.i("取消的请求Id " + SKYDownloadRequest.getRequestId());
				mRequest.finish();
				updateDownloadFailed(SKYDownloadRequest, SKYDownloadManager.ERROR_DOWNLOAD_CANCELLED, "取消下载");
				return;
			}
			int bytesRead = readFromResponse(SKYDownloadRequest, data, in);

			if (mContentLength != -1) {
				int progress = (int) ((mCurrentBytes * 100) / mContentLength);
				updateDownloadProgress(SKYDownloadRequest, progress, mCurrentBytes);
			}

			if (bytesRead == -1) {
				updateDownloadComplete(SKYDownloadRequest);
				return;
			} else if (bytesRead == Integer.MIN_VALUE) {
				return;
			}

			writeDataToDestination(SKYDownloadRequest, data, bytesRead, out);
			mCurrentBytes += bytesRead;
		}
	}

	/**
	 * 从响应体里读取
	 * 
	 * @param SKYDownloadRequest
	 *            参数
	 * @param data
	 *            参数
	 * @param entityStream
	 *            参数
	 * @return 返回值
	 */
	private int readFromResponse(SKYDownloadRequest SKYDownloadRequest, byte[] data, BufferedInputStream entityStream) {
		try {
			return entityStream.read(data);
		} catch (IOException ex) {
			updateDownloadFailed(SKYDownloadRequest, SKYDownloadManager.ERROR_HTTP_DATA_ERROR, "无法读取响应");
			return Integer.MIN_VALUE;
		}
	}

	/**
	 * 下载数据到目标文件
	 * 
	 * @param SKYDownloadRequest
	 *            参数
	 * @param data
	 *            参数
	 * @param bytesRead
	 *            参数
	 * @param out
	 *            参数
	 */
	private void writeDataToDestination(SKYDownloadRequest SKYDownloadRequest, byte[] data, int bytesRead, BufferedOutputStream out) {
		while (true) {
			try {
				out.write(data, 0, bytesRead);
				return;
			} catch (IOException ex) {
				updateDownloadFailed(SKYDownloadRequest, SKYDownloadManager.ERROR_FILE_ERROR, "写入目标文件时，IO异常错误");
			}
		}
	}

	/**
	 * 读取响应头信息
	 * 
	 * @param SKYDownloadRequest
	 *            参数
	 * @param response
	 *            参数
	 * @return 返回值
	 */
	private int readResponseHeaders(SKYDownloadRequest SKYDownloadRequest, Response response) {
		final String transferEncoding = response.header("Transfer-Encoding");

		if (transferEncoding == null) {
			mContentLength = getHeaderFieldLong(response, "Content-Length", -1);
		} else {
			L.i("长度无法确定的请求Id " + SKYDownloadRequest.getRequestId());
			mContentLength = -1;
		}

		if (mContentLength == -1 && (transferEncoding == null || !transferEncoding.equalsIgnoreCase("chunked"))) {
			return -1;
		} else {
			return 1;
		}
	}

	/**
	 * 读取头信息
	 * 
	 * @param conn
	 *            参数
	 * @param field
	 *            参数 参数
	 * @param defaultValue
	 *            参数
	 * @return 返回值
	 */
	public long getHeaderFieldLong(Response conn, String field, long defaultValue) {
		try {
			return Long.parseLong(conn.header(field));
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	/**
	 * 清理目标文件
	 * 
	 * @param SKYDownloadRequest
	 *            参数
	 */
	private void cleanupDestination(SKYDownloadRequest SKYDownloadRequest) {
		L.i("目标文件路径 : " + SKYDownloadRequest.getDestinationUrl());
		File destinationFile = new File(SKYDownloadRequest.getDestinationUrl().getPath().toString());
		if (destinationFile.exists()) {
			destinationFile.delete();
		}
	}

	/**
	 * 更新状态 - 链接
	 * 
	 * @param SKYDownloadRequest
	 *            参数
	 * @param state
	 *            参数
	 */
	public void updateDownloadState(SKYDownloadRequest SKYDownloadRequest, int state) {
		SKYDownloadRequest.setDownloadState(state);
	}

	/**
	 * 更新状态 - 下载成功
	 * 
	 * @param SKYDownloadRequest
	 *            参数
	 */
	public void updateDownloadComplete(SKYDownloadRequest SKYDownloadRequest) {
		SKYDownloadRequest.setDownloadState(SKYDownloadManager.STATUS_SUCCESSFUL);
		if (SKYDownloadRequest.getSKYDownloadListener() != null) {
			postDownloadComplete(SKYDownloadRequest);
			SKYDownloadRequest.finish();
		}
	}

	/**
	 * 更新状态 - 下载失败
	 * 
	 * @param SKYDownloadRequest
	 *            参数
	 * @param errorCode
	 *            参数
	 * @param errorMsg
	 *            参数
	 */
	public void updateDownloadFailed(SKYDownloadRequest SKYDownloadRequest, int errorCode, String errorMsg) {
		shouldAllowRedirects = false;
		SKYDownloadRequest.setDownloadState(SKYDownloadManager.STATUS_FAILED);
		cleanupDestination(SKYDownloadRequest);
		if (SKYDownloadRequest.getSKYDownloadListener() != null) {
			postDownloadFailed(SKYDownloadRequest, errorCode, errorMsg);
			SKYDownloadRequest.finish();
		}
	}

	/**
	 * 更新状态 - 加载进度
	 * 
	 * @param SKYDownloadRequest
	 *            参数
	 * @param progress
	 *            参数
	 * @param downloadedBytes
	 *            参数
	 */
	public void updateDownloadProgress(SKYDownloadRequest SKYDownloadRequest, int progress, long downloadedBytes) {
		if (SKYDownloadRequest.getSKYDownloadListener() != null) {
			postProgressUpdate(SKYDownloadRequest, mContentLength, downloadedBytes, progress);
		}
	}

	/**
	 * 下载成功
	 *
	 * @param request
	 *            请求
	 */
	public void postDownloadComplete(final SKYDownloadRequest request) {
		SKYHelper.mainLooper().execute(new Runnable() {

			@Override public void run() {
				request.getSKYDownloadListener().onDownloadComplete(request.getRequestId());
			}
		});
	}

	/**
	 * 下载失败
	 *
	 * @param request
	 *            请求
	 * @param errorCode
	 *            错误编号
	 * @param errorMsg
	 *            错误信息
	 */
	public void postDownloadFailed(final SKYDownloadRequest request, final int errorCode, final String errorMsg) {
		SKYHelper.mainLooper().execute(new Runnable() {

			@Override public void run() {
				request.getSKYDownloadListener().onDownloadFailed(request.getRequestId(), errorCode, errorMsg);
			}
		});
	}

	/**
	 * 下载进度
	 *
	 * @param request
	 *            请求
	 * @param totalBytes
	 *            总字节数
	 * @param downloadedBytes
	 *            参数
	 * @param progress
	 *            参数
	 */
	public void postProgressUpdate(final SKYDownloadRequest request, final long totalBytes, final long downloadedBytes, final int progress) {
		SKYHelper.mainLooper().execute(new Runnable() {

			@Override public void run() {
				request.getSKYDownloadListener().onDownloadProgress(request.getRequestId(), totalBytes, downloadedBytes, progress);
			}
		});
	}

	/**
	 * *******************************上传****************************************
	 */
	/**
	 * 退出
	 */
	public void quit() {
		mQuit = true;
		interrupt();
	}
}
