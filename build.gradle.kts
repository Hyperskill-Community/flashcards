import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    id("org.springframework.boot") version libs.versions.spring.boot
    id("io.spring.dependency-management") version libs.versions.spring.dependency.management
}

repositories {
    mavenCentral()
}

tasks.withType<BootRun> {
    enabled = false
}

tasks.register<BootRun>("bootRunFlashcards") {
    group = "build"
    description = "Run flashcards app and start containers for mongodb and auth server"
    dependsOn(":flashcards-server:bootRun")
}

