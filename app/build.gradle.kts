import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.google.gms.google-services")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.tek.holvi"
    compileSdk = 34


    defaultConfig {
        applicationId = "com.tek.holvi"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.tek.holvi.TestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }


    }
    signingConfigs {
        val properties = Properties().apply {
            load(rootProject.file("local.properties").reader())
        }
        create("release") {
            storeFile = file(properties["key.dir"].toString())
            storePassword = properties["key.storePassword"].toString()
            keyAlias = properties["key.keyAlias"].toString()
            keyPassword = properties["key.keyPassword"].toString()
        }
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            manifestPlaceholders["app_name"] = "Holvi"
            signingConfig = signingConfigs.getByName("release")
        }

        named("debug") {
            applicationIdSuffix = ".debug"
            manifestPlaceholders["app_name"] = "Holvi-Debug"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlinCompiler.get()
    }
    androidComponents {
        onVariants(selector().withBuildType("release")) {
            it.packaging.resources.excludes.add("/META-INF/*")
        }
    }
}

dependencies {
    debugImplementation(libs.androidx.compose.ui.tooling)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.firebase.messaging.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.biometric.ktx)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.serialization.json)
    implementation(project(":core:database"))
    implementation(project(":core:ui"))
    implementation(project(":core:test"))
    implementation(project(":core:util"))
    implementation(project(":core:network"))
    implementation(project(":feature:password"))
    implementation(project(":feature:card"))

    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.mockito.android)
    androidTestImplementation(libs.androidx.runner)

    testImplementation(libs.koin.test)
    testImplementation(libs.mockito.core)
    testImplementation(libs.truth)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)

    implementation(libs.androidx.material3.android)

}