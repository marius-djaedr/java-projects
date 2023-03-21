package com.me.turnips.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.me.io.google.GoogleSheetsIo;

@Configuration
@ComponentScan(basePackages = {"com.me.turnips"})
public class SpringConfig {

	@Bean
	public GoogleSheetsIo googleSheetsIo() {
		return new GoogleSheetsIo();
	}
}
