package com.hong.jpastudy.config

import com.hong.jpastudy.config.jwt.JwtAuthenticationFilter
import com.hong.jpastudy.config.jwt.JwtAuthorizationFilter
import com.hong.jpastudy.repository.UserRepository
import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@EnableWebSecurity
@RequiredArgsConstructor
class WebSecurityConfig(val userRepository: UserRepository) : WebSecurityConfigurerAdapter() {

    @Bean
    open fun passwordEncoder(): PasswordEncoder {
            return BCryptPasswordEncoder()
    }

    override fun configure(http: HttpSecurity?) {

        if (http != null) {
            http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .headers().frameOptions().disable()
                .and()
                .addFilter(getJwtAuthenticationFilter())
                .addFilter(JwtAuthenticationFilter(authenticationManager()))
                .addFilter(JwtAuthorizationFilter(userRepository, authenticationManager()))
        } else {
            throw RuntimeException("HttpSecurity NullPointerException")
        }
    }

    fun getJwtAuthenticationFilter(): JwtAuthenticationFilter {
        var filter = JwtAuthenticationFilter(authenticationManager())
        filter.setFilterProcessesUrl("/api/user/login")
        filter.usernameParameter = "userId"
        return filter
    }
}
