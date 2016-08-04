# SKYStructure
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

    #sky

    -keep class jc.sky.** { * ; }

    #butterknife 8.1

    # Retain generated class which implement ViewBinder.
    -keep public class * implements butterknife.internal.ViewBinder { public <init>(); }

    # Prevent obfuscation of types which use ButterKnife annotations since the simple name
    # is used to reflectively look up the generated ViewBinder.
    -keep class butterknife.*
    -keepclasseswithmembernames class * { @butterknife.* <methods>; }
    -keepclasseswithmembernames class * { @butterknife.* <fields>; }

    #nineoldandroids

    -libraryjars /libs/nineoldandroids-2.4.0.jar
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

    -keepattributes *Annotation*
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

    #commons-lang3

    #commons-io