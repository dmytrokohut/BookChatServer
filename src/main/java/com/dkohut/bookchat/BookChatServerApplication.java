package com.dkohut.bookchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.dkohut.bookchat")
public class BookChatServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookChatServerApplication.class, args);
	}
}
