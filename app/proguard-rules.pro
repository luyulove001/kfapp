# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/xnnzone/Desktop/android/sdk/tools/proguard/proguard-android.txt
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

-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-printmapping priguardMapping.txt
-optimizations !code/simplification/artithmetic,!field/*,!class/merging/*
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-keepclasseswithmembernames class * {
    native <methods>;
}
-keep class com.xxl.kfapp.** { *; }
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.app.Fragment
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.annotation.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.**

-keep public class com.null.test.ui.fragment.** {*;}

-keepclassmembers class * extends android.app.Activity {
    public void * (android.view.View);
}

-ignorewarnings
-dontwarn android.net.SSLCertificateSocketFactory
#jPush
-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }
-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }

#wx pay
-keep class com.tencent.mm.opensdk.** {*;}
-keep class com.tencent.wxop.** {*;}
-keep class com.tencent.mm.sdk.** {*;}

#ali pay
-dontwarn com.alipay.**
-keep class com.alipay.** { *;}
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}

####################Butter Knife#####################
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

-dontwarn
-keep class talex.zsw.baselibrary.xx{ *;}
# 保留自定义控件(继承自View)不能被混淆
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(***);
    *** get* ();
}

# 保留Parcelable序列化的类不能被混淆
-keep class * implements android.os.Parcelable{
    public static final android.os.Parcelable$Creator *;
}

# 保留Serializable 序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
   static final long serialVersionUID;
   private static final java.io.ObjectStreamField[] serialPersistentFields;
   !static !transient <fields>;
   private void writeObject(java.io.ObjectOutputStream);
   private void readObject(java.io.ObjectInputStream);
   java.lang.Object writeReplace();
   java.lang.Object readResolve();
}

# 对R文件下的所有类及其方法，都不能被混淆
-keepclassmembers class **.R$* {
    *;
}
# 保留实体类和成员不被混淆
-keep class com.null.test.entities.** {
    public void set*(***);
    public *** get*();
    public *** is*();
}
# 对WebView的处理
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String);
}
# 对JavaScript的处理
-keepclassmembers class com.null.test.MainActivity$JSInterfacel {
    <methods>;
}
####################baseLibrary#####################
-keep class talex.zsw.baselibrary.** {*;}
-dontwarn talex.zsw.baselibrary.**
-keep class talex.zsw.baselibrary.view.** {*;}
-dontwarn talex.zsw.baselibrary.view.**
-keep class com.sendinfo.travelconverge.database.** {*;}
-dontwarn com.sendinfo.travelconverge.database.**
-keep class com.sendinfo.travelconverge.entitys.** {*;}
-dontwarn com.sendinfo.travelconverge.entitys.**
-keep class com.sendinfo.travelconverge.mpush.** {*;}
-dontwarn com.sendinfo.travelconverge.mpush.**
-keep class com.sendinfo.travelconverge.test.** {*;}
-dontwarn com.sendinfo.travelconverge.test.**
-keep class com.sendinfo.travelconverge.widget.** {*;}
-dontwarn com.sendinfo.travelconverge.widget.**
-keep class com.sendinfo.travelconverge.update.** {*;}
-dontwarn com.sendinfo.travelconverge.update.**
-keep class com.sendinfo.travelconverge.util.** {*;}
-dontwarn com.sendinfo.travelconverge.util.**
-keep class com.sendinfo.travelconverge.zxing.** {*;}
-dontwarn com.sendinfo.travelconverge.zxing.**
################gson##################
-keep class com.google.gson.** {*;}
-keep class com.google.**{*;}
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.google.** {
    <fields>;
    <methods>;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-dontwarn com.google.gson.**
-dontwarn com.google.**
####################zxing#####################
-keep class com.google.zxing.** {*;}
-dontwarn com.google.zxing.**
####################OkHttp3#####################
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**
-keep class okio.**{*;}

#okgo
-dontwarn com.lzy.okgo.**
-keep class com.lzy.okgo.**{*;}

#okrx
-dontwarn com.lzy.okrx.**
-keep class com.lzy.okrx.**{*;}

#okserver
-dontwarn com.lzy.okserver.**
-keep class com.lzy.okserver.**{*;}

####################OkHttp3#####################
-dontwarn com.squareup.okhttp3.**
-dontwarn okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**
-keep class okio.**{*;}

#okgo
-dontwarn com.lzy.okgo.**
-keep class com.lzy.okgo.**{*;}

#okrx
-dontwarn com.lzy.okrx.**
-keep class com.lzy.okrx.**{*;}

#okserver
-dontwarn com.lzy.okserver.**
-keep class com.lzy.okserver.**{*;}

-dontwarn org.dom4j.**
-dontwarn com.thoughtworks.**
-dontwarn org.simpleframework.**


-keep class com.bumptech.glide.**{*;}