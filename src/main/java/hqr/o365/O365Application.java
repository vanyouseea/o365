package hqr.o365;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class O365Application {

	public static void main(String[] args) {
		SpringApplication.run(O365Application.class, args);
	}

}
