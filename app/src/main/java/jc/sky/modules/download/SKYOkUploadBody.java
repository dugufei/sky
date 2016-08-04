package jc.sky.modules.download;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.internal.Util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jc.sky.modules.log.L;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * @创建人 sky
 * @创建时间 15/4/7 上午9:36
 * @类描述 Okhttp 请求体
 */
public class SKYOkUploadBody extends RequestBody {

	private static final int		SEGMENT_SIZE	= 2048; // okio.Segment.SIZE

	private final File				file;					// 上传的问题件

	private final SKYUploadListener listener;				// 事件

	private long					totalSize;				// 待上传文件总大小

	private final SKYUploadRequest SKYUploadRequest;		// 请求

	public SKYOkUploadBody(SKYUploadRequest SKYUploadRequest, SKYUploadListener listener) {
		this.file = SKYUploadRequest.getSKYUploadBody().file;
		if(file != null){
			totalSize = file.length();
		}
		this.SKYUploadRequest = SKYUploadRequest;
		this.listener = listener;
	}

	@Override public long contentLength() {
		return totalSize;
	}

	@Override public MediaType contentType() {
		final StringBuilder buffer = new StringBuilder();
		buffer.append(SKYUploadRequest.getSKYContentType().getMimeType());
		if (SKYUploadRequest.getSKYContentType().getCharset() != null) {
			buffer.append("; charset=");
			buffer.append(SKYUploadRequest.getSKYContentType().getCharset());
		}
		return MediaType.parse(buffer.toString());
	}

	@Override public void writeTo(BufferedSink sink) throws IOException {
		Source source = null;
		try {

			source = Okio.source(file);
			long total = 0;
			long read;

			while ((read = source.read(sink.buffer(), SEGMENT_SIZE)) != -1) {
				if (SKYUploadRequest.isCanceled()) {
					L.i("取消的请求Id " + SKYUploadRequest.getRequestId());
					return;
				}
				total += read;
				sink.flush();
				int progres = (int) (100 * total / totalSize);
				this.listener.onUploadProgress(SKYUploadRequest.getRequestId(), totalSize, total, progres);// 进度
			}
		} finally {
			Util.closeQuietly(source);
		}
	}

	/**
	 * 建造
	 * 
	 * @return
	 */
	public RequestBody build() {
		// 请求头信息
		Headers headers = SKYUploadRequest.getSKYUploadBody().getHeader();
		MultipartBuilder multipartBuilder = new MultipartBuilder().type(MultipartBuilder.FORM);
		if(file != null){
			multipartBuilder.addPart(headers, this);
		}
		List<SKYFromData> SKYHeadersBeans = SKYUploadRequest.getSKYUploadBody().SKYFromData;
		if (SKYHeadersBeans != null && SKYHeadersBeans.size() > 0) {
			int count = SKYUploadRequest.getSKYUploadBody().SKYFromData.size();
			for (int i = 0; i < count; i++) {
				multipartBuilder.addFormDataPart(SKYHeadersBeans.get(i).key,SKYHeadersBeans.get(i).value);
			}
		}
		RequestBody requestBody = multipartBuilder.build();
		return requestBody;
	}
}
