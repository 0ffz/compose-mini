plugins {
    id("conventions.publishing") apply false
    id("conventions.library") apply false
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}