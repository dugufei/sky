package jc.sky.modules.download;

import java.nio.charset.Charset;

/**
 * @创建人 sky
 * @创建时间 15/4/8 下午2:28
 * @类描述 数据类型
 */
public class SKYContentType {

	public static final SKYContentType APPLICATION_ATOM_XML		= create("application/atom+xml", SKYDownloadConstants.ISO_8859_1);

	public static final SKYContentType APPLICATION_FORM_URLENCODED	= create("application/x-www-form-urlencoded", SKYDownloadConstants.ISO_8859_1);

	public static final SKYContentType APPLICATION_JSON			= create("application/json", SKYDownloadConstants.UTF_8);

	public static final SKYContentType APPLICATION_OCTET_STREAM	= create("application/octet-stream", (Charset) null);

	public static final SKYContentType APPLICATION_SVG_XML			= create("application/svg+xml", SKYDownloadConstants.ISO_8859_1);

	public static final SKYContentType APPLICATION_XHTML_XML		= create("application/xhtml+xml", SKYDownloadConstants.ISO_8859_1);

	public static final SKYContentType APPLICATION_XML				= create("application/xml", SKYDownloadConstants.ISO_8859_1);

	public static final SKYContentType MULTIPART_FORM_DATA			= create("multipart/form-data", SKYDownloadConstants.ISO_8859_1);

	public static final SKYContentType TEXT_HTML					= create("text/html", SKYDownloadConstants.ISO_8859_1);

	public static final SKYContentType TEXT_PLAIN					= create("text/plain", SKYDownloadConstants.ISO_8859_1);

	public static final SKYContentType TEXT_XML					= create("text/xml", SKYDownloadConstants.ISO_8859_1);

	public static final SKYContentType IMAGE_PNG					= create("image/png", (Charset) null);

	public static final SKYContentType IMAGE_JPG					= create("image/jpeg", (Charset) null);

	public static final SKYContentType WILDCARD					= create("*/*", (Charset) null);

	public static final SKYContentType DEFAULT_TEXT				= TEXT_PLAIN;

	public static final SKYContentType DEFAULT_FILE				= MULTIPART_FORM_DATA;

	/**
	 * 创建类型
	 * 
	 * @param mimeType
	 * @param charset
	 * @return
	 */
	public static SKYContentType create(final String mimeType, final Charset charset) {
		return new SKYContentType(mimeType, charset);
	}

	private final String	mimeType;

	private final Charset	charset;

	SKYContentType(final String mimeType, final Charset charset) {
		this.mimeType = mimeType;
		this.charset = charset;
	}

	public String getMimeType() {
		return this.mimeType;
	}

	public Charset getCharset() {
		return this.charset;
	}
}
