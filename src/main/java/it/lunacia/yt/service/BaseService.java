package it.lunacia.yt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public abstract class BaseService {
    @Autowired
    protected WebClient webClient;
    @Autowired
    ServerOAuth2AuthorizedClientRepository authorizedClient;

    //Performs REST call using queryParams,headers and a body
    protected <T> Mono<T>  performCall(HttpMethod method, String path, Optional<HttpHeaders> params, Optional<HttpHeaders> headers, Optional<Object> body, Class<T> responseType){
        return webClient.method(method).uri(UriComponentsBuilder.fromUriString(getBaseUrl()+path).queryParams(params.orElseGet(null)).toUriString())
                .headers(httpHeaders ->
                    headers.ifPresent(httpHeaders::addAll)
                )
                .body(body.map(BodyInserters::fromValue).orElseGet(BodyInserters::empty))
                .exchangeToMono(resp -> resp.bodyToMono(responseType));
    }
    //Performs REST call without headers
    protected <T> Mono<T>  performCall(HttpMethod method, String path, Optional<HttpHeaders> params, Optional<Object> body, Class<T> responseType){
        return webClient.method(method).uri(UriComponentsBuilder.fromUriString(getBaseUrl()+path).queryParams(params.orElseGet(null)).toUriString())
                .body(body.map(BodyInserters::fromValue).orElseGet(BodyInserters::empty))
                .exchangeToMono(resp -> resp.bodyToMono(responseType));
    }

    //Performs REST call without body and headers
    protected <T> Mono<T>  performCall(HttpMethod method, String path, Optional<HttpHeaders> params, Class<T> responseType){
        return webClient.method(method).uri(UriComponentsBuilder.fromUriString(getBaseUrl()+path).queryParams(params.orElseGet(null)).toUriString())
                .attributes(
                        ServerOAuth2AuthorizedClientExchangeFilterFunction
                                .clientRegistrationId("google")).exchangeToMono(resp -> resp.bodyToMono(responseType));
    }
    //Performs REST call without queryParams,body and headers
    protected <T> Mono<T>  performCall(HttpMethod method, String path, Class<T> responseType){
        return webClient.method(method).uri(UriComponentsBuilder.fromUriString(getBaseUrl()+path).toUriString())
                .exchangeToMono(resp -> resp.bodyToMono(responseType));
    }

    abstract protected String getBaseUrl();
}
