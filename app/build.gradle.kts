plugins {
    java
    id("com.gradleup.shadow") version "8.3.7"
}

group = "org.yuemi"
version = "1.0-SNAPSHOT"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    implementation("org.eclipse.jgit:org.eclipse.jgit:6.6.0.202305301015-r")
    implementation("com.h2database:h2:2.2.224")
    implementation("org.bstats:bstats-bukkit:3.0.2")
}

// Disable default JAR task
tasks.jar {
    enabled = false
}

// Configure shadowJar
tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    archiveBaseName.set("Git-Craft")
    archiveClassifier.set("")
    archiveVersion.set("")
    relocate("org.bstats", "org.yuemi.libs.bstats") 
}

// Make build depend on shadowJar
tasks.named("build") {
    dependsOn("shadowJar")
}
