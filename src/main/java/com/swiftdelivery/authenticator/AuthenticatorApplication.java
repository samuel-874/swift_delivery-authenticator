package com.swiftdelivery.authenticator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.swiftdelivery.authenticator.response.CustomResponse.response;

@RestController()
@SpringBootApplication
public class AuthenticatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticatorApplication.class, args);
	}

	@GetMapping("/hello")
	public ResponseEntity helloWorld(){
		return response(200,"Works Fine",null);
	}

}
