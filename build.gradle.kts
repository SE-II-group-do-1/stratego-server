plugins {
    id("java")
    id("org.sonarqube") version "4.4.1.3373"
    jacoco
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

sonar {
  properties {
    property("sonar.projectKey", "SE-II-group-do-1_stratego-server")
    property("sonar.organization", "se-ii-group-do-1")
    property("sonar.host.url", "https://sonarcloud.io")
  }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-api")
}

tasks.test {
    useJUnitPlatform()
}