package com.telia.Lease_management.utils;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.StreamUtils;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telia.Lease_management.dto.ifuDto.IfuResponse;

@Getter
@Configuration
public class AppConfig {
    
    @Value("${email.set.From}")
    private String emailSetFrom;

    @Value("${ifu.auth.url}")
    private String ifuAuthUrl; // URL de l'API d'authentification

    @Value("${ifu.numifu.url}")
    private String ifuNumifuUrl; 

    @Value("${ifu.login}")
    private String login;

    @Value("${ifu.password}")
    private String password;

    
    @Value("${jwt.auth.converter.principal-attribute}")
    private String principleAttribute;
    
    @Value("${jwt.auth.converter.resource-id}")
    private String resourceId;

    @Value("${keycloak.token-url}")
    private String tokenKeycloakUrl;

    // @Bean
    // public RestTemplate restTemplate() {
    //     return new RestTemplate();
    // }

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.baseUrl("https://dgi.bf/api/ifu").build();
    }
    

    
}
