package sky.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dalvik.system.DexFile;
import sky.core.exception.SKYNullPointerException;
import sky.core.methodModule.SKYIModule;

/**
 * @author sky
 * @version 1.0 on 2017-10-31 下午2:22
 * @see SKYModuleUtils
 */
final class SKYModuleUtils {

	/**
	 * 初始化 module
	 *
	 * @param context
	 *            上下文
	 */
	public synchronized static final void initModule(Context context) {
		try {

			SKYModulesManage skyModulesManage = SKYHelper.getManage();

			long e = System.currentTimeMillis();
			Object skyMap;
			if (!SKYHelper.isLogOpen() && !isNewVersion(context)) {
				L.d("Sky::", "加载缓存.");
				skyMap = new HashSet(context.getSharedPreferences("SP_SKY_CACHE", 0).getStringSet("SKY_MAP", new HashSet()));
			} else {
				L.d("Sky::", "获取运行时，指定包目录下的所有信息并缓存到skyMap");
				skyMap = SKYModuleUtils.getFileNameByPackageName(context, "com.sky.android.module.biz");
				if (!((Set) skyMap).isEmpty()) {
					context.getSharedPreferences("SP_SKY_CACHE", 0).edit().putStringSet("SKY_MAP", (Set) skyMap).apply();
				}
			}

			L.d("Sky::", "Find router map finished, map size = " + ((Set) skyMap).size() + ", cost " + (System.currentTimeMillis() - e) + " ms.");
			e = System.currentTimeMillis();
			Iterator var5 = ((Set) skyMap).iterator();

			while (var5.hasNext()) {
				String className = (String) var5.next();
				String bizName = StringUtils.removeStart(className, "com.sky.android.module.biz.Sky$$Biz$$");
				skyModulesManage.provideModule.put(bizName, (Class<? extends SKYIModule>) Class.forName(className));
			}

			L.d("Sky::", "加载完毕, cost " + (System.currentTimeMillis() - e) + " ms.");
		} catch (Exception var7) {
			throw new SKYNullPointerException("Sky::Sky init logistics center exception! [" + var7.getMessage() + "]");
		}
	}

	/**
	 * 通过指定包名，扫描包下面包含的所有的ClassName
	 *
	 * @param context
	 *            U know
	 * @param packageName
	 *            包名
	 * @return 所有class的集合
	 */
	static Set<String> getFileNameByPackageName(Context context, final String packageName) throws PackageManager.NameNotFoundException, IOException, InterruptedException {
		final Set<String> classNames = new HashSet<>();

		List<String> paths = getSourcePaths(context);
		final CountDownLatch parserCtl = new CountDownLatch(paths.size());

		for (final String path : paths) {

			SKYHelper.threadPoolHelper().getWorkExecutorService().execute(new Runnable() {

				@Override public void run() {
					DexFile dexfile = null;

					try {
						if (path.endsWith(".zip")) {
							// NOT use new DexFile(path), because it will throw "permission error in
							// /data/dalvik-cache"
							dexfile = DexFile.loadDex(path, path + ".tmp", 0);
						} else {
							dexfile = new DexFile(path);
						}

						Enumeration<String> dexEntries = dexfile.entries();
						while (dexEntries.hasMoreElements()) {
							String className = dexEntries.nextElement();
							if (className.startsWith(packageName)) {
								classNames.add(className);
							}
						}
					} catch (Throwable ignore) {
						L.e("ARouter", "Scan map file in dex files made error.", ignore);
					} finally {
						if (null != dexfile) {
							try {
								dexfile.close();
							} catch (Throwable ignore) {
							}
						}

						parserCtl.countDown();
					}
				}
			});
		}

		parserCtl.await();

		L.d("Filter " + classNames.size() + " classes by packageName <" + packageName + ">");
		return classNames;
	}

	/**
	 * get all the dex path
	 *
	 * @param context
	 *            the application context
	 * @return all the dex path
	 * @throws PackageManager.NameNotFoundException
	 * @throws IOException
	 */
	static List<String> getSourcePaths(Context context) throws PackageManager.NameNotFoundException, IOException {
		ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
		File sourceApk = new File(applicationInfo.sourceDir);

		List<String> sourcePaths = new ArrayList<>();
		sourcePaths.add(applicationInfo.sourceDir); // add the default apk path

		// the prefix of extracted file, ie: test.classes
		String extractedFilePrefix = sourceApk.getName() + ".classes";

		// 如果VM已经支持了MultiDex，就不要去Secondary Folder加载 Classesx.zip了，那里已经么有了
		// 通过是否存在sp中的multidex.version是不准确的，因为从低版本升级上来的用户，是包含这个sp配置的
		if (!isVMMultidexCapable()) {
			// the total dex numbers
			int totalDexNumber = getMultiDexPreferences(context).getInt("dex.number", 1);
			File dexDir = new File(applicationInfo.dataDir, SECONDARY_FOLDER_NAME);

			for (int secondaryNumber = 2; secondaryNumber <= totalDexNumber; secondaryNumber++) {
				// for each dex file, ie: test.classes2.zip, test.classes3.zip...
				String fileName = extractedFilePrefix + secondaryNumber + ".zip";
				File extractedFile = new File(dexDir, fileName);
				if (extractedFile.isFile()) {
					sourcePaths.add(extractedFile.getAbsolutePath());
					// we ignore the verify zip part
				} else {
					throw new IOException("Missing extracted secondary dex file '" + extractedFile.getPath() + "'");
				}
			}
		}

		if (SKYHelper.isLogOpen()) { // Search instant run support only debuggable
			sourcePaths.addAll(tryLoadInstantRunDexFile(applicationInfo));
		}
		return sourcePaths;
	}

	private static List<String> tryLoadInstantRunDexFile(ApplicationInfo applicationInfo) {
		ArrayList instantRunSourcePaths = new ArrayList();
		if (Build.VERSION.SDK_INT >= 21 && null != applicationInfo.splitSourceDirs) {
			instantRunSourcePaths.addAll(Arrays.asList(applicationInfo.splitSourceDirs));
			L.d("Sky::", "Found InstantRun support");
		} else {
			try {
				Class e = Class.forName("com.android.tools.fd.runtime.Paths");
				Method getDexFileDirectory = e.getMethod("getDexFileDirectory", new Class[] { String.class });
				String instantRunDexPath = (String) getDexFileDirectory.invoke((Object) null, new Object[] { applicationInfo.packageName });
				File instantRunFilePath = new File(instantRunDexPath);
				if (instantRunFilePath.exists() && instantRunFilePath.isDirectory()) {
					File[] dexFile = instantRunFilePath.listFiles();
					File[] var7 = dexFile;
					int var8 = dexFile.length;

					for (int var9 = 0; var9 < var8; ++var9) {
						File file = var7[var9];
						if (null != file && file.exists() && file.isFile() && file.getName().endsWith(".dex")) {
							instantRunSourcePaths.add(file.getAbsolutePath());
						}
					}

					L.d("Sky::", "Found InstantRun support");
				}
			} catch (Exception var11) {
				L.e("Sky::", "InstantRun support error, " + var11.getMessage());
			}
		}

		return instantRunSourcePaths;
	}

	private static boolean isVMMultidexCapable() {
		boolean isMultidexCapable = false;
		String vmName = null;

		try {
			if (isYunOS()) {
				vmName = "\'YunOS\'";
				isMultidexCapable = Integer.valueOf(System.getProperty("ro.build.version.sdk")).intValue() >= 21;
			} else {
				vmName = "\'Android\'";
				String ignore = System.getProperty("java.vm.version");
				if (ignore != null) {
					Matcher matcher = Pattern.compile("(\\d+)\\.(\\d+)(\\.\\d+)?").matcher(ignore);
					if (matcher.matches()) {
						try {
							int ignore1 = Integer.parseInt(matcher.group(1));
							int minor = Integer.parseInt(matcher.group(2));
							isMultidexCapable = ignore1 > 2 || ignore1 == 2 && minor >= 1;
						} catch (NumberFormatException var6) {
							;
						}
					}
				}
			}
		} catch (Exception var7) {
			;
		}

		L.i("Sky::", "VM with name " + vmName + (isMultidexCapable ? " has multidex support" : " does not have multidex support"));
		return isMultidexCapable;
	}

	@SuppressLint("WrongConstant") private static SharedPreferences getMultiDexPreferences(Context context) {
		return context.getSharedPreferences("multidex.version", Build.VERSION.SDK_INT < 11 ? 0 : 4);
	}

	private static final String SECONDARY_FOLDER_NAME;

	private static boolean isYunOS() {
		try {
			String ignore = System.getProperty("ro.yunos.version");
			String vmName = System.getProperty("java.vm.name");
			return vmName != null && vmName.toLowerCase().contains("lemur") || ignore != null && ignore.trim().length() > 0;
		} catch (Exception var2) {
			return false;
		}
	}


	/**
	 * 判断是否是新版本
	 *
	 * @param context
	 * @return
	 */
	static boolean isNewVersion(Context context) {
		PackageInfo packageInfo = getPackageInfo(context);
		if (null != packageInfo) {
			String versionName = packageInfo.versionName;
			int versionCode = packageInfo.versionCode;
			SharedPreferences sp = context.getSharedPreferences("SP_SKY_CACHE", 0);
			if (versionName.equals(sp.getString("LAST_VERSION_NAME", null)) && versionCode == sp.getInt("LAST_VERSION_CODE", -1)) {
				return false;
			} else {
				sp.edit().putString("LAST_VERSION_NAME", versionName).putInt("LAST_VERSION_CODE", versionCode).apply();
				return true;
			}
		} else {
			return true;
		}
	}


	/**
	 * 获取包信息
	 *
	 * @param context
	 * @return
	 */
	static PackageInfo getPackageInfo(Context context) {
		PackageInfo packageInfo = null;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
		} catch (Exception ex) {
			L.e("Get package info error.");
		}

		return packageInfo;
	}

	static {
		SECONDARY_FOLDER_NAME = "code_cache" + File.separator + "secondary-dexes";
	}
}
