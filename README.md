# SKY
接受不完美的自己，享受不断完善的自己 我们的承诺是，每天都要有进步

Gradle 版本
-----------------------------------
1.classpath 'com.android.tools.build:gradle:2.1.2'<br />
2.版本 - gradle-2.14-all.zip<br />

项目引用
-----------------------------------
Project-build.gradle

     buildscript {
         repositories {
             //从中央库里面获取依赖
             jcenter()
         }
         dependencies {
             classpath 'com.android.tools.build:gradle:2.1.2'
         }
     }

     allprojects {
         repositories {
             jcenter()
             //远程仓库
             maven { url "https://github.com/J2W/mvn-repo-j2w/raw/master/repository" }
         }
     }

App-build.gradle:

     dependencies {
        compile 'jc:sky:1.0'
     }


proguard

    #sky--------------------------------------------------------------------------------------------------------
    -keepattributes *Annotation*

    -keepclasseswithmembers class * {
       <init> ();
    }
    -keep class jc.sky.** { *; }

    #butterknife 8.1

    # Retain generated class which implement ViewBinder.
    -keep public class * implements butterknife.internal.ViewBinder { public <init>(); }

    # Prevent obfuscation of types which use ButterKnife annotations since the simple name
    # is used to reflectively look up the generated ViewBinder.
    -keep class butterknife.*
    -keepclasseswithmembernames class * { @butterknife.* <methods>; }
    -keepclasseswithmembernames class * { @butterknife.* <fields>; }

    #nineoldandroids

    -dontwarn com.nineoldandroids.**
    -keep class com.nineoldandroids.** { *;}


    #picasso

     -dontwarn com.squareup.okhttp.**

    #glide

    -keep public class * implements com.bumptech.glide.module.GlideModule
    -keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
        **[] $VALUES;
        public *;
    }

    #eventbus3.0

    -keepclassmembers class ** {
        @org.greenrobot.eventbus.Subscribe <methods>;
    }
    -keep enum org.greenrobot.eventbus.ThreadMode { *; }

    # Only required if you use AsyncExecutor
    -keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
        <init>(java.lang.Throwable);
    }

    #okhttp3
    -keep class com.squareup.okhttp3.** {*;}

    #commons-io-1.3.2.jar
    -keep public class org.apache.commons.** {*;}

    #retrofit2
    -dontwarn retrofit2.**
    -keep class retrofit2.** { *; }
    -keepattributes Signature
    -keepattributes Exceptions

    -keepclasseswithmembers class * {
        @retrofit2.http.* <methods>;
    }
# 结构

技术 | 简述
-------- | --------
[MVP](http://blog.csdn.net/lmj623565791/article/details/46596109) | 项目使用接口 来隔离 view层 和 biz 业务层 , 原理可以看blog
[http](https://github.com/square/retrofit) | 网络使用retrofit2, 初始化进行封装, 通过SKYHelpter.http 使用
[activity] | 采用builder 建造者模式  持续更新
[display] | 支持跳转拦截,日志打印,方法前拦截 和 方法执行后拦截
[appconfig](https://github.com/J2W/J2WStructure/wiki/SharedPreference%EF%BC%9AJ2WProperties) | 替代sharedpreferences  文件采用 Properties 存储
[contact] | 通讯录的增删改查 持续更新
[download](https://github.com/J2W/J2WStructure/wiki/HTTP%EF%BC%9A-Download) | 文件下载和上传
[file](https://github.com/J2W/J2WStructure/wiki/Cache-:-J2WFileCacheManage) |  文件存储 支持 map, listmap, object, listobject
[log](https://github.com/JakeWharton/timber) | 修改调用 L.i() 直接打印
[screen] | activity 自动管理堆栈

插件/工具 | 简述
-------- | --------
[Genymotion](https://www.genymotion.com/#!/download) | 强大好用高性能的 Android 模拟器, 自带的那个模拟器简直不忍直视, 启动超慢的, 跟这个没法比, 无论你是用 Eclipse 还是 Android Studio 里面都有 Genymotion 插件
[jadx](https://github.com/skylot/jadx) | 一个 Android 反编译神器, 不同于常见的 [dex2jar](https://github.com/pxb1988/dex2jar), 这个反编译器生成代码的 try/catch 次数更少, View也不再是数字 id 了, 可读性更高
[android-selector](https://github.com/importre/android-selector-intellij-plugin) | 可以根据指定颜色生成 Selector Drawable 的插件
[GradleDependenciesHelperPlugin](https://github.com/ligi/GradleDependenciesHelperPlugin) | Gradle 依赖自动补全插件
[Smali Viewer](http://blog.avlyun.com/show/%E3%80%8Asv%E7%94%A8%E6%88%B7%E6%8C%87%E5%8D%97%E3%80%8B/) | sv 是一款免费 APK 分析软件, 对你感兴趣的 APP 分析看看它们都用了些什么, 对你学习借鉴有一定帮助
[folding-plugin](https://github.com/dmytrodanylyk/folding-plugin) | layout 文件夹里的布局文件一般很长很长没有目录, 这个插件就可以把 layout 分好层级结构, 类似 src 下目录结构
[CodeGlance](https://github.com/Vektah/CodeGlance) | 在编辑代码最右侧, 显示一块代码小地图
[Statistic](https://plugins.jetbrains.com/plugin/?idea&id=4509) | 统计整个项目行数的插件, 这可表示我们日日夜夜辛辛苦苦必备插件
[idea-markdown](https://github.com/nicoulaj/idea-markdown) | 在 AS 中编写 Markdown 文件, 可以直接预览网页显示效果, 对于你经常编写 Markdown 文件的你值得拥有这个插件
[Stetho](http://facebook.github.io/stetho/) | Stetho 是 Facebook 出品的一个强大的 Android 调试工具,使用该工具你可以在 Chrome Developer Tools 查看 App 的布局, 网络请求(仅限使用 Volley, okhttp 的网络请求库), sqlite, preference, 一切都是可视化的操作,无须自己在去使用 adb, 也不需要 root 你的设备
[ClassyShark](https://github.com/google/android-classyshark) | ClassyShark 是 Google 发布的一款可以查看 Android 可执行文件的浏览工具, 支持.dex, .aar, .so, .apk, .jar, .class, .xml 等文件格式, 分析里面的内容包括 classes.dex 文件, 包方法数量, 类, 字符串, 使用的 NativeLibrary 等
[AndroidWiFiADB](https://github.com/pedrovgs/AndroidWiFiADB) | 使用 WiFi 连接而不需要 USB 连接 Android 设备达到安装, 运行, 调试应用的目的
[checkstyle-idea](https://github.com/jshiell/checkstyle-idea) | Checkstyle-idea 是一款检查自己写的代码是否符合规范的插件, 该插件是根据 checkstyle.xml 文件来检查的, checkstyle.xml 文件可以由自己自己定义, 也可以使用一些大公司定义的规范, 如果不懂得如何定义, 可以查看 [官方文档](http://checkstyle.sourceforge.net/checks.html), 该插件的详细介绍以及使用, 可以看一下咕咚大侠写的 [文章](http://gudong.name/2016/04/07/checkstyle.html)
[ECTranslation](https://github.com/Skykai521/ECTranslation) | ECTranslation 是 Android Studio 开发工具的一个翻译插件, 可以将英文翻译为中文, 英语基础差的童鞋装上它就可以轻松阅读 Android 源码啦
[Android Methods Count](https://plugins.jetbrains.com/plugin/8076?pr=androidstudio) | 统计 Android 依赖库中方法的总个数, 避免应用方法数超过 65K 问题
[AndroidLocalizationer](https://github.com/westlinkin/AndroidLocalizationer) | 可用于将项目中的 string 资源自动翻译为其他语言的插件, 其翻译支持使用 Microsoft Translator 或 Google Translation
[ButterKnife Zelezny](https://github.com/avast/android-butterknife-zelezny) | ButterKnife 生成器, 使用起来非常简单方便, 为你简写了很多 findViewId 代码, 如果你不了解 ButterKnife 的可以到[这里](http://stormzhang.com/openandroid/android/2014/01/12/android-butterknife/)看看
[SelectorChapek](https://github.com/inmite/android-selector-chapek) | 设计师给我们提供好了各种资源, 每个按钮都要写一个selector是不是很麻烦? 这么这个插件就为解决这个问题而生, 你只需要做的是告诉设计师们按照规范命名就好了, 其他一键搞定
[GsonFormat](https://github.com/zzz40500/GsonFormat) | 现在大多数服务端 api 都以 json 数据格式返回, 而客户端需要根据 api 接口生成相应的实体类, 这个插件把这个过程自动化了, 赶紧使用起来吧
[ParcelableGenerator](https://github.com/mcharmas/android-parcelable-intellij-plugin) | Android中的序列化有两种方式, 分别是实现 Serializable 接口和 Parcelable 接口, 但在 Android 中是推荐使用 Parcelable, 只不过我们这种方式要比Serializable方式要繁琐, 那么有了这个插件一切就ok了
[LeakCanary](https://github.com/square/leakcanary) | 良心企业 Square 最近刚开源的一个非常有用的工具, 强烈推荐, 帮助你在开发阶段方便的检测出内存泄露的问题, 使用起来更简单方便, 而且我们团队第一时间使用帮助我们发现了不少问题, 英文不好的这里有雷锋同志翻译的中文版 [LeakCanary 中文使用说明](http://www.liaohuqiu.net/cn/posts/leak-canary-read-me/)