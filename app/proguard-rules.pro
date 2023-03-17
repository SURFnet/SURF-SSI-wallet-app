# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Applications/Android Studio.app/sdk/tools/proguard/proguard-android.txt
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

-allowaccessmodification

-dontwarn javax.annotation.**

-keepclassmembers,allowobfuscation class * {
    @javax.inject.* *;
    @dagger.* *;
    <init>();
}

# Dagger

-keepattributes *Annotation*

-keepattributes EnclosingMethod

-keep class **$$ModuleAdapter
-keep class **$$InjectAdapter
-keep class **$$StaticInjection

-keep class dagger.* { *; }

-dontwarn com.google.errorprone.annotations.**

-keep class javax.inject.* { *; }
-keep class * extends dagger.internal.Binding
-keep class * extends dagger.internal.ModuleAdapter
-keep class * extends dagger.internal.StaticInjection

-keepnames class dagger.Lazy

-keep class !nl.eduid.wallet.**  { *; }
-keepclasseswithmembernames,includedescriptorclasses class * {
    native <methods>;
}

-dontwarn javax.**
-dontwarn lombok.**
-dontwarn org.apache.**
-dontwarn com.squareup.**
-dontwarn com.sun.**
-dontwarn **handmark**
-dontwarn androidx.test.**
-dontwarn org.junit.**
-dontwarn kotlin.**
-dontwarn org.xmlpull.v1.**

# Retrofit
-dontnote retrofit2.Platform
# Retrofit does reflection on generic parameters and InnerClass is required to use Signature.
-keepattributes Signature, InnerClasses
-keepattributes Exceptions

# Okio
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn org.conscrypt.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform

# GMS
-keep public class com.google.android.gms.* { public *; }
-dontwarn com.google.android.gms.**

# Many libraries use Animal Sniffer to ensure they're API compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# Kotlin serialization generates sibling serializer classes which are looked up reflectively.
-keep class **.*$serializer { *; }

# Data binding
-dontwarn androidx.databinding.**
-keep class androidx.databinding.** { *; }

# Data
-dontwarn nl.eduid.wallet.data.**
-keep class nl.eduid.wallet.** { *; }

# Network
-dontwarn nl.eduid.wallet.**
-keep class nl.eduid.wallet.** { *; }

-keep class nl.eduid.wallet.inject.config.** { *; }

# Kotlin
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# These classes are used via kotlin reflection and the keep might not be required anymore once Proguard supports
# Kotlin reflection directly.
-keep class kotlin.reflect.jvm.internal.impl.builtins.BuiltInsLoaderImpl
-keep class kotlin.reflect.jvm.internal.impl.serialization.deserialization.builtins.BuiltInsLoaderImpl
-keep class kotlin.reflect.jvm.internal.impl.load.java.FieldOverridabilityCondition
-keep class kotlin.reflect.jvm.internal.impl.load.java.ErasedOverridabilityCondition
-keep class kotlin.reflect.jvm.internal.impl.load.java.JavaIncompatibilityRulesOverridabilityCondition

# If Companion objects are instantiated via Kotlin reflection and they extend/implement a class that Proguard
# would have removed or inlined we run into trouble as the inheritance is still in the Metadata annotation
# read by Kotlin reflection.
# FIXME Remove if Kotlin reflection is supported by Pro/Dexguard
-if class **$Companion extends **
-keep class <2>
-if class **$Companion implements **
-keep class <2>

# https://medium.com/@AthorNZ/kotlin-metadata-jackson-and-proguard-f64f51e5ed32
-keep class kotlin.Metadata { *; }

# https://stackoverflow.com/questions/33547643/how-to-use-kotlin-with-proguard
-dontwarn kotlin.**

-keep class nl.eduid.wallet.**Starter** { *; }

# Facebook Conceal config, which is used by Slink
-keep class com.facebook.crypto.** { *; }
-keep class com.facebook.jni.** { *; }
-keepclassmembers class com.facebook.cipher.jni.** { *; }

# Moshi

# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**

-keepclasseswithmembers class * {
    @com.squareup.moshi.* <methods>;
}

-keep @com.squareup.moshi.JsonQualifier interface *

# Enum field names are used by the integrated EnumJsonAdapter.
# Annotate enums with @JsonClass(generateAdapter = false) to use them with Moshi.
-keepclassmembers @com.squareup.moshi.JsonClass class * extends java.lang.Enum {
    <fields>;
}

# The name of @JsonClass types is used to look up the generated adapter.
-keepnames @com.squareup.moshi.JsonClass class *

# Retain generated JsonAdapters if annotated type is retained.
-if @com.squareup.moshi.JsonClass class *
-keep class <1>JsonAdapter {
    <init>(...);
    <fields>;
}
-if @com.squareup.moshi.JsonClass class **$*
-keep class <1>_<2>JsonAdapter {
    <init>(...);
    <fields>;
}
-if @com.squareup.moshi.JsonClass class **$*$*
-keep class <1>_<2>_<3>JsonAdapter {
    <init>(...);
    <fields>;
}
-if @com.squareup.moshi.JsonClass class **$*$*$*
-keep class <1>_<2>_<3>_<4>JsonAdapter {
    <init>(...);
    <fields>;
}
-if @com.squareup.moshi.JsonClass class **$*$*$*$*
-keep class <1>_<2>_<3>_<4>_<5>JsonAdapter {
    <init>(...);
    <fields>;
}
-if @com.squareup.moshi.JsonClass class **$*$*$*$*$*
-keep class <1>_<2>_<3>_<4>_<5>_<6>JsonAdapter {
    <init>(...);
    <fields>;
}

-keep class kotlin.reflect.jvm.internal.** { *; }

-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# Firebase Crashlytics

-keepattributes SourceFile,LineNumberTable        # Keep file names and line numbers.
-keep public class * extends java.lang.Exception  # Optional: Keep custom exceptions.

-keep class com.google.firebase.crashlytics.** { *; }
-dontwarn com.google.firebase.crashlytics.**
