# SKY
接受不完美的自己，享受不断完善的自己 我们的承诺是，每天都要有进步

[![Maven Central][mavenbadge-svg]][mavenbadge]

[SKY 版本说明](https://github.com/skyJinc/sky/wiki)
-----------------------------------
3.1.0-SNAPSHOT

 - 支持kotlin 前提是 类和方法都加上open
 - 支持父类方法的调用
 - 增加display module 组件化跳转
 - 调整结构
<br />

3.0.0

 - 新增helper.ui(class)  直接获取activity,fragment 来执行方法, 无需判定是否在子线程和是否为空
 - 新增 cglib for android 动态代理，无需在声明接口，可直接使用，详情可看 [sample demo](https://github.com/skyJinc/sky/tree/master/sample/src/main/java/com/example/sky)<br />
 - 新建插件 快速生成
 
 Gradle maven 中央库
 ----------------------
 
  - [WIKI](https://github.com/skyJinc/sky/wiki/3.1.0-gradle_init)
  
 App-build.gradle:
 
      dependencies {
         api 'com.jincanshen:sky:3.1.0-SNAPSHOT' //SKY架构
         api 'com.jincanshen:sky-aac:3.1.0-SNAPSHOT' // AAC 架构 
         annotationProcessor 'com.jincanshen:sky-compiler:3.1.0-SNAPSHOT'
      }
 
[SKY-AAC 版本说明](https://github.com/skyJinc/sky/wiki)
-----------------------------------
3.1.0-SNAPSHOT

 - 新结构正在设计

 Gradle maven 中央库
 ----------------------
 
  - [WIKI](https://github.com/skyJinc/sky/wiki/3.1.0-gradle_init)
  
 App-build.gradle:
 
      dependencies {
         api 'com.jincanshen:sky-aac:3.1.0-SNAPSHOT' // AAC 
         annotationProcessor 'com.jincanshen:sky-compiler:3.1.0-SNAPSHOT'
      }
 
 
[SKY-DI 注入架构 版本说明](https://github.com/skyJinc/sky/wiki/DI%E6%B3%A8%E5%85%A5%E6%A1%86%E6%9E%B6)
-----------------------------------
3.1.0-SNAPSHOT

 - 支持动态注入
 - 简化注入操作
 - 支持library注入
 - 支持继承注入

 Gradle maven 中央库
 ----------------------
 
  - [WIKI](https://github.com/skyJinc/sky/wiki/3.1.0-gradle_init)
  
 App-build.gradle:
 
      dependencies {
         api 'com.jincanshen:sky-di:3.1.0-SNAPSHOT' 
         annotationProcessor 'com.jincanshen:sky-compiler:3.1.0-SNAPSHOT'
      }
 

Gradle 版本
-----------------------------------
1.插件 com.android.tools.build:gradle:3.0.1'<br />
2.版本 - gradle-4.+-all.zip<br />



帮助
-----------------------

如果需要帮助，请给我提问题, [open a new issue on Github](https://github.com/skyJinc/sky/issues/new).

测试和检测
-----------------------
* Unit Testing: [JUnit4](https://github.com/junit-team/junit4) +
 [Mockito](https://github.com/mockito/mockito) +
 [Hamcrest](https://github.com/hamcrest/JavaHamcrest)
* Developer Debugging Tools: [Stetho](https://github.com/facebook/stetho) +
 [LeakCanary](https://github.com/square/leakcanary)


插件/工具 | 简述
-------- | --------
[sky plugin](https://github.com/skyJinc/SkyPlugin) | 快速创建view和biz,gson转换,注解findview 等功能...
[sky gradle plugin](https://github.com/skyJinc/SkyGradlePlugIn) | 组件化插件
[folding-plugin](https://github.com/dmytrodanylyk/folding-plugin) | layout 文件夹里的布局文件一般很长很长没有目录, 这个插件就可以把 layout 分好层级结构, 类似 src 下目录结构
[AndroidLocalizationer](https://github.com/westlinkin/AndroidLocalizationer) | 可用于将项目中的 string 资源自动翻译为其他语言的插件, 其翻译支持使用 Microsoft Translator 或 Google Translation
[ParcelableGenerator](https://github.com/mcharmas/android-parcelable-intellij-plugin) | Android中的序列化有两种方式, 分别是实现 Serializable 接口和 Parcelable 接口, 但在 Android 中是推荐使用 Parcelable, 只不过我们这种方式要比Serializable方式要繁琐, 那么有了这个插件一切就ok了


[mavenbadge-svg]: https://maven-badges.herokuapp.com/maven-central/com.jincanshen/sky/badge.svg
[mavenbadge]: https://maven-badges.herokuapp.com/maven-central/com.jincanshen/sky
