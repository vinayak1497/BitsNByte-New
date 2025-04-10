// top-level build.gradle.kts
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.google.gms.google.services) apply false
}

buildscript {
    dependencies {
        // Google Services classpath for Firebase integration
        classpath("com.google.gms:google-services:4.4.2")
    }
}

