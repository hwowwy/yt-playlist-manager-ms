package it.lunacia.yt.config;


import io.netty.handler.logging.LogLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;


@Configuration
@Slf4j
public class WebClientConfig {
    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }
//    @Bean
//    public WebClient.Builder loadBalancedWebClientBuilder(ReactiveClientRegistrationRepository clientRegistrations,
//                                                          ObjectMapper objectMapper,
//                                                          ServerOAuth2AuthorizedClientRepository clientRepository) {
//
//        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth2ClientFilter = new ServerOAuth2AuthorizedClientExchangeFilterFunction(
//                clientRegistrations,
//                clientRepository);
//        oauth2ClientFilter.setDefaultClientRegistrationId("google");
//        WebClient.Builder builder = WebClient.builder();
//        builder.filter(oauth2ClientFilter);
//        return builder;
//    }
    @Bean
    WebClient buildWebClient(ReactiveOAuth2AuthorizedClientManager  authorizedClientManager){

        ServerOAuth2AuthorizedClientExchangeFilterFunction  oauth2Client =
                new ServerOAuth2AuthorizedClientExchangeFilterFunction(
                        authorizedClientManager);
        oauth2Client.setDefaultClientRegistrationId("google");
        HttpClient httpClient = HttpClient.create().wiretap("reactor.netty.http.client.HttpClient", LogLevel.INFO, AdvancedByteBufFormat.TEXTUAL);
        return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).filter(oauth2Client)
        .filter(logRequest()).build();
    }

    @Bean
    public AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager(
            ReactiveClientRegistrationRepository clientRegistrationRepository,
            ReactiveOAuth2AuthorizedClientService authorizedClientService) {

        ReactiveOAuth2AuthorizedClientProvider authorizedClientProvider =
                ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
                        .authorizationCode()
                        .refreshToken()
                        .clientCredentials()
                        .build();

        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(
                        clientRegistrationRepository,  authorizedClientService);

        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }
}
