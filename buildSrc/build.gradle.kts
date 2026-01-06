plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.plugindep.kotlin)
    implementation(libs.plugindep.maven.publish)
}
