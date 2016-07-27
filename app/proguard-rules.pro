# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\wm\programs\sdk\androidsdk/tools/proguard/proguard-android.txt
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
# To enable ProGuard in your project, edit project.properties
# to define the proguard.config property as described in that file.
#
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ${sdk.dir}/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the ProGuard
# include property in project.properties.
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

# 申明-不用修改
-optimizationpasses 9
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

# 常用混淆-不用修改
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.**
-keep public class com.android.vending.licensing.ILicensingService
#-keep class com.google.gson.stream.** { *; }
#-keep class com.google.gson.examples.android.model.** { *; }
-keep public class javax.**
-keep public class android.webkit.**
-dontwarn android.support.**
-keep class android.support.** { *; }
-dontshrink
-dontoptimize

-dontwarn com.android.**
-keep class org.android.**{*;}

-keep public class android.net.http.SslError
-keep public class android.webkit.WebViewClient
-dontwarn android.webkit.WebView
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient

# webview + js - 可修改
# keep 使用 webview 的类
#-keepclassmembers class com.easyparking.ui.activity.WebActivity {
#   public *;
#}
# keep 使用 webview 的类的所有的内部类
#-keepclassmembers class com.easyparking.ui.activity.WebActivity$*{
#    *;
#}

# 保持 native 方法不被混淆-不用修改
-keepclasseswithmembernames class * {
    native <methods>;
}

# 保持自定义控件类不被混淆 -不用修改
-keepclasseswithmembers class * {
    public <init>(android.content.Context);
}
-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 保持枚举 enum 类不被混淆-不用修改
-keepclassmembers enum * { *; }


# 保持 Parcelable不被混淆-不用修改
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# 保持 R文件不被混淆-可修改
-keepclassmembers class **.R$* { #不混淆R文件
    public static <fields>;
}





#umeng
-dontwarn com.umeng.**
-keep class com.umeng.** { *; }
-keep class com.umeng.analytics.** { *; }
-keep class com.umeng.common.** { *; }
-keep class com.umeng.newxp.** { *; }
-keep public class com.umeng.socialize.* {*;}
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**
-keep class com.umeng.socialize.sensor.**
-keep class com.umeng.scrshot.**
-keepclassmembers class * {
	public <init>(org.json.JSONObject);
}

#eventbus
-dontwarn de.greenrobot.**
-keep class de.greenrobot.** {*; }
-keepclassmembers class ** {
    public void onEventMainThread(**);
}

# 过滤模型 -可修改
-keep class com.jingantech.iam.mfa.android.app.model.** { *; }

-dontwarn com.viewpagerindicator.LinePageIndicator

#rest
-dontwarn android.net.**
-keep class android.net.**{*;}
-dontwarn com.android.**
-keep class com.android.**{*;}
-dontwarn org.springframework.**
-keep class org.springframework.**{*;}

#commons
-dontwarn org.apache.**
-keep class org.apache.**{*;}

#ormlite
-dontwarn com.j256.ormlite.**
-keep class com.j256.ormlite.**{*;}

-dontwarn com.google.**
-keep class com.google.** {*; }

-dontwarn org.spongycastle.**
-keep class org.spongycastle.** {*; }

