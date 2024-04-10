plugins {
    id("java")
    id("org.sonarqube") version "4.4.1.3373"
    id("org.springframework.boot") version "3.2.4"
    id("io.spring.dependency-management") version "1.1.4"
    jacoco
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

jacoco {
    toolVersion = "0.8.11"
}

sonar {
  properties {
    property("sonar.projectKey", "SE-II-group-do-1_stratego-server")
    property("sonar.organization", "se-ii-group-do-1")
    property("sonar.host.url", "https://sonarcloud.io")
    property("sonar.coverage.exclusions", "src/main/java/com/example/stratego/connection/**,")
  }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
    testLogging {
            events("passed", "skipped", "failed")
    }
}


tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
    }
}
