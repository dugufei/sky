package jc.sky.modules.file;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sky.core.L;
import sky.core.SKYHelper;

/**
 * @author sky
 * @version 版本
 */
public class SKYFileCacheManage {

	private final String	ENCODING	= "utf8";

	private final String	FILE_SUFFIX	= ".txt";

	public String			BASE_CACHE_PATH;

	private final String	TAG			= "CACHE_UTILS";

	/**
	 * @param file
	 *            文件
	 */
	public void configureCustomerCache(File file) {
		BASE_CACHE_PATH = file.getAbsolutePath();

		if (file.mkdirs()) {
			if (SKYHelper.isLogOpen()) {
				Log.d(TAG, BASE_CACHE_PATH + " created.");
			}
		}
	}

	/**
	 * @param context
	 *            参数
	 */
	public void configurePhoneCache(Context context) {
		BASE_CACHE_PATH = context.getCacheDir().getAbsolutePath();
	}

	/**
	 * @param name
	 *            参数
	 * @return 返回值
	 */
	private String pathForCacheEntry(String name) {
		return BASE_CACHE_PATH + File.separator + name + FILE_SUFFIX;
	}

	/**
	 * @param dataString
	 *            参数
	 * @param <T>
	 *            参数
	 * @return 返回值
	 */
	private <T> List<Map<String, T>> dataMapsFromJson(String dataString) {
		if (StringUtils.isEmpty(dataString)) return new ArrayList<>();

		try {
			Type listType = new TypeToken<List<Map<String, T>>>() {}.getType();
			return SKYGsonHelper.buildGson().fromJson(dataString, listType);
		} catch (Exception e) {
			if (SKYHelper.isLogOpen()) {
				Log.d(TAG, "failed to read json" + e.toString());
			}
			return new ArrayList<>();
		}
	}

	/**
	 * @param dataMaps
	 *            参数
	 * @param <T>
	 *            参数
	 * @return 返回值
	 */
	private <T> String dataMapstoJson(List<Map<String, T>> dataMaps) {
		try {
			return SKYGsonHelper.buildGson().toJson(dataMaps);
		} catch (Exception e) {
			if (SKYHelper.isLogOpen()) {
				Log.d(TAG, "failed to write json" + e.toString());
			}
			return "[]";
		}
	}

	/**
	 * @param fileName
	 *            the name of the file
	 * @return the content of the file, null if there is no such file
	 */
	public String readFile(String fileName) {
		try {
			return IOUtils.toString(new FileInputStream(pathForCacheEntry(fileName)), ENCODING);
		} catch (IOException e) {
			if (SKYHelper.isLogOpen()) {
				Log.d(TAG, "read cache file failure" + e.toString());
			}
			return null;
		}
	}

	/**
	 * @param fileName
	 *            the name of the file
	 * @param fileContent
	 *            the content of the file
	 */
	public void writeFile(String fileName, String fileContent) {
		try {
			IOUtils.write(fileContent, new FileOutputStream(pathForCacheEntry(fileName)), ENCODING);
		} catch (IOException e) {
			if (SKYHelper.isLogOpen()) {
				Log.d(TAG, "write cache file failure" + e.toString());
			}
		}
	}

	/**
	 * @param <T>
	 *            参数
	 * @param fileName
	 *            the name of the file
	 * @param dataMaps
	 *            the map list you want to store
	 */
	public <T> void writeDataMapsFile(String fileName, List<Map<String, T>> dataMaps) {
		writeFile(fileName, dataMapstoJson(dataMaps));
	}

	/**
	 * @param <T>
	 *            参数
	 * @param fileName
	 *            the name of the file
	 * @return the map list you previous stored, an empty {@link List} will be
	 *         returned if there is no such file
	 */
	public <T> List<Map<String, T>> readDataMapsFile(String fileName) {
		return dataMapsFromJson(readFile(fileName));
	}

	/**
	 * @param <T>
	 *            参数
	 * @param dataString
	 *            参数
	 * @param t
	 *            参数
	 * @param <T>
	 *            参数
	 * @return 返回值
	 */
	private <T> T objectFromJson(String dataString, Type t) {
		try {
			return SKYGsonHelper.buildGson().fromJson(dataString, t);
		} catch (Exception e) {
			if (SKYHelper.isLogOpen()) {
				Log.v(TAG, "failed to read json" + e.toString());
			}
			return null;
		}
	}

	/**
	 * @param dataString
	 *            参数
	 * @param t
	 *            番薯
	 * @param <T>
	 *            参数
	 * @return 返回值
	 */
	private <T> T objectFromJson(String dataString, Class<T> t) {
		try {
			return SKYGsonHelper.buildGson().fromJson(dataString, t);
		} catch (Exception e) {
			if (SKYHelper.isLogOpen()) {
				Log.v(TAG, "failed to read json" + e.toString());
			}
			return null;
		}
	}

	/**
	 * @param o
	 *            参数
	 * @param <T>
	 *            参数
	 * @return 返回值
	 */
	private <T> String objectToJson(T o) {
		try {
			return SKYGsonHelper.buildGson().toJson(o);
		} catch (Exception e) {
			if (SKYHelper.isLogOpen()) {
				Log.v(TAG, "failed to write json" + e.toString());
			}
			return null;
		}
	}

	/**
	 * @param fileName
	 *            the name of the file
	 * @param object
	 *            the object you want to store
	 * @param <T>
	 *            a class extends from {@link Object}
	 */
	public <T> void writeObjectFile(String fileName, T object) {
		writeFile(fileName, objectToJson(object));
	}

	/**
	 * @param fileName
	 *            the name of the file
	 * @param type
	 *            参数
	 * @param <T>
	 *            参数
	 * @return 返回值
	 */
	public <T> T readObjectFile(String fileName, Type type) {
		return objectFromJson(readFile(fileName), type);
	}

	/**
	 * @param fileName
	 *            参数
	 * @param clazz
	 *            参数
	 * @param <T>
	 *            参数
	 * @return 返回值
	 */
	public <T> T readObjectFile(String fileName, Class<T> clazz) {
		return objectFromJson(readFile(fileName), clazz);
	}

	/**
	 * @param dataString
	 *            参数
	 * @param <T>
	 *            参数
	 * @return 返回值
	 */
	private <T> Map<String, T> dataMapFromJson(String dataString) {
		if (StringUtils.isEmpty(dataString)) return new HashMap<String, T>();

		try {
			Type t = new TypeToken<Map<String, T>>() {}.getType();
			return SKYGsonHelper.buildGson().fromJson(dataString, t);
		} catch (Exception e) {
			if (SKYHelper.isLogOpen()) {
				L.v(TAG, "failed to read json" + e.toString());
			}
			return new HashMap<String, T>();
		}
	}

	/**
	 * @param dataMap
	 *            参数
	 * @param <T>
	 *            参数
	 * @return 返回值
	 */
	private <T> String dataMaptoJson(Map<String, T> dataMap) {
		try {
			return SKYGsonHelper.buildGson().toJson(dataMap);
		} catch (Exception e) {
			if (SKYHelper.isLogOpen()) {
				L.v(TAG, "failed to write json" + e.toString());
			}
			return "{}";
		}
	}

	/**
	 * @param fileName
	 *            the name of the file
	 * @param dataMap
	 *            the map data you want to store
	 * @param <T>
	 *            参数
	 */
	public <T> void writeDataMapFile(String fileName, Map<String, T> dataMap) {
		writeFile(fileName, dataMaptoJson(dataMap));
	}

	/**
	 * @param fileName
	 *            the name of the file
	 * @param <T>
	 *            参数
	 * @return the map data you previous stored
	 */
	public <T> Map<String, T> readDataMapFile(String fileName) {
		return dataMapFromJson(readFile(fileName));
	}

	/**
	 * delete the file with fileName
	 * 
	 * @param fileName
	 *            the name of the file
	 */
	public void deleteFile(String fileName) {
		try {
			FileUtils.forceDelete(new File(pathForCacheEntry(fileName)));
		} catch (IOException e) {
			if (SKYHelper.isLogOpen()) {
				L.v(TAG, "not delete " + e.toString());
			}
		}
	}

	/**
	 * check if there is a cache file with fileName
	 * 
	 * @param fileName
	 *            the name of the file
	 * @return true if the file exits, false otherwise
	 */
	public boolean hasCache(String fileName) {
		return new File(pathForCacheEntry(fileName)).exists();
	}
}
