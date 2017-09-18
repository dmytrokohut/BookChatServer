package com.dkohut.bookchat.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.dkohut.bookchat.server")
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class BookChatServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookChatServerApplication.class, args);
	}
}
