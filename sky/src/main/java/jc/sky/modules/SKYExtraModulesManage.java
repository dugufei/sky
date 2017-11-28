package jc.sky.modules;

import jc.sky.modules.download.SKYDownloadManager;
import jc.sky.modules.file.SKYFileCacheManage;
import jc.sky.modules.job.SKYJobService;
import sky.core.SKYModulesManage;

/**
 * @author sky
 * @version 1.0 on 2017-11-28 下午4:25
 * @see SKYExtraModulesManage
 */
public class SKYExtraModulesManage extends SKYModulesManage {

	private SKYFileCacheManage	skyFileCacheManage;	// 文件缓存管理器

	private SKYJobService		skyJobService;		// 任务管理器

	private SKYDownloadManager	skyDownloadManager;	// 下载和上传管理

	public SKYFileCacheManage skyFileCacheManage() {
		if (skyFileCacheManage == null) {
			synchronized (SKYFileCacheManage.class) {
				if (skyFileCacheManage == null) {
					skyFileCacheManage = new SKYFileCacheManage();
				}
                skyFileCacheManage.configurePhoneCache(application);
			}
		}
		return skyFileCacheManage;
	}

	public SKYJobService skyJobService() {
		if (skyJobService == null) {
			synchronized (SKYJobService.class) {
				if (skyJobService == null) {
                    skyJobService = new SKYJobService();
				}
			}
		}
		return skyJobService;
	}

	public SKYDownloadManager skyDownloadManager() {
		if (skyDownloadManager == null) {
			synchronized (SKYDownloadManager.class) {
				if (skyDownloadManager == null) {
                    skyDownloadManager = new SKYDownloadManager();
				}
			}
		}
		return skyDownloadManager;
	}
}
