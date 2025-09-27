package com.webechannelingsystem.webechannelingsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.webechannelingsystem"})
@EnableJpaRepositories("com.webechannelingsystem.webechannelingsystem.repository")
public class WebechannelingsystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebechannelingsystemApplication.class, args);
	}

}
