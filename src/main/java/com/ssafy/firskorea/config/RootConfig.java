package com.ssafy.firskorea.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement // annotation-driven transaction-manager
@ComponentScan("com.ssafy")
@PropertySource("classpath:/application.properties")
//@EnableAspectJAutoProxy // aspectj-autoproxy (aop 설정할 때 풀자 이거 쓸거면 weaver dependency 설정해야 함)
public class RootConfig {

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	public HikariConfig hikariConfig() {
		return new HikariConfig();
	}

	@Bean
	public DataSource dataSource() throws Exception {
		DataSource dataSource = new HikariDataSource(hikariConfig());
		System.out.println(dataSource.toString());
		return dataSource;
	}

	@Bean
	public DataSourceTransactionManager transactionManager(@Autowired DataSource dataSource) {
		DataSourceTransactionManager dstm = new DataSourceTransactionManager(dataSource);
		return dstm;
	}

}