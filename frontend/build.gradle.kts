import com.github.gradle.node.npm.task.NpmTask

plugins {
    base
    id("com.github.node-gradle.node") version libs.versions.node.gradle
}

node {
    version = libs.versions.node.js
    nodeProjectDir = projectDir
    download = true
}

configurations {
    create("npmResources")
    default {
        extendsFrom(configurations["npmResources"])
    }
}

tasks.register<NpmTask>("eslint") {
    group = "npm"
    description = "ES-Linting of frontend code"
    args.addAll("run", "lint")
    inputs.dir("src")
    inputs.dir("test")
    dependsOn("npmInstall")
}

tasks.register<NpmTask>("vitest") {
    group = "verification"
    description = "Runs vitest"
    args.addAll("run", "test")
    inputs.dir("src")
    inputs.dir("test")
    dependsOn("eslint")
}

tasks.register<NpmTask>("npmBuild") {
    group = "npm"
    description = "Builds the frontend"
    args.addAll("run", "build", "--omit=dev")
    inputs.files("package.json", "package-lock.json", "vite.config.ts", "tsconfig.json", "tsconfig.node.json")
    inputs.dir("src")
    inputs.dir(fileTree("node_modules").exclude(".cache"))
    outputs.dir("dist")
    dependsOn("eslint", "vitest")
}

tasks.register<Zip>("packageFrontend") {
    group = "build"
    description = "Packages the frontend into a jar"
    archiveBaseName = project.name
    archiveExtension = "jar"
    from("dist") {
        into("static")
    }
    dependsOn("npmBuild")
}

artifacts {
    val archivePath = (tasks.named("packageFrontend").get() as Zip).archiveFile
    add("npmResources", archivePath) {
        type = "jar"
        builtBy("packageFrontend")
    }
}

tasks.withType<Delete>().named("clean") {
    delete("dist")
}
