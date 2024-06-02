import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    application
    id("org.springframework.boot") version libs.versions.spring.boot
    id("io.spring.dependency-management") version libs.versions.spring.dependency.management
    id("org.sonarqube") version libs.versions.sonar.gradle
    id("com.gorylenko.gradle-git-properties") version libs.versions.git.gradle
    jacoco
}

group = "org.hyperskill.community"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
    }
}

jacoco {
    reportsDirectory = layout.buildDirectory.dir("reports/jacoco")
}

val sonarToken: String by project
sonar {
    properties {
        property("sonar.token", sonarToken)
        property("sonar.projectKey", "flashcards-server")
        property("sonar.projectName", "Flashcards Server")
        property("sonar.junit.reportPaths", "build/test-results/test")
        property("sonar.host.url", "http://localhost:9000")
    }
}

tasks.sonar {
    dependsOn(tasks.jacocoTestReport)
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
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    // add our frontend jar as a dependency
    implementation(project(":flashcards-client"))

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mongodb")
}

springBoot {
    buildInfo()
}

tasks.withType<BootRun> {
    workingDir = rootProject.projectDir
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named("processResources") {
    dependsOn(":select-compose-file")
}
