import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    java
    id("org.springframework.boot") version libs.versions.spring.boot
    id("io.spring.dependency-management") version libs.versions.spring.dependency.management
    id("org.graalvm.buildtools.native") version libs.versions.graalvm.buildtools
}

group = "org.hyperskill.community"
version = "0.0.1-SNAPSHOT"

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
// On ARM64 (!) uncomment the following lines to build JVM AMD64 image, since then default paketo builder is used
tasks.named<BootBuildImage>("bootBuildImage") {
    builder.set("dashaun/builder:tiny")
    imageName.set(dockerHubRepo + rootProject.name + project.name + ":" + version)
    createdDate.set("now")
    environment.put("BP_NATIVE_IMAGE", "true")
}
