buildscript {

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(libs.android.pluginGradle)
        classpath(libs.dagger.hilt.pluginGradle)
        classpath(libs.kotlin.pluginGradle)
        classpath(libs.junitPlatform.pluginGradle)
        classpath(libs.androidJunit.pluginGradle)
        classpath(libs.java.poet)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://s3.amazonaws.com/repo.commonsware.com")
    }
}

subprojects {
    // See GitHub ticket for more details: https://github.com/gradle/kotlin-dsl-samples/issues/607#issuecomment-375687119
    parent!!.path.takeIf { it != rootProject.path }?.let { evaluationDependsOn(it) }
}
