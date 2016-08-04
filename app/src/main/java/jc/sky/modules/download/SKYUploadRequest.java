package jc.sky.modules.download;

import android.net.Uri;

import com.squareup.okhttp.Headers;

import jc.sky.common.utils.SKYCheckUtils;


/**
 * @创建人 sky
 * @创建时间 15/4/3 下午3:25
 * @类描述 上传请求
 */
public class SKYUploadRequest extends SKYBaseRequest {

	private Uri					uploadUri;

	private SKYUploadListener j2WUploadListener;

	Headers.Builder				headers;

	SKYContentType j2WContentType;

	SKYUploadBody j2WUploadBody;

	/**
	 * 初始化
	 *
	 * @param uri
	 *            地址
	 * @param j2WUploadBody
	 *            请求体
	 * @param j2WContentType
	 *            类型
	 */
	public SKYUploadRequest(Uri uri, SKYUploadBody j2WUploadBody, SKYContentType j2WContentType) {
		if (SKYCheckUtils.isEmpty(j2WUploadBody.headerName) || SKYCheckUtils.isEmpty(j2WUploadBody.headerValue)) {
			throw new IllegalArgumentException("文件体头信息不能为空！");
		}

		String scheme = uri.getScheme();
		if (scheme == null || (!scheme.equals("http") && !scheme.equals("https"))) {
			throw new IllegalArgumentException("上传地址只能是  HTTP/HTTPS 开头！ uri : " + uri);
		}
		setDownloadState(SKYDownloadManager.STATUS_PENDING);
		this.uploadUri = uri;
		this.j2WUploadBody = j2WUploadBody;
		this.headers = new Headers.Builder();
		this.j2WContentType = j2WContentType;
	}

	/**
	 * 添加头信息
	 *
	 * @param headerName
	 * @param headerValue
	 * @return
	 */
	public SKYUploadRequest addHeader(String headerName, String headerValue) {
		headers.add(headerName, headerValue);
		return this;
	}

	/**
	 * 添加头信息
	 *
	 * @param headerName
	 * @param headerValue
	 * @return
	 */
	public SKYUploadRequest addHeaderBody(String headerName, String headerValue) {
		headers.add(headerName, headerValue);
		return this;
	}

	/**
	 * 返回请求头信息
	 * 
	 * @return
	 */
	public Headers getHeaders() {
		return headers.build();
	}

	/**
	 * 返回请求体
	 * 
	 * @return
	 */
	public SKYUploadBody getJ2WUploadBody() {
		return j2WUploadBody;
	}

	/**
	 * 返回类型
	 * 
	 * @return
	 */
	public SKYContentType getJ2WContentType() {
		return j2WContentType;
	}

	/**
	 * 上传地址
	 *
	 * @return 地址
	 */
	public Uri getUploadUrl() {
		return uploadUri;
	}

	/**
	 * 获取上传事件
	 *
	 * @return 事件
	 */
	public SKYUploadListener getJ2WUploadListener() {
		return j2WUploadListener;
	}

	/**
	 * 设置下载事件
	 *
	 * @param j2WUploadListener
	 *            事件
	 * @return
	 */
	public SKYUploadRequest setJ2WUploadListener(SKYUploadListener j2WUploadListener) {
		this.j2WUploadListener = j2WUploadListener;
		return this;
	}
}
