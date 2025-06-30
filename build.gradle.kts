plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.kotlin.plugin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.sonarqube)
    alias(libs.plugins.jooq) apply false
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    plugins.withType<JavaPlugin> {
        extensions.configure<JavaPluginExtension> {
            toolchain.languageVersion.set(JavaLanguageVersion.of(17))
        }

        configurations.configureEach {
            if (name == "compileOnly") {
                extendsFrom(configurations.getByName("annotationProcessor"))
            }
        }

        tasks.withType<Test>().configureEach {
            useJUnitPlatform()
        }
    }
}

sonar {
    properties {
        property("sonar.projectKey", "antop-dev_toy-board")
        property("sonar.organization", "antop-dev")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}
