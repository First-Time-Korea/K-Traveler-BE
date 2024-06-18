package com.ssafy.firskorea.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info().title("K-Traveler Open API Docs").description("<h3>별도의 인증이 필요 없는 api 모음</h3>")
                .version("v1").contact(new io.swagger.v3.oas.models.info.Contact().name("semin kim")
                        .email("seminkim1432@gmail.com").url("http://edu.ssafy.com"));

        return new OpenAPI().components(new Components()).info(info);
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder().group("회원 관리").pathsToMatch("/user/**").build();
    }

    @Bean
    public GroupedOpenApi boardApi() {
        return GroupedOpenApi.builder().group("게시판 관리").pathsToMatch("/articles/**").build();
    }

    @Bean
    public GroupedOpenApi attractionApi() {
        return GroupedOpenApi.builder().group("관광지 관리").pathsToMatch("/attractions/**").build();
    }

    @Bean
    public GroupedOpenApi categoryApi(){
        return GroupedOpenApi.builder()
                .group("테마 및 지역 정보")
                .pathsToMatch("/themes/**", "/regions/**")
                .build();
    }

}