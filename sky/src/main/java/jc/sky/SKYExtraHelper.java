package jc.sky;

import jc.sky.modules.SKYExtraModulesManage;
import jc.sky.modules.download.SKYDownloadManager;
import jc.sky.modules.file.SKYFileCacheManage;
import jc.sky.modules.job.SKYIJobService;
import sky.core.SKYHelper;

/**
 * @author sky
 * @version 1.0 on 2017-11-28 下午3:13
 * @see SKYExtraHelper
 */
public class SKYExtraHelper extends SKYHelper {

	/**
	 * 任务管理器
	 *
	 * @return 返回值
	 */
	public static SKYIJobService jobSerceHelper() {
        SKYExtraModulesManage  manage = getManage();
		return manage.skyJobService();
	}

	/**
	 * 文件缓存管理器
	 *
	 * @return 返回值
	 */
	public static SKYFileCacheManage fileCacheManage() {
        SKYExtraModulesManage  manage = getManage();
        return manage.skyFileCacheManage();
	}

	/**
	 * 下载器工具
	 *
	 * @return 返回值
	 */
	public static SKYDownloadManager downloader() {
        SKYExtraModulesManage  manage = getManage();
        return manage.skyDownloadManager();
	}

}
