package ba.unsa.etf.nbp_tim6;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class NbpTim6Application {

	public static void main(String[] args) {
		SpringApplication.run(NbpTim6Application.class, args);
	}
}
