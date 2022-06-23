package com.hong.jpastudy.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket

@Configuration
open class SwaggerConfig {

    @Bean
    open fun api(): Docket {
        return Docket(DocumentationType.OAS_30)
            .useDefaultResponseMessages(false)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.hong.jpastudy.controller"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(metaInfo())
    }

    private fun metaInfo(): ApiInfo{
        return ApiInfoBuilder()
            .title("공지사항 어드민 API SPEC")
            .version("1.0")
            .contact(
                Contact(
                    "sehong, im",
                    "https://github.com/sehonge",
                    "sehonge1602@naver.com"
                )
            )
            .build()
    }
}
