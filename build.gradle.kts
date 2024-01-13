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

tasks.register<Copy>("select-compose-file") {
    group = "build"
    description = "Copy compose file for OS architecture"
    val composeFile = if (System.getProperty("os.arch").equals("aarch64")) {
        "compose_arm.yaml"
    } else {
        "compose_amd.yaml"
    }
    println("Using $composeFile")
    from("${rootDir}/docker/arch/$composeFile")
    into("${rootDir}/docker")
    rename(composeFile, "compose.yaml")
}

tasks.register<BootRun>("bootRunFlashcards") {
    group = "build"
    description = "Run flashcards app and start containers for mongodb and auth server"
    dependsOn(":flashcards-server:bootRun")
}

