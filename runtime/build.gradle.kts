plugins {
    alias(idofrontLibs.plugins.mia.kotlin.multiplatform)
    alias(idofrontLibs.plugins.mia.publication)
    alias(idofrontLibs.plugins.compose.compiler)
    alias(idofrontLibs.plugins.jetbrainsCompose)
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
    jvm()
    js(IR) {
        browser()
        nodejs()
    }
    wasmJs() {
        browser()
        nodejs()
    }
//    iosX64()
//    iosArm64()
//    iosSimulatorArm64()
//    linuxX64()

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
        }
        all {
            languageSettings.enableLanguageFeature("ContextParameters")
        }
    }
}