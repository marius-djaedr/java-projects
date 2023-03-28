package com.me.ttrpg.abilities.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.me.io.google.GoogleSheetsIo;
import com.me.util.spring.UtilSpringConfig;

@Configuration
@Import(UtilSpringConfig.class)
@ComponentScan(basePackages = {"com.me.ttrpg.abilities"})
public class SpringConfig {

	@Bean
	public GoogleSheetsIo sheetsIo() {
		return new GoogleSheetsIo();
	}
}
