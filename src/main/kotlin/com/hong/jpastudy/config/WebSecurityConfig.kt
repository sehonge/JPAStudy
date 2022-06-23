package com.hong.jpastudy.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableWebSecurity
open class WebSecurityConfig: WebSecurityConfigurerAdapter() {

    @Bean
    open fun getPasswordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }


    // WebSecurityConfigurerAdapter deprecated -> https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
//    @Bean
    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .formLogin().disable()
            .headers().frameOptions().disable()

    }
}
