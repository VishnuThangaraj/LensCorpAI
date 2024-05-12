package com.vishnuthangaraj.LensCorpAI;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(title = "LensCorpAI Assessment", version = "1.0",
		description = "User Authentication SpringBoot application",
		contact = @Contact(name = "Vishnu Thangaraj",
				url = "https://drive.google.com/file/d/1uF0UlLTTeRnLVkl9UK4PjLEDd_FIWkZk/view?usp=drive_link",
				email = "vishnuthangaraj.developer@gmail.com")))
@SpringBootApplication
public class LensCorpAiApplication {

	public static void main(String[] args) {
		SpringApplication.run(LensCorpAiApplication.class, args);
	}

}
