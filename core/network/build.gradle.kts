import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("plugin.serialization") version "1.9.23"
}

android {
    namespace = "com.tek.network"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        val properties = Properties().apply {
            load(rootProject.file("local.properties").reader())
        }

        buildConfigField(
            type = "String",
            name = "BIN_CHECKER_API_KEY",
            value = properties["bin-checker-api-key"].toString()
        )

    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(libs.retrofit)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.okhttp)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}