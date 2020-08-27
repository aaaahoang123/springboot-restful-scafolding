package vn.amit.springboot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["vn.amit.common", "vn.amit.springboot", "vn.amit.token.jwt", "vn.amit.security"])
@EntityScan(basePackages = ["vn.amit.entity.auth"])
class AuthenticationServiceApplication

fun main(args: Array<String>) {
    runApplication<AuthenticationServiceApplication>(*args)
}
