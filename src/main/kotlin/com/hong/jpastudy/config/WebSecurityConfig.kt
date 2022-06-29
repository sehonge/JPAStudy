package com.hong.jpastudy.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
open class WebSecurityConfig {

    @Bean
    open fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    // WebSecurityConfigurerAdapter deprecated -> https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
    @Bean
    open fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests{
                authorize -> authorize.mvcMatchers()
            }
            .oauth2ResourceServer {
                it.jwt {
                    it.jwtAuthenticationConverter()
                }
            }
            .csrf().disable()
            .formLogin().disable()
            .headers().frameOptions().disable()
            .and()
            .exceptionHandling()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

    }

//    override fun configure(http: HttpSecurity) {
//        http
//            .csrf().disable()
//            .formLogin().disable()
//            .headers().frameOptions().disable()
//            .and()
//            .exceptionHandling()
//            .authenticationEntryPoint(unauth)
//            .and()
//            .sessionManagement()
//            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//
//
//    }
}
