plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.0"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "org.antop"
version = "0.0.1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

val htmxVersion: String by project
val htmxSpringBootVersion: String by project
val kotlinxDatetimeVersion: String by project
val exposedVersion: String by project
val prettytimeVersion: String by project

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    // https://github.com/Kotlin/kotlinx-datetime
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDatetimeVersion")
    // https://jetbrains.github.io/Exposed/home.html
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-crypt:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-spring-boot-starter:$exposedVersion")
    // view
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    // https://github.com/wimdeblauwe/htmx-spring-boot
    implementation("io.github.wimdeblauwe:htmx-spring-boot:$htmxSpringBootVersion")
    implementation("io.github.wimdeblauwe:htmx-spring-boot-thymeleaf:$htmxSpringBootVersion")
    // https://www.ocpsoft.org/prettytime/
    implementation("org.ocpsoft.prettytime:prettytime:$prettytimeVersion")

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    // https://htmx.org/
    runtimeOnly("org.webjars.npm:htmx.org:$htmxVersion")
    // https://mariadb.com/kb/en/about-mariadb-connector-j/
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
