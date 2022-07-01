import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    kotlin("plugin.jpa") version "1.6.21"
    kotlin("kapt") version "1.6.0"

    jacoco
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("mysql:mysql-connector-java")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.jacoco:org.jacoco.agent:0.8.7")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")

    kapt(group = "com.querydsl", name = "querydsl-apt", classifier = "jpa")
    implementation("com.querydsl:querydsl-jpa:5.0.0")
    implementation("io.springfox:springfox-boot-starter:3.0.0")
    implementation("com.auth0:java-jwt:3.10.3")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

kotlin.sourceSets.main {
    setBuildDir("${buildDir}/generated/source/kapt/main")
}

jacoco {
    // JaCoCo 버전
    toolVersion = "0.8.7"
}

tasks {
    jacocoTestReport {
        reports {
            // 원하는 리포트를 켜고 끌 수 있습니다.
            html.required.set(true)
            xml.required.set(false)
            csv.required.set(false)
        }
    }

    jacocoTestCoverageVerification {
        violationRules {
            rule {
                element = "CLASS"

                limit {
                    counter = "BRANCH"
                    value = "COVEREDRATIO"
                    minimum = "0.90".toBigDecimal()
                }
            }
        }
    }
}
