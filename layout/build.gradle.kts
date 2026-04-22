plugins {
    id("conventions.library")
    id("conventions.publishing")
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrainsCompose)
}
kotlin {
    dependencies {
        implementation("org.jetbrains.compose.runtime:runtime:1.10.3")
        implementation("com.github.skydoves:compose-stable-marker:1.0.7")
//        api("org.jetbrains.compose.ui:ui:1.10.3") { isTransitive = false }
        api("org.jetbrains.compose.ui:ui-unit:1.10.3")
        api("org.jetbrains.compose.ui:ui-unit:1.10.3")
        implementation(project(":modifier"))
    }
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}