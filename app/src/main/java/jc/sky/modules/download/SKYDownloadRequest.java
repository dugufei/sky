package jc.sky.modules.download;

import android.net.Uri;

/**
 * @author sky
 * @version 版本
 */
public class SKYDownloadRequest extends SKYBaseRequest {

	/**
	 * 初始化
	 * 
	 * @param uri
	 *            地址
	 */
	public SKYDownloadRequest(Uri uri) {
		if (uri == null) {
			throw new NullPointerException();
		}

		String scheme = uri.getScheme();
		if (scheme == null || (!scheme.equals("http") && !scheme.equals("https"))) {
			throw new IllegalArgumentException("下载地址只能是  HTTP/HTTPS 开头！ uri : " + uri);
		}
		setDownloadState(SKYDownloadManager.STATUS_PENDING);
		downloadUrl = uri;
	}

	/**
	 * 下载事件
	 */
	private SKYDownloadListener SKYDownloadListener;

	/**
	 * 下载后的文件路径名
	 */
	private Uri					downloadUrl;

	/**
	 * 下载URL
	 */

	private Uri					destinationUrl;

	/**
	 * 获取下载事件
	 *
	 * @return 事件
	 */
	public SKYDownloadListener getSKYDownloadListener() {
		return SKYDownloadListener;
	}

	/**
	 * 下载地址
	 * 
	 * @return 地址
	 */
	public Uri getDownloadUrl() {
		return downloadUrl;
	}

	/**
	 * 下载后目标地址
	 * 
	 * @return 地址
	 */
	public Uri getDestinationUrl() {
		return destinationUrl;
	}

	/**
	 * 设置目标地址
	 * 
	 * @param destinationUrl
	 *            参数
	 * @return 返回值
	 */
	public SKYDownloadRequest setDestinationUrl(Uri destinationUrl) {
		this.destinationUrl = destinationUrl;
        return this;
	}

	/**
	 * 设置下载事件
	 * 
	 * @param SKYDownloadListener
	 *            事件
	 * @return 返回值
	 */
	public SKYDownloadRequest setSKYDownloadListener(SKYDownloadListener SKYDownloadListener) {
		this.SKYDownloadListener = SKYDownloadListener;
		return this;
	}

}
