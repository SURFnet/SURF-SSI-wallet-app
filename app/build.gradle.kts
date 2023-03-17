@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    kotlin("plugin.serialization") version "1.7.20"
}

android {

    defaultConfig {

        compileSdk = 33

        defaultConfig {
            minSdk = 23
            targetSdk = 33
        }

        compileOptions {
            isCoreLibraryDesugaringEnabled = true
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }

        applicationId = "nl.eduid.wallet"

        versionCode = 22
        versionName = "1.0.0"

        testInstrumentationRunner = "nl.eduid.wallet.SkeletonTestRunner"

        testHandleProfiling = true
        testFunctionalTest = true

        vectorDrawables.useSupportLibrary = true

        setProperty(
            "archivesBaseName",
            "eduwallet"
        ) // Enable again when using new DeployPlus

        resourceConfigurations.addAll(listOf("en", "nl"))

    }

    // Speeds up local development builds
    if (project.hasProperty("devBuild")) {
        splits.abi.isEnable = false
        splits.density.isEnable = false
        androidResources.noCompress("")
    }

    buildFeatures {
        buildConfig = true
        compose = true
        dataBinding = false
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    signingConfigs {

        getByName("debug") {
            enableV1Signing = true
        }

        create("default") {

            storeFile = file("keystore/surf_eduid_wallet_poc_keystore.jks")
            storePassword = System.getenv("KEYSTORE_PASSWORD")

            keyAlias = "surf_eduid_wallet_poc_key0"
            keyPassword = System.getenv("KEYSTORE_PASSWORD")

            enableV1Signing = true
        }
    }

    buildTypes {
        getByName("debug") {

            isDebuggable = true
            isMinifyEnabled = false

            (this as ExtensionAware).extra["alwaysUpdateBuildId"] = false

            matchingFallbacks.add("release")

            println("is signing v1 enabled for debug? -> ${signingConfig?.enableV1Signing}")
        }

        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            signingConfig = signingConfigs.getByName("default")
        }
    }

    configurations {

        testImplementation {
            exclude("org.jetbrains.kotlin", "kotlin-test-junit")
        }

        all {
            resolutionStrategy {
                val get = libs.androidx.room.runtime.get()
                force("${get.module.group}:${get.module.name}:${get.versionConstraint.requiredVersion}")
                val get1 = libs.androidx.fragment.get()
                force("${get1.module.group}:${get1.module.name}:${get1.versionConstraint.requiredVersion}")
            }
        }
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()

        // Enable experimental coroutines APIs, including Flow
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-Xopt-in=kotlinx.coroutines.FlowPreview",
        )
    }

    kapt {
        correctErrorTypes = true
        useBuildCache = true
    }

}

// App dependencies
dependencies {

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))
    
    implementation(libs.kotlin.stdlib)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

    implementation(libs.androidx.room.common)
    implementation(libs.kotlin.reflect)
    implementation(libs.mobilization.logging)
    implementation(libs.mobilization.loggingApi)

    "coreLibraryDesugaring"("com.android.tools:desugar_jdk_libs:2.0.2")
    implementation("com.google.accompanist:accompanist-drawablepainter:0.24.7-alpha")
    implementation(libs.accompanist.permissions)

    api(libs.bundles.androidxDataStoreLib)
    api(libs.google.tink)
    api(libs.datastore.encrypted)

    api(libs.androidx.room.ktx)
    api("org.threeten:threetenbp:${libs.versions.threetenbp.get()}:no-tzdb")
    api(libs.kotlin.datetime)
    api(libs.kotlinxSerializationJson)

    kapt(libs.dagger.compiler)

    api(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)

    implementation(libs.saferoom)

    implementation(libs.dagger.hilt.base)
    kapt(libs.dagger.hilt.compiler)

    api(libs.bundles.ktor)

    api(libs.timber)
    api(libs.timberkt)

    implementation(libs.bundles.coroutines)
    implementation(libs.bundles.google)

    compileOnly(libs.androidx.annotation)
    implementation(libs.bundles.androidx)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.androidx.compose.uiToolingPreview)
    debugImplementation(libs.androidx.compose.uiTooling)

    implementation(libs.coil)

    implementation(libs.androidx.compose.navigation)
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.navigationFragment)
    implementation(libs.androidx.work.runtimeKtx)
    implementation(libs.dagger.dagger)
    implementation(libs.dagger.hilt.base)
    implementation(libs.dagger.androidSupport)
    implementation(libs.timber)
    implementation(libs.timberkt)
    implementation(libs.threeTen.abp)
    implementation(libs.kotlin.datetime)
    implementation(libs.bundles.mavericks)
    implementation(libs.bundles.accompanist)
    implementation(libs.bundles.aboutLibraries)

    implementation(libs.bundles.androidxDataStoreLib)
    implementation(libs.google.tink)
    implementation(libs.datastore.encrypted)

    kapt(libs.androidx.lifecycle.compiler)
    kapt(libs.dagger.compiler)
    kapt(libs.dagger.hilt.compiler)
    kapt(libs.dagger.androidProcessor)

    implementation(libs.processPhoenix)

    compileOnly(libs.javaxannotation)

    implementation(libs.bundles.accompanist)
    implementation(libs.bundles.mavericks)
    implementation(libs.bundles.navigationFragment)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.androidx.compose.uiToolingPreview)
    debugImplementation(libs.androidx.compose.uiTooling)

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-guava:1.6.0-RC")

    implementation("com.google.mlkit:barcode-scanning:17.0.2")

    // CameraX core library using the camera2 implementation
    val camerax_version = "1.3.0-alpha01"
    // The following line is optional, as the core library is included indirectly by camera-camera2
    implementation("androidx.camera:camera-core:${camerax_version}")
    implementation("androidx.camera:camera-camera2:${camerax_version}")
    // If you want to additionally use the CameraX Lifecycle library
    implementation("androidx.camera:camera-lifecycle:${camerax_version}")
    // If you want to additionally use the CameraX View class
    implementation("androidx.camera:camera-view:${camerax_version}")
    // If you want to additionally add CameraX ML Kit Vision Integration
    implementation("androidx.camera:camera-mlkit-vision:${camerax_version}")
    // If you want to additionally use the CameraX Extensions library
    implementation("androidx.camera:camera-extensions:${camerax_version}")

    implementation(libs.dagger.hilt.compose)
    implementation(libs.dagger.hilt.base)
    kapt(libs.dagger.hilt.compiler)
}

// Test dependencies
dependencies {
    testImplementation(libs.kotlin.stdlib)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.testJunit)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.dagger.hilt.test)

    testImplementation(libs.okhttpMockwebserver)
    testImplementation(libs.mockk)

    testImplementation(libs.junit.junit)
    testImplementation(libs.junit.jupiterApi)
    testRuntimeOnly(libs.junit.jupiterEngine)
    testRuntimeOnly(libs.junit.vintageEngine)
    testImplementation(libs.junit.jupiterParams)
}

// Android test dependencies
dependencies {
    androidTestImplementation(libs.androidx.compose.uiTest)
    androidTestImplementation(libs.kotlin.reflect)
    androidTestImplementation(libs.dagger.hilt.test)
    androidTestImplementation(libs.bundles.androidTestDependencies)
    kaptAndroidTest(libs.dagger.hilt.compiler)
}
