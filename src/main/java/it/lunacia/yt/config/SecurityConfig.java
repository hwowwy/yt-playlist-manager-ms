package it.lunacia.yt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${spring.security.oauth2.client.registration.google.scope}")
    private String scopes;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("spring.security.oauth2.client.registration.google.client-secret")
    private String clientSecret;

    @Bean
    public CorsConfigurationSource corsConfiguration() {
        final CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.applyPermitDefaultValues();
        corsConfig.addAllowedMethod("GET");
        corsConfig.addAllowedMethod("PATCH");
        corsConfig.addAllowedMethod("POST");
        corsConfig.addAllowedMethod("OPTIONS");
        corsConfig.addAllowedMethod("PUT");
        corsConfig.addAllowedMethod("HEAD");
        corsConfig.addAllowedMethod("DELETE");
        corsConfig.setAllowedOrigins(List.of("*"));
        corsConfig.setAllowedHeaders(List.of("Authorization", "Requestor-Type", "Content-Type", "Cache-Control"));
        corsConfig.setExposedHeaders(List.of("X-Get-Header", "Authorization", "Content-Type", "Cache-Control"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        return new CorsWebFilter(corsConfiguration());
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(authorize -> authorize
                        .anyExchange().authenticated()
                )
                .oauth2Login(withDefaults()).csrf(ServerHttpSecurity.CsrfSpec::disable);

        return http.build();
    }
}
