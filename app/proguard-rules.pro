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
    -dontnote retrofit2.Platform
    -dontnote retrofit2.Platform$IOS$MainThreadExecutor
    -dontwarn retrofit2.Platform$Java8
    -keepattributes Signature
    -keepattributes Exceptions

    #glide
    -keep public class * implements com.bumptech.glide.module.GlideModule
    -keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
      **[] $VALUES;
      public *;
    }
    #guava
    -keep class com.google.j2objc.annotations.** { *; }
    -dontwarn   com.google.j2objc.annotations.**
    -keep class java.lang.ClassValue { *; }
    -dontwarn   java.lang.ClassValue
    -keep class org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement { *; }
    -dontwarn   org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
