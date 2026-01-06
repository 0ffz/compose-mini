plugins {
    id("conventions.library")
    id("conventions.publishing")
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrainsCompose)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
        }
        all {
            languageSettings.enableLanguageFeature("ContextParameters")
        }
    }
}