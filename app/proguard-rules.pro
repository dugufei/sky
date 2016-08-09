    #sky--------------------------------------------------------------------------------------------------------
    -keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
    -keep class jc.sky.** { *; }

    # Some methods are only called from tests, so make sure the shrinker keeps them.
    -keep class android.support.v4.widget.DrawerLayout { *; }
    -keep class android.support.test.espresso.IdlingResource { *; }
    -keep class com.google.common.base.Preconditions { *; }

    # Proguard rules that are applied to your test apk/code.
    -ignorewarnings

    -keepattributes *Annotation*

    -keepclasseswithmembers class * {
           <init> ();
    }

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
    # Platform calls Class.forName on types which do not exist on Android to determine platform.
        -dontnote retrofit2.Platform
    # Platform used when running on RoboVM on iOS. Will not be used at runtime.
        -dontnote retrofit2.Platform$IOS$MainThreadExecutor
    # Platform used when running on Java 8 VMs. Will not be used at runtime.
        -dontwarn retrofit2.Platform$Java8
    # Retain generic type information for use by reflection by converters and adapters.
        -keepattributes Signature
    # Retain declared checked exceptions for use by a Proxy instance.
        -keepattributes Exceptions