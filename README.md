# SKY
接受不完美的自己，享受不断完善的自己 我们的承诺是，每天都要有进步

[![](https://jitpack.io/v/skyJinc/sky.svg)](https://jitpack.io/#skyJinc/sky) [![Maven Central][mavenbadge-svg]][mavenbadge]

版本说明
-----------------------------------
3.0.0-SNAPSHOT<br />
1.新增helper.ui(class)  直接获取activity,fragment 来执行方法, 无需判定是否在子线程和是否为空<br />
2.新增 cglib for android 动态代理，无需在声明接口，可直接使用，详情可看 [sample demo](https://github.com/skyJinc/sky/tree/master/sample/src/main/java/com/example/sky)<br />
3.新建插件 快速生成 biz<br />
4.如果习惯声明接口，3.0.0 新增 注解动态生成接口代码 @UI @BIZ 给方法注释，就可以生成接口~<br />

Gradle 版本
-----------------------------------
1.插件 com.android.tools.build:gradle:2.3.3'<br />
2.版本 - gradle-3.4-all.zip<br />

项目引用 - 方法1 - maven 中央库
----------------------

App-build.gradle:

     dependencies {
        compile 'com.jincanshen:sky:3.0.0-SNAPSHOT'
     }
     
项目引用 - 方法2 - JitPack
--------------------
Project-build.gradle

    allprojects {
    		repositories {
    			...
    			maven { url "https://jitpack.io" }
    		}
    	}

App-build.gradle:
 
      dependencies {
         compile 'com.github.skyJinc:sky:2.4.0'
      }   	
      
proguard

    proguardFile getDefaultProguardFile('proguard-android.txt')
    proguardFile("proguard/proguard-default.pro")
    proguardFile("proguard/proguard-sky.pro")
    proguardFile("proguard/proguard-butterknife.pro")
    proguardFile("proguard/proguard-glide.pro")
    proguardFile("proguard/proguard-gson.pro")
    proguardFile("proguard/proguard-guava.pro")
    proguardFile("proguard/proguard-nineoldandroids.pro")
    proguardFile("proguard/proguard-okhttp3.pro")
    proguardFile("proguard/proguard-support-v7.pro")
    proguardFile("proguard/proguard-okio.pro")
    proguardFile("proguard/proguard-retrofit2.pro")

插件/工具 | 简述
-------- | --------
[jadx](https://github.com/skylot/jadx) | 一个 Android 反编译神器, 不同于常见的 [dex2jar](https://github.com/pxb1988/dex2jar), 这个反编译器生成代码的 try/catch 次数更少, View也不再是数字 id 了, 可读性更高
[folding-plugin](https://github.com/dmytrodanylyk/folding-plugin) | layout 文件夹里的布局文件一般很长很长没有目录, 这个插件就可以把 layout 分好层级结构, 类似 src 下目录结构
[CodeGlance](https://github.com/Vektah/CodeGlance) | 在编辑代码最右侧, 显示一块代码小地图
[Statistic](https://plugins.jetbrains.com/plugin/?idea&id=4509) | 统计整个项目行数的插件, 这可表示我们日日夜夜辛辛苦苦必备插件
[Android Methods Count](https://plugins.jetbrains.com/plugin/8076?pr=androidstudio) | 统计 Android 依赖库中方法的总个数, 避免应用方法数超过 65K 问题
[AndroidLocalizationer](https://github.com/westlinkin/AndroidLocalizationer) | 可用于将项目中的 string 资源自动翻译为其他语言的插件, 其翻译支持使用 Microsoft Translator 或 Google Translation
[ButterKnife Zelezny](https://github.com/avast/android-butterknife-zelezny) | ButterKnife 生成器, 使用起来非常简单方便, 为你简写了很多 findViewId 代码, 如果你不了解 ButterKnife 的可以到[这里](http://stormzhang.com/openandroid/android/2014/01/12/android-butterknife/)看看
[GsonFormat](https://github.com/zzz40500/GsonFormat) | 现在大多数服务端 api 都以 json 数据格式返回, 而客户端需要根据 api 接口生成相应的实体类, 这个插件把这个过程自动化了, 赶紧使用起来吧
[ParcelableGenerator](https://github.com/mcharmas/android-parcelable-intellij-plugin) | Android中的序列化有两种方式, 分别是实现 Serializable 接口和 Parcelable 接口, 但在 Android 中是推荐使用 Parcelable, 只不过我们这种方式要比Serializable方式要繁琐, 那么有了这个插件一切就ok了
[LeakCanary](https://github.com/square/leakcanary) | 良心企业 Square 最近刚开源的一个非常有用的工具, 强烈推荐, 帮助你在开发阶段方便的检测出内存泄露的问题, 使用起来更简单方便, 而且我们团队第一时间使用帮助我们发现了不少问题, 英文不好的这里有雷锋同志翻译的中文版 [LeakCanary 中文使用说明](http://www.liaohuqiu.net/cn/posts/leak-canary-read-me/)


[mavenbadge-svg]: https://maven-badges.herokuapp.com/maven-central/com.jincanshen/sky/badge.svg
[mavenbadge]: https://maven-badges.herokuapp.com/maven-central/com.jincanshen/sky