Spring Boot multiple modules template

[Create new Module]

- Just create new directory from root, then add a file name: `build.gradle.kts` with this template code.
----
	plugins {
        id("org.springframework.boot")
    
        kotlin("jvm")
        kotlin("plugin.spring")
    }
    
    dependencies {
        implementation(project(":common")) // if you want to use common module
        implementation(kotlin("stdlib-jdk8"))
        implementation(kotlin("reflect"))
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.apache.commons:commons-text:1.8")
        implementation("org.springframework.boot:spring-boot-starter-validation")
        // more dependencies here
    }
    // Uncomment the following code if you want to build a spring app.
    // tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    //    enabled = false
    // }
    
    // tasks.getByName<Jar>("jar") {
    //     enabled = true
    // }
----

- Add the module to `settings.gradle.kts`
----
    rootProject.name = "ecaf"
    include("common", "auth", "entity", "you-new-module")
----

- Refresh gradle
- Create a directory like: src/main/kotlin inside the new directory.
- Create package like you want like: com.example.product.
- Create an ModuleBoot.kts class with the code like a main class of spring app.

----
    package vn.amit.auth // or your package
    
    import org.springframework.boot.autoconfigure.SpringBootApplication
    import org.springframework.boot.autoconfigure.domain.EntityScan
    import org.springframework.boot.runApplication
    import org.springframework.context.annotation.ComponentScan
    
    @SpringBootApplication
    // scan the common package to take the common component like:
    // exception handler, restful body advice, language header interceptor,...
    @ComponentScan(basePackages = ["vn.amit.common", "vn.amit.auth"])
    // Scan the entity package
    @EntityScan(basePackages = ["vn.amit.entity.auth"])
    class AuthenticationServiceApplication
    
    fun main(args: Array<String>) {
        runApplication<AuthenticationServiceApplication>(*args)
    }
----

- Start to build your module as a spring application.


[Build]

`./gradlew clean build` builds the whole thing

`./gradlew auth:clean` build builds a single module where auth is the module name

`./gradlew auth:test` runs tests within a single module where auth is the module name

`./gradlew test` runs all tests across all modules

== License
Spring Boot is Open Source software released under the
https://www.apache.org/licenses/LICENSE-2.0.html[Apache 2.0 license].