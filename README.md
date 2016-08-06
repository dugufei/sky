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
[MVP](http://blog.csdn.net/lmj623565791/article/details/46596109) | 项目使用接口 来隔离 view层 和 biz 业务层 , 思想可以看blog
[http](https://github.com/square/retrofit) | 网络使用retrofit2, 初始化进行封装, 通过SKYHelpter.http 使用
[appconfig](https://github.com/J2W/J2WStructure/wiki/SharedPreference%EF%BC%9AJ2WProperties) | 替代sharedpreferences  文件采用 Properties 存储
[download](https://github.com/J2W/J2WStructure/wiki/HTTP%EF%BC%9A-Download) | 文件下载和上传
[file](https://github.com/J2W/J2WStructure/wiki/Cache-:-J2WFileCacheManage) |  文件存储 支持 map, listmap, object, listobject
[log](https://github.com/JakeWharton/timber) | 修改调用 L.i() 直接打印

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

## 博客

博客地址 | 博主信息
-------- | --------
[Android Developers Blog](http://android-developers.blogspot.com/) | Android官网博客, 在上面可以关注 Android 最新的进展与最权威的博客(需翻墙)
[stormzhang](http://stormzhang.com/) | 博主是上海薄荷科技开发主管, 他的博客分享了他从编程白痴到自学 Android 一路走过的经验, 写了一篇 [Android学习之路](http://stormzhang.com/android/2014/07/07/learn-android-from-rookie/) 帮助了无数人, 里面还有很多好的文章非常适合新手入门, 并且是微信公众号「AndroidDeveloper」的运营者, 可以算是 Android 界最有影响力的公众号之一了, 强烈推荐关注
[胡凯](http://hukai.me/) | 目前就职于腾讯上海从事 Android 开发的工作, 发起 Google Android 官方培训课程中文版翻译, 这课程是学习 Android 应用开发的绝佳资料
[Trinea](http://www.trinea.cn/) | 目前在滴滴负责 Android 客户端技术, 他是开源库项目收集达人, 你想要的开源库[上面](https://github.com/Trinea)都有, 并且发起 Android 开源项目源码解析, 在使用开源库的同时也可以知道其中原理, 大家可以去关注一下, [地址](http://p.codekk.com)
[郭霖](http://blog.csdn.net/guolin_blog) | 博主郭霖是大神, 人人都称"郭神", 是第一行代码的作者, 博主在 CSDN 上所写的文章都非常值得学习
[代码家](http://blog.daimajia.com/) | 90后 Android 大神, 称作库达人, 博主收集了很多 Android 开源库, 博主自己也做了很多开源库, 非常值得学习
[light_sky](http://www.lightskystreet.com/) | Android 开发工程师, 目前在北京工作. Google big fan, 热爱开源, 热爱分享, 活跃于 GitHub, G+, Twitter, 捕捉Android最新资讯和技术, ViewPagerIndicator 开源项目分析者
[张鸿洋](http://blog.csdn.net/lmj623565791/) | 张鴻洋是 CSDN 博客专家, 博主在 CSDN 所发布的每一篇文章都是干货, 都很值得大家去学习
[张兴业](http://blog.csdn.net/xyz_lmn) | 张兴业同样也是 CSDN 博客专家, 博主专注移动互联网开发, 关注移动支付业务
[hi大头鬼hi](http://blog.csdn.net/lzyzsd/) | hi大头鬼hi 是阿里巴巴集团的一名 Android 工程师, 擅长 Android, RxJava, RxAndroid, ReactNative, Node.js, 大前端, 可谓是全栈工程师, 如果你对 RxJava 技术感兴趣, 不妨到他的博客看看, 他写了一系列有关 RxJava 的介绍
[更多](https://github.com/android-cn/android-dev-cn) | 这里面收集了很多国内外开发者的信息, 大家可以去看看

## 开源项目学习

项目名称 | 项目简介
-------- | --------
[Google I/O 2014](https://github.com/google/iosched) | Google I/O Android App 使用了当时最新推出的 Material Design 设计
[Google play music](https://github.com/googlesamples/android-UniversalMusicPlayer) | 一个跨多个平台音乐播放器
[Google Santa Tracker for Android](https://github.com/google/santa-tracker-android?utm_source=www.race604.com) | Google 开源的一个儿童教育和娱乐的 App
[github客户端](https://github.com/pockethub/PocketHub) | 开源者 github 团队, 支持项目的 lssues 和 Gists 并集成了新闻 feed 以便及时获取来自组织好友和资料库的更新信息, 还提供了一个用于快速访问你创建,监控以及发布 issue 面板, 可查看并将问题加到收藏夹
[Talon-for-Twitter](https://github.com/klinker24/Talon-for-Twitter) | 一个完整版 Twitter 第三方客户端, 属于顶级水平, 而且在源代码 100% 开源, 学习资源让你取之不尽
[Anime Taste](https://github.com/daimajia/AnimeTaste) | 开发者是代码家为 AnimeTaste 全球动画精选开发的 Android 客户端, 国内很少见的精彩而且开源的 APP, 获得豌豆荚110期设计奖
[EverMemo](https://github.com/daimajia/EverMemo) | 开发者是代码家, EverMemo 是一款让你快速记录与分享灵感的随身便签, 极简的界面与卡片式布局, 让你记录与查找便签更有效率
[9GAG](https://github.com/stormzhang/9GAG) | 开发者是 stormzhang 博主,这个开源项目教你使用 Studio, Gradle 以及一些流行的开源库快速开发一个不错的 Android 客户端
[MIUI 便签](https://github.com/MiCode/Notes) | MiCode 便签是小米便签的社区开源版, 由 MIUI 团队（ww.miui.com）发起并贡献第一批代码, 遵循 NOTICE 文件所描述的开源协议
[贝壳单词](https://github.com/drakeet/Seashell-app) | 开发者是许晓峰(Drakeet), 获得豌豆荚设计奖, 这个是初期版本, 新版没有开源, 但是在博主的博客当中将一些新版的内容都剥离出来写成了文章,可以去学习一下
[Muzei Live Wallpaper](https://github.com/romannurik/muzei) | 开发者是 Ian Lake , 就职于 Google, 这款是定时更换桌面精美壁纸 App
[四次元-新浪微博客户端](https://github.com/qii/weiciyuan) | 一个仿新浪微博客户端 App, 基本都含有新浪微博的功能, 开源团队写了一份 wiki 文档, 可以去学习一下该 App 是如何实现的
[知乎专栏](https://github.com/bxbxbai/ZhuanLan) | 开发者是白瓦力, 项目结构清晰, 代码分包合理, 很棒. 很适合新手去学习如何搭建一个 App 结构, 熟悉开发一个完整 App 的流程
[eoe 客户端](https://github.com/eoecn/android-app) | eoe 社区Android客户端
[oschina](http://git.oschina.net/oschina/android-app) | 开源中国社区 Android 客户端, 此开源的是 v1 版本, v2 版本将在 2015 年年中开源
[v2ex](https://github.com/kyze8439690/v2ex-daily-android) | 开发者是[杨辉](http://yanghui.name/about/), 这个是 v2ex Android 第三方客户端
[Tweet Lanes](https://github.com/chrislacy/TweetLanes) | 功能比较完整的 Twitter 客户端
[Financius](https://github.com/mvarnagiris/financius-public) | 一款简单易用个人理财 Android 程序
[Coding](https://coding.net/u/coding/p/Coding-Android/git) | Coding 类似于 github 一个代码托管平台, 这个是 Coding 的 Android 版客户端
[ZXing](https://github.com/zxing/zxing) | 二维码扫描工具,市场上许多应用的二维码扫描工具都是从这个修改得到的
[photup](https://github.com/chrisbanes/photup) | 编辑机批量上传照片到 facebook 上,代码分包合理,很棒,不过这个项目依赖的开源项目比较多, 比较难编译
[todo.txt-android](https://github.com/ginatrapani/todo.txt-android) | todo.txt 官方 Android 应用, 一个极简的将待办事件记录在 .txt 文件中
[扫扫图书](https://github.com/JayFang1993/ScanBook) | 一个让你懂得如何去选择一本书的 APP, 具有扫描搜索查询图书的信息功能
[ChaseWhisplyProject](https://github.com/tvbarthel/ChaseWhisplyProject) | 开启摄像头在你所在位置寻找鬼魂, 进行打鬼游戏
[AntennaPod](https://github.com/AntennaPod/AntennaPod) | AntennaPod 是一个自由, 开源的播客客户端, 支持 Atom 和 RSS Feed
[干柴](https://github.com/openproject/AndroidDigest) | 开发者是冯建, 一个专注收集 android 相关干货(文摘,名博,github等等)资源 App
[Hacker News](https://github.com/manmal/hn-android) | 一个查看黑客新闻报道和评论的 App
[proxydroid](https://github.com/madeye/proxydroid) | ProxyDroid 是一个帮助你在你的 Android 设备上设置代理 (HTTP / SOCKS4 / SOCKS5)
[AliGesture](https://git.oschina.net/way/AliGesture) | Android平台手势识别应用, 简单的手势就可以进入预置的应用, 方便快捷
[指读](https://coding.net/u/youzi/p/Zhidu-Android/git/tree/master) | 指读顾名思义就是用手指去阅读浩瀚的书海
[KJ 音乐](https://github.com/KJFrame/KJMusic) | 开发者是[张涛](http://blog.kymjs.com/about/), 快捷音乐使用到[KJFrameForAndroid](https://github.com/kymjs/KJFrameForAndroid)开发框架, 拥有界面绚丽, 操作简单, 播放手机本地音乐, 在线收听音乐等功能, 还支持: 新浪微博, 百度账号, QQ 账号的一键登录
[哎嘛](http://git.oschina.net/tonlin/android-app) | OSCHINA 第三方客户端, 这个版本界面比官方版本界面好看多了, 使用的是 Material Design, 界面看上去非常清爽舒服
[码厩](http://git.oschina.net/wlemuel/Cotable/tree/master/) | 博客园第三方客户端, 使用到缓存技术, 当你打开一次之后的博客内容, 会自动保存到设备中, 下次打开阅读同一文章时不需要再利用网络加载, 大家如果想为自己 App 搞缓存的, 可以研究一下该 App 是如何实现的喔
[cnBeta 第三方阅读器](http://git.oschina.net/ywwxhz/cnBeta-reader) | 该 App 是 cnBeta(中文业界资讯站) 第三方阅读客户端, 界面也是使用 Material Design, 还加上一些动画效果, 让人阅读文章起来不会感觉枯燥, 值得去研究学习一下
[BlackLight](https://github.com/PaperAirplane-Dev-Team/BlackLight) | BlackLight是由纸飞机开发团队开发的一款Android上的新浪微博第三方轻量级客户端
[Actor](https://github.com/actorapp/actor-platform) | Actor(优聆) 是一款即时通讯 App, 解决网络通讯差的情况, 支持离线消息和文件存储, 自动与手机联系人建立联系等等
[Plaid](https://github.com/nickbutcher/plaid) | 一个遵循 Material Design 的一个设计新闻客户端，里面的一些完全MD的一些特效很棒，同时整个客户端的框架设计也很有参考意义
[SimplifyReader](https://github.com/SkillCollege/SimplifyReader) | 一款基于 Google Material Design 设计开发的 Android 客户端, 包括新闻简读, 图片浏览, 视频爽看, 音乐轻听以及二维码扫描五个子模块, 项目采取的是MVP架构开发
[PHPHub-Android](https://github.com/CycloneAxe/phphub-android) | PHPHub Android 客户端, 项目架构使用 nucleus 简化 MVP 架构, API 请求返回数据使用 RxJava 进行处理, 客户端使用了独特的二维码扫码登录, 有兴趣的可以去研究一下
[Leisure](https://github.com/MummyDing/Leisure) | 闲暇(Leisure)是一款集"知乎日报", "果壳科学人", "新华网新闻"以及"豆瓣图书"于一体的阅读类 Android 应用, 项目里面有多语言切换、夜间模式以及无图模式, 如果你还不知道这些技术怎么实现, 不妨参考一下这个项目
[Meizhi](https://github.com/drakeet/Meizhi) | 开发者是许晓峰(Drakeet), 该 app 是数据来自代码家干货网站 [gank.io](http://gank.io), 有很多开发者都纷纷为这网站做客户端 app, 因为代码家大神开放该网站的 Api, 更重要的是该网站每天除了有干货还有漂亮妹子看呢, 该 App 使用到的技术有 RxJava + Retrofit, 代码结构非常清晰, 值得一看的开源 App.
[Bingo](https://github.com/sfsheng0322/Bingo) | 开发者是[孙福生](http://weibo.com/u/3852192525), Bingo 是一款 IT 阅读学习类的开源软件, 作者收集一些干货学习内容, 你也可以在 App 上分享你的认为干货文章, 该项目使用动态代理 AOP 编程框架, 使开发起来更简洁, 更高效. 里面有一个功能就是多种主题切换, 感兴趣的可以看看怎么实现