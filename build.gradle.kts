plugins {
    application
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "self.tekichan"
version = "1.0.0"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    implementation("xerces:xercesImpl:2.12.2")
    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("org.slf4j:slf4j-simple:2.0.16")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("com.github.stefanbirkner:system-lambda:1.2.1")
}

application {
    mainClass.set("self.tekichan.xmlvalidator.MainApp")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.test {
    useJUnitPlatform()
}

// Configure the Shadow plugin to generate a fat JAR
tasks.shadowJar {
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }
    archiveFileName.set("XmlValidator-${project.version}-all.jar")
}

// Make the build task depend on shadowJar to create the fat JAR by default
tasks.build {
    dependsOn(tasks.shadowJar)
}