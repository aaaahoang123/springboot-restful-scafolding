plugins {
    id("org.springframework.boot")
    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
//    implementation(project(":common"))
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    implementation("io.jsonwebtoken:jjwt:0.9.1")
//    implementation("org.apache.commons:commons-text:1.8")
//    implementation("commons-codec:commons-codec:1.14")
//    implementation("org.springframework.boot:spring-boot-starter-validation")
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}