import com.saveourtool.diktat.plugin.gradle.DiktatExtension
import com.saveourtool.diktat.plugin.gradle.DiktatGradlePlugin

buildscript {

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.gradle)
        classpath(libs.google.services)
        classpath(libs.kotlin.gradle.plugin)
    }
}


plugins {
    id("com.saveourtool.diktat") version "2.0.0" apply false
}


allprojects {
    apply<DiktatGradlePlugin>()
    configure<DiktatExtension> {
        diktatConfigFile = rootProject.file("diktat-analysis.yml")
        inputs { include("src/**/*.kt") }
        debug = true
    }
}


tasks {
    register("clean", Delete::class) {
        delete(rootProject.layout.buildDirectory)
    }
}
