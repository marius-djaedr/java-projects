package com.me.ttrpg.dungeonworld.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.me.io.google.GoogleDocsIo;

@Configuration
@ComponentScan(basePackages = {"com.me.ttrpg.dungeonworld"})
@PropertySource({"classpath:rand-options.properties"})
public class SpringConfig {

	@Bean
	public GoogleDocsIo googleDocsIo() {
		return new GoogleDocsIo();
	}

}
