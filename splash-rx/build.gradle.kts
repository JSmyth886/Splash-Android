import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    compileSdkVersion(29)
    buildToolsVersion("29.0.2")


    defaultConfig {
        minSdkVersion(23)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    testOptions {
        unitTests.all(closureOf<Test> {
            useJUnitPlatform()
            testLogging.showStackTraces = true
        } as groovy.lang.Closure<Test>)
    }

    buildTypes {

        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

}

dependencies {
    compileOnly(project(path = ":splash"))
    implementation(kotlin("stdlib-jdk8", KotlinCompilerVersion.VERSION))

    // Android
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.core:core-ktx:1.1.0")

    // rxjava
    implementation("io.reactivex.rxjava2:rxjava:2.2.16")
    implementation("io.reactivex.rxjava2:rxkotlin:2.4.0")
    implementation("com.jakewharton.rx2:replaying-share-kotlin:2.2.0")
    implementation("com.jakewharton.rxrelay2:rxrelay:2.1.1")


    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.4.2")
    testImplementation("io.mockk:mockk:1.9.3.kotlin12")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")

}
