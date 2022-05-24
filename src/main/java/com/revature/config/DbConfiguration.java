package com.revature.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration // the Spring iOC container will scan this class and only
				// invoke the method of the class whose profile has been declared active
@ConfigurationProperties("spring.datasource")
public class DbConfiguration { // profiles exist to differentiate properties and config
								// based on the software state (testing, development, production)
	
	private String driverClassName;
	private String url;
	private String username;
	private String password;
	
	@Profile("dev")
	@Bean
	public String debDBConnection() {
		
		System.out.println(driverClassName);
		System.out.println(url);
		System.out.println(username);
		System.out.println(password);
		
		return "DB connection for dev profile - low cost RDS instance";
	}
	
	@Profile("prod")
	@Bean
	public String prodDBConnection() {
		
		System.out.println(driverClassName);
		System.out.println(url);
		System.out.println(username);
		System.out.println(password);
		
		return "DB connection for prod profile - high performance RDS instance";
	}
	
	@Profile("default")
	@Bean
	public String defaultDBConnection() {
		
		System.out.println(driverClassName);
		System.out.println(url);
		System.out.println(username);
		System.out.println(password);
		
		return "DB connection for default profile";
		
	}

}
