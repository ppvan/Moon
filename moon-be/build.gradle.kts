plugins {
    java
    id("org.springframework.boot") version "3.1.4"
    id("io.spring.dependency-management") version "1.1.3"
}

group = "vnu.uet"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
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
    // https://mvnrepository.com/artifact/org.python/jython
    implementation("org.python:jython:2.7.2")

    // https://mvnrepository.com/artifact/commons-net/commons-net
    implementation("commons-net:commons-net:3.9.0")

    // https://mvnrepository.com/artifact/com.jcraft/jsch
    implementation("com.jcraft:jsch:0.1.55")

    // https://mvnrepository.com/artifact/net.jthink/jaudiotagger
    implementation("net.jthink:jaudiotagger:3.0.1")

    // https://mvnrepository.com/artifact/com.mpatric/mp3agic
    implementation("com.mpatric:mp3agic:0.9.1")

//    // https://mvnrepository.com/artifact/org.hibernate/hibernate-search-orm
//    implementation("org.hibernate:hibernate-search-orm:5.11.12.Final")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.postgresql:postgresql:42.6.0")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
    testImplementation("org.springframework.security:spring-security-test")

    implementation("org.hibernate.orm:hibernate-core:6.3.1.Final")
    implementation("commons-io:commons-io:2.14.0")
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.mysql:mysql-connector-j")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

//bootJar {
//    mainClassName = "vnu.uet.moonbe.MoonBeApplication"
//}
