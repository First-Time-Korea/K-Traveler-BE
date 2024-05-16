package com.ssafy.firskorea.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI openAPI() {
		Info info = new Info().title("SpringBootMvcLocal API Docs").description("<h3>강의실에서 한 회원제 파일 게시판</h3>")
				.version("v1").contact(new io.swagger.v3.oas.models.info.Contact().name("semin kim")
						.email("2007ksm@naver.com").url("http://edu.ssafy.com"));

		return new OpenAPI().components(new Components()).info(info);
	}

	@Bean
	public GroupedOpenApi api() {
		return GroupedOpenApi.builder().group("springmvclocal").pathsToMatch("/api/**").build();
	}
}