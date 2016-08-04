package jc.sky.modules.download;

import android.net.Uri;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jc.sky.SKYHelper;

/**
 * @创建人 sky
 * @创建时间 15/4/3 下午12:07
 * @类描述 下载管理器
 */
public class SKYDownloadManager implements SKYIDownloadMagnager {

	private SKYDownloadRequest j2WDownloadRequest;

	private SKYUploadRequest j2WUploadRequest;

	private SKYDownloadRequestQueue mRequestQueue;

	public SKYDownloadManager() {
		mRequestQueue = new SKYDownloadRequestQueue();
		mRequestQueue.start();
	}

	public SKYDownloadManager(int threadPoolSize) {
		mRequestQueue = new SKYDownloadRequestQueue(threadPoolSize);
		mRequestQueue.start();
	}

	/**
	 * 添加指令
	 * 
	 * @param request
	 *            请求指令
	 * @return
	 */
	@Override public int add(SKYBaseRequest request) {
		if (request == null) {
			throw new IllegalArgumentException("请求不能为空");
		}
		return mRequestQueue.add(request);
	}

	/**
	 * 取消指令
	 * 
	 * @param downloadId
	 *            请求ID
	 * @return
	 */
	@Override public int cancel(int downloadId) {
		return mRequestQueue.cancel(downloadId);
	}

	/**
	 * 取消所有指令
	 */
	@Override public void cancelAll() {
		mRequestQueue.cancelAll();
	}

	/**
	 * 查询下载状态
	 * 
	 * @param downloadId
	 *            请求ID
	 * @return
	 */
	@Override public int query(int downloadId) {
		return mRequestQueue.query(downloadId);
	}

	/**
	 * 取消所有待决运行的请求，并释放所有的工作线程
	 */
	@Override public void release() {
		if (mRequestQueue != null) {
			mRequestQueue.release();
			mRequestQueue = null;
		}
	}

	/**
	 * 下载 默认路径 /storage/emulated/0/Android/data/APP包
	 *
	 * @param url
	 *            下载地址
	 * @param fileName
	 *            文件名
	 * @param j2WDownloadListener
	 *            事件
	 */
	@Override public int download(String url, String fileName, SKYDownloadListener j2WDownloadListener) {
		return download(url, SKYHelper.getInstance().getExternalCacheDir().toString(), fileName, j2WDownloadListener);
	}

	/**
	 * 下载
	 *
	 * @param url
	 *            下载地址
	 * @param destination
	 *            路径
	 * @param fileName
	 *            文件名
	 * @param j2WDownloadListener
	 *            事件
	 */
	@Override public int download(String url, String destination, String fileName, SKYDownloadListener j2WDownloadListener) {
		StringBuilder sDestination = new StringBuilder(destination);
		StringBuilder sFileName = new StringBuilder(fileName);

		if (destination.endsWith("/")) {
			// 删除斜线 - 防止双斜线
			sDestination.deleteCharAt(destination.length() - 1);
		}

		if (fileName.startsWith("/")) {
			// 删除斜线 - 防止双斜线
			sFileName.deleteCharAt(0);
		}

		Uri uri = Uri.parse(url); // 下载地址
		StringBuilder stringBuilder = new StringBuilder(sDestination.toString());
		stringBuilder.append("/");
		stringBuilder.append(sFileName.toString());
		Uri destinationUri = Uri.parse(stringBuilder.toString());// 目标地址
		return download(uri, destinationUri, j2WDownloadListener);
	}

	/**
	 * 下载
	 *
	 * @param downloadUri
	 *            下载地址
	 * @param destinationUri
	 *            路径
	 * @param j2WDownloadListener
	 *            事件
	 */
	@Override public int download(Uri downloadUri, Uri destinationUri, SKYDownloadListener j2WDownloadListener) {
		j2WDownloadRequest = new SKYDownloadRequest(downloadUri);
		j2WDownloadRequest.setDestinationUrl(destinationUri);
		j2WDownloadRequest.setJ2WDownloadListener(j2WDownloadListener);
		return add(j2WDownloadRequest);
	}

	/**
	 * 上传
	 *
	 * @param uploadUri
	 *            上传地址
	 * @param j2WUploadHeader
	 * @param j2WUploadBody
	 *            请求体
	 * @param j2WUploadListener
	 *            上传事件
	 * @return
	 */
	@Override public int upload(Uri uploadUri, SKYUploadHeader j2WUploadHeader, SKYUploadBody j2WUploadBody, SKYUploadListener j2WUploadListener) {
		List<SKYUploadHeader> j2WUploadHeaders = new ArrayList<>();
		if (j2WUploadHeader != null) {
			j2WUploadHeaders.add(j2WUploadHeader);
		}
		return upload(uploadUri, j2WUploadHeaders, j2WUploadBody, SKYContentType.DEFAULT_FILE, j2WUploadListener);
	}

	/**
	 * 上传
	 * 
	 * @param uploadUri
	 *            上传地址
	 * @param j2WUploadHeaders
	 *            上传头信息
	 * @param j2WUploadBody
	 *            上传体
	 * @param j2WContentType
	 *            类型
	 * @param j2WUploadListener
	 *            事件
	 * @return
	 */
	@Override public int upload(Uri uploadUri, List<SKYUploadHeader> j2WUploadHeaders, SKYUploadBody j2WUploadBody, SKYContentType j2WContentType, SKYUploadListener j2WUploadListener) {
		j2WUploadRequest = new SKYUploadRequest(uploadUri, j2WUploadBody, j2WContentType);
		j2WUploadRequest.setJ2WUploadListener(j2WUploadListener);
		for (SKYUploadHeader j2WUploadHeader : j2WUploadHeaders) {
			j2WUploadRequest.addHeader(j2WUploadHeader.headerName, j2WUploadHeader.headerValue);
		}
		return add(j2WUploadRequest);
	}

	/**
	 * 上传
	 * 
	 * @param uploadUri
	 *            上传地址
	 * @param file
	 *            文件
	 * @param j2WUploadListener
	 *            事件
	 * @return
	 */
	@Override public int upload(String uploadUri, File file, SKYUploadListener j2WUploadListener) {
		return upload(uploadUri, file, null, j2WUploadListener);
	}

	/**
	 * 上传
	 *
	 * @param uploadUri
	 *            上传地址
	 * @param file
	 *            文件
	 * @param j2WUploadHeader
	 *            请求头信息
	 * @param j2WUploadListener
	 *            事件
	 * @return
	 */
	@Override public int upload(String uploadUri, File file, SKYUploadHeader j2WUploadHeader, SKYUploadListener j2WUploadListener) {
		List<SKYUploadHeader> j2WUploadHeaders = new ArrayList<>();
		if (j2WUploadHeader != null) {
			j2WUploadHeaders.add(j2WUploadHeader);
		}
		return upload(uploadUri, j2WUploadHeaders, file, j2WUploadListener);
	}

	/**
	 * 上传
	 *
	 * @param uploadUri
	 *            上传地址
	 * @param j2WUploadHeaders
	 *            请求头信息 数组
	 * @param file
	 *            文件
	 * @param j2WUploadListener
	 *            事件
	 * @return
	 */
	@Override public int upload(String uploadUri, List<SKYUploadHeader> j2WUploadHeaders, File file, SKYUploadListener j2WUploadListener) {
		return upload(uploadUri, j2WUploadHeaders, file, SKYContentType.DEFAULT_FILE, j2WUploadListener);
	}

	/**
	 * 上传
	 *
	 * @param uploadUri
	 *            上传地址
	 * @param j2WUploadHeaders
	 *            请求头信息 数组
	 * @param file
	 *            文件
	 * @param j2WUploadListener
	 *            事件
	 * @return
	 */
	@Override public int upload(String uploadUri, List<SKYUploadHeader> j2WUploadHeaders, File file, SKYContentType j2WContentType, SKYUploadListener j2WUploadListener) {
		Uri uri = Uri.parse(uploadUri);
		SKYUploadBody j2WUploadBody = new SKYUploadBody();
		j2WUploadBody.headerName = SKYUploadBody.CONTENT_DISPOSITION;
		j2WUploadBody.headerValue = "file";
		j2WUploadBody.file = file;

		return upload(uri, j2WUploadHeaders, j2WUploadBody, j2WContentType, j2WUploadListener);
	}

	@Override public int upload(String uploadUri, List<SKYUploadHeader> j2WUploadHeaders, SKYUploadBody j2WUploadBody, SKYContentType j2WContentType, SKYUploadListener j2WUploadListener) {
		Uri uri = Uri.parse(uploadUri);
		return upload(uri, j2WUploadHeaders, j2WUploadBody, j2WContentType, j2WUploadListener);
	}
}
