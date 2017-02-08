# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /usr/local/Cellar/android-sdk/tools/proguard/proguard-android.txt
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

-dontwarn java.lang.invoke.*
-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }

-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**
-dontnote org.apache.http.**
-keep class android.net.http.** { *; }
-dontwarn android.net.http.**
-dontnote android.net.http.**

-keepattributes SourceFile,LineNumberTable
-dontwarn com.squareup.picasso.**
-keepclasseswithmembernames class * {
    native <methods>;
}

-dontwarn okio.**

-keep class mobi.kujon.network.json.** { *; }
-keep class mobi.kujon.google_drive.model.json.**{*;}

-keep class ch.qos.** { *; }
-dontwarn okhttp3.internal.Internal
-keep class org.slf4j.** { *; }
-keep class org.apache.** { *; }
-keep class com.sun.jna.** { *; }
-keep class com.papertrailapp.logback.** { *; }
-keep class org.productivity.java.syslog4j.impl.** { *; }
-dontwarn org.slf4j.**
-keepattributes *Annotation*
-dontwarn ch.qos.logback.core.net.*
-dontwarn org.apache.**
-dontwarn com.sun.jna.**
-dontwarn org.productivity.java.syslog4j.impl.**

-dontwarn retrofit.**
-dontwarn com.flurry.sdk.**
-keep class retrofit.** { *; }

-keep class okhttp3.** { *; }

-keep interface okhttp3.** { *; }

-dontwarn okhttp3.internal.huc.**

-keep class com.google.**
-dontwarn com.google.**



-dontwarn sun.misc.**

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

-dontnote rx.internal.util.PlatformDependent