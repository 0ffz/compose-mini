plugins {
    id("com.vanniktech.maven.publish")
}

mavenPublishing {
    publishToMavenCentral()
    if (!version.toString().endsWith("-SNAPSHOT")) {
        signAllPublications()
    }

    coordinates(group.toString(), name, version.toString())

    pom {
        name = "Compose mini ${project.name}"
        description = "Helpers for building compose UI libraries from scratch."
        url = "https://github.com/0ffz/compose-mini"
        developers {
            developer {
                name = "Danielle Voznyy"
                email = "dan.voznyy@gmail.com"
                organization = "github"
                organizationUrl = "https://github.com/0ffz"
            }
        }
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        scm {
            url = "https://github.com/0ffz/compose-mini"
            connection = "scm:git:git://github.com/0ffz/compose-mini.git"
            developerConnection = "scm:git:ssh://github.com:0ffz/compose-mini.git"
        }
    }
}