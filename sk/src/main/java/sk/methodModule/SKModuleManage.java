package sk.methodModule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

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
import sk.L;
import sk.SKHelper;

/**
 * @author Sk
 * @version 1.0 on 2019-01-26 10:53 AM
 * @see SKModuleManage
 */
public final class SKModuleManage {

	private final String								SECONDARY_FOLDER_NAME;

	private final ArrayList<Class<? extends SKIModule>>	classArrayList;

	private static final int							VM_WITH_MULTIDEX_VERSION_MAJOR	= 2;

	private static final int							VM_WITH_MULTIDEX_VERSION_MINOR	= 1;

	public SKModuleManage() {
		SECONDARY_FOLDER_NAME = "code_cache" + File.separator + "secondary-dexes";
		classArrayList = new ArrayList<>();
	}

	public ArrayList<Class<? extends SKIModule>> getList() {
		return classArrayList;
	}

	/**
	 * 初始化
	 */
	public final void init() {
		SKHelper.executors().work().execute(new Runnable() {

			@Override public void run() {
				initModule();
			}
		});
	}

	private final void initModule() {
		Context context = SKHelper.getInstance();
		try {
			long e = System.currentTimeMillis();
			Object skMap;
			if (!isNewVersion(context)) {
				L.d("Sk::%s", "加载缓存.");
				skMap = new HashSet(context.getSharedPreferences("SP_SK_CACHE", 0).getStringSet("SK_MAP", new HashSet()));
			} else {
				L.d("Sk::%s", "获取运行时，指定包目录下的所有信息并缓存到skMap");
				skMap = getFileNameByPackageName(context, "sk.module");
				if (!((Set) skMap).isEmpty()) {
					context.getSharedPreferences("SP_SK_CACHE", 0).edit().putStringSet("SK_MAP", (Set) skMap).apply();
				}
			}

			L.d("Sk::%s", "Find sk map finished, map size = " + ((Set) skMap).size() + ", cost " + (System.currentTimeMillis() - e) + " ms.");
			e = System.currentTimeMillis();
			Iterator var5 = ((Set) skMap).iterator();

			while (var5.hasNext()) {
				String className = (String) var5.next();
				classArrayList.add((Class<? extends SKIModule>) Class.forName(className));
			}

			L.d("Sk::%s", "加载完毕, cost " + (System.currentTimeMillis() - e) + " ms.");
		} catch (Exception var7) {
			throw new RuntimeException("Sk::Sk init logistics center exception! [" + var7.getMessage() + "]");
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
	/**
	 * 通过指定包名，扫描包下面包含的所有的ClassName
	 *
	 * @param context
	 *            U know
	 * @param packageName
	 *            包名
	 * @return 所有class的集合
	 */
	private Set<String> getFileNameByPackageName(Context context, final String packageName) throws PackageManager.NameNotFoundException, IOException, InterruptedException {
		final Set<String> classNames = new HashSet<>();

		List<String> paths = getSourcePaths(context);
		final CountDownLatch parserCtl = new CountDownLatch(paths.size());

		for (final String path : paths) {
			SKHelper.executors().work().execute(new Runnable() {

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
						L.e("Sk::%s", "Scan map file in dex files made error.", ignore);
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
	List<String> getSourcePaths(Context context) throws PackageManager.NameNotFoundException, IOException {
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

		if (SKHelper.isLogOpen()) { // Search instant run support only debuggable
			sourcePaths.addAll(tryLoadInstantRunDexFile(applicationInfo));
		}
		return sourcePaths;
	}

	private List<String> tryLoadInstantRunDexFile(ApplicationInfo applicationInfo) {
		ArrayList instantRunSourcePaths = new ArrayList();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && null != applicationInfo.splitSourceDirs) {
			instantRunSourcePaths.addAll(Arrays.asList(applicationInfo.splitSourceDirs));
			L.d("Sk::%s", "Found InstantRun support");
		} else {
			try {
				Class pathsByInstantRun = Class.forName("com.android.tools.fd.runtime.Paths");
				Method getDexFileDirectory = pathsByInstantRun.getMethod("getDexFileDirectory", String.class);
				String instantRunDexPath = (String) getDexFileDirectory.invoke(null, applicationInfo.packageName);

				File instantRunFilePath = new File(instantRunDexPath);
				if (instantRunFilePath.exists() && instantRunFilePath.isDirectory()) {
					File[] dexFile = instantRunFilePath.listFiles();
					for (File file : dexFile) {
						if (null != file && file.exists() && file.isFile() && file.getName().endsWith(".dex")) {
							instantRunSourcePaths.add(file.getAbsolutePath());
						}
					}
					L.d("Sk::%s", "Found InstantRun support");
				}
			} catch (Exception var11) {
				L.e("Sk::%s", "InstantRun support error, " + var11.getMessage());
			}
		}

		return instantRunSourcePaths;
	}

	private boolean isVMMultidexCapable() {
		boolean isMultidexCapable = false;
		String vmName = null;

		try {
			if (isYunOS()) { // YunOS需要特殊判断
				vmName = "'YunOS'";
				isMultidexCapable = Integer.valueOf(System.getProperty("ro.build.version.sdk")) >= 21;
			} else { // 非YunOS原生Android
				vmName = "'Android'";
				String versionString = System.getProperty("java.vm.version");
				if (versionString != null) {
					Matcher matcher = Pattern.compile("(\\d+)\\.(\\d+)(\\.\\d+)?").matcher(versionString);
					if (matcher.matches()) {
						try {
							int major = Integer.parseInt(matcher.group(1));
							int minor = Integer.parseInt(matcher.group(2));
							isMultidexCapable = (major > VM_WITH_MULTIDEX_VERSION_MAJOR) || ((major == VM_WITH_MULTIDEX_VERSION_MAJOR) && (minor >= VM_WITH_MULTIDEX_VERSION_MINOR));
						} catch (NumberFormatException ignore) {
							// let isMultidexCapable be false
						}
					}
				}
			}
		} catch (Exception var7) {
		}

		L.i("Sk::%s", "VM with name " + vmName + (isMultidexCapable ? " has multidex support" : " does not have multidex support"));
		return isMultidexCapable;
	}

	@SuppressLint("WrongConstant") private static SharedPreferences getMultiDexPreferences(Context context) {
		return context.getSharedPreferences("multidex.version", Build.VERSION.SDK_INT < 11 ? 0 : 4);
	}

	/**
	 * 判断系统是否为YunOS系统
	 */
	private static boolean isYunOS() {
		try {
			String version = System.getProperty("ro.yunos.version");
			String vmName = System.getProperty("java.vm.name");
			return (vmName != null && vmName.toLowerCase().contains("lemur")) || (version != null && version.trim().length() > 0);
		} catch (Exception ignore) {
			return false;
		}
	}

	/**
	 * 判断是否是新版本
	 *
	 * @param context
	 * @return
	 */
	private boolean isNewVersion(Context context) {
		PackageInfo packageInfo = getPackageInfo(context);
		if (null != packageInfo) {
			String versionName = packageInfo.versionName;
			int versionCode = packageInfo.versionCode;
			SharedPreferences sp = context.getSharedPreferences("SP_Sk_CACHE", 0);
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
	private PackageInfo getPackageInfo(Context context) {
		PackageInfo packageInfo = null;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
		} catch (Exception ex) {
			L.e("Get package info error.");
		}

		return packageInfo;
	}

}
