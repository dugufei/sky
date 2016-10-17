#sky--------------------------------------------------------------------------------------------------------
-keep class jc.sky.** { *; }
-dontwarn jc.sky.**

#commons-io-1.3.2.jar
-keep public class org.apache.commons.io.** {*;}


-dontnote android.net.http.*
-dontnote org.apache.http.**

-dontnote okhttp3.**
-dontnote retrofit2.**
-dontnote butterknife.**
-dontnote org.apache.commons.io.**