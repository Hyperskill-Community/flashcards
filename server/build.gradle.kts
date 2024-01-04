plugins {
    application
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.spotless)
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
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
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

tasks.withType<Test> {
    useJUnitPlatform()
}

spotless {
    java {
        // Excludes build folder since it contains generated java classes.
        targetExclude("build/**")
        endWithNewline()
        // \\# is prefix for static imports
        // empty string covers all imports that aren't explicitly specified
        importOrder("\\#", "java", "javax", "org.hyperskill.community", "")
        removeUnusedImports()
        eclipse("4.26").configFile("src/main/resources/formatting.xml")
    }
}
