# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/pengjianbo/Documents/dev/android_dev/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#--------------- BEGIN: GSON ----------
-keepattributes Signature
# For using GSON @Expose annotation
-keepattributes *Annotation*
-keepattributes EnclosingMethod
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
#--------------- END: GSON ----------

#--------------- BEGIN: okhttp ----------
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**
#--------------- END: okhttp ----------

#--------------- BEGIN: okio ----------
-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**
#--------------- END: okio ----------

#--------------- BEGIN: DbHelper ----------
-keep class * extends java.lang.annotation.Annotation { *; }
#--------------- END: DbHelper ----------

#--------------- BEGIN: 数据库模型 ----------
-keep class cn.finalteam.okhttpfinal.* {*;}
-keep class * extends cn.finalteam.okhttpfinal.dm.DownloadInfo { *; }
#请求模型
-keep class * extends cn.finalteam.okhttpfinal.ApiResponse { *; }
#--------------- END: 数据库模型 ----------

#--------------- BEGIN: butterknife ----------
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
#--------------- END: butterknife ----------