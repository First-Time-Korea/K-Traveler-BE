package com.ssafy.firskorea.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.ssafy.firskorea.intercepter.AuthenticationInterceptor;

@Configuration
public class ServletConfig implements WebMvcConfigurer {

	@Value("${planThumbFile.path}")
	private String PLAN_FILE_PATH;

	@Value("${attractionKCurtureFile.path}")
	private String K_ATTRACTION_IMG_PATH;
	
	@Value("${articleFile.path.upload-images}")
	private String ARTICLE_IMG_PATH;
	
	@Autowired
	private AuthenticationInterceptor authenticationInterceptor;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
		registry.addResourceHandler("/img/**").addResourceLocations("/WEB-INF/assets/img/");
		registry.addResourceHandler("/css/**").addResourceLocations("/WEB-INF/assets/css/");
		registry.addResourceHandler("/js/**").addResourceLocations("/WEB-INF/assets/js/");
		registry.addResourceHandler("/plan/img/**").addResourceLocations("file:///" + PLAN_FILE_PATH);
		registry.addResourceHandler("/attraction/kculture/img/**").addResourceLocations("file:///" + K_ATTRACTION_IMG_PATH);
		registry.addResourceHandler("/article/img/**").addResourceLocations("file:///" + ARTICLE_IMG_PATH);
	}

	@Bean
	ViewResolver viewResolver() {
		InternalResourceViewResolver irvr = new InternalResourceViewResolver();
		irvr.setPrefix("/WEB-INF/views/");
		irvr.setSuffix(".jsp");
		return irvr;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authenticationInterceptor)
			.addPathPatterns("/users/logout/**")
			.addPathPatterns("/users/info/**")
			.addPathPatterns("/users/refresh/**")
			.addPathPatterns("/articles")
			.addPathPatterns("/articles/*")
			.addPathPatterns("/articles/modify/**");
	}

	@Bean
	StandardServletMultipartResolver multipartResolver() {
		StandardServletMultipartResolver mr = new StandardServletMultipartResolver();
		return mr;
	}

	@Bean
	BeanNameViewResolver fileViewResolver() {
		BeanNameViewResolver bnvr = new BeanNameViewResolver();
		bnvr.setOrder(0);
		return bnvr;
	}

}