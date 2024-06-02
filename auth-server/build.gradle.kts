import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    application
    id("org.springframework.boot") version libs.versions.spring.boot
    id("io.spring.dependency-management") version libs.versions.spring.dependency.management
    id("com.gorylenko.gradle-git-properties") version libs.versions.git.gradle
    id("org.graalvm.buildtools.native") version libs.versions.graalvm.buildtools
}

graalvmNative {
    testSupport.set(false) // seems to not work on 2024-06-02
}

group = "org.hyperskill.community"
version = "0.0.1-SNAPSHOT"

application {
    mainClass.set("org.hyperskill.community.authserver.AuthServerApplication")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

springBoot {
    buildInfo()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-oauth2-authorization-server")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val dockerHubRepo = "wisskirchenj/"
tasks.named<BootBuildImage>("bootBuildImage") {
//    buildpacks.set(listOf("paketobuildpacks/java:beta"))
    buildpacks.set(listOf("paketobuildpacks/java-native-image:latest"))
    builder.set("paketobuildpacks/builder-jammy-buildpackless-tiny")
    imageName.set(dockerHubRepo + rootProject.name + project.name + ":" + version)
    createdDate.set("now")
}
