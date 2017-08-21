# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Users\Administrator\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
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

-dontskipnonpubliclibraryclasses
-keepattributes Signature
-keepattributes EnclosingMethod
-keepattributes *Annotation*

-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose
-ignorewarnings

#将你不需要混淆的部分申明进来，因为有些类经过混淆会导致程序编译不通过
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.**
-keep public class com.android.vending.licensing.ILicensingService
-keep public class com.sxbb.utils.scanner.common.Runnable

-keep class com.lovelilu.model.** {*;}
-keep public class [com.lovelilu].R$*{
public static final int *;
}


## ----------------------------------
##      okhttp框架的混淆
## ----------------------------------
-dontwarn com.squareup.okhttp.internal.http.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-keepnames class com.levelup.http.okhttp.** { *; }
-keepnames interface com.levelup.http.okhttp.** { *; }
-keepnames class com.squareup.okhttp.** { *; }
-keepnames interface com.squareup.okhttp.** { *; }
-keepnames class com.squareup.okhttp.** { *; }
-keep class com.sxbb.utils.OkHttpClientManager {*;}
-keep class com.sxbb.utils.OkHttpClientManager$ResultCallback{*;}
-dontwarn okio.**


## ----------------------------------
##      EventBus
## ----------------------------------
-keepclassmembers class ** {
    public void onEvent*(**);
}
-keepclassmembers class ** {
public void onEventMainThread(**);
}

## ----------------------------------
##      Native
## ----------------------------------
-keepclasseswithmembernames class * {
    native <methods>;
}

