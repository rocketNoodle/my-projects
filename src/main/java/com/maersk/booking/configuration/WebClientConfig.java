package com.maersk.booking.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        HttpClient httpClient = HttpClient.create()
                .httpResponseDecoder(spec -> spec.maxHeaderSize(32 * 1024)); // 32 KB

        return WebClient.builder()
                .baseUrl("https://maersk.com")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
