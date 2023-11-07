package it.lunacia.yt;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GestorePlaylistApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestorePlaylistApplication.class, args);
	}
	@Bean
	public OpenAPI springOpenAPI() {
		return new OpenAPI()
				.components(new Components().addSecuritySchemes("bearer-key",
						new SecurityScheme().type(SecurityScheme.Type.OAUTH2).scheme("bearer").bearerFormat("JWT")))
				.info(new Info().title("Youtube playlist manager MS")
						.description("").version("v0.0.1"));

	}
}
