package com.telia.Lease_management.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telia.Lease_management.dto.ifuDto.AuthRequest;
import com.telia.Lease_management.dto.ifuDto.AuthResponse;
import com.telia.Lease_management.dto.ifuDto.IfuRequest;
import com.telia.Lease_management.dto.ifuDto.IfuResponse;
import com.telia.Lease_management.utils.AppConfig;

import lombok.AllArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownContentTypeException;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
@AllArgsConstructor
public class IfuService {

    // private final RestTemplate restTemplate;
    private final WebClient webClient;
    private AppConfig appConfig;
    private final ObjectMapper objectMapper;


    public Mono<String> authenticate() {
        return webClient.post()
            .uri("/authentification")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(Map.of("LOGIN", appConfig.getLogin(), "MOTDEPASSE", appConfig.getPassword()))
            .retrieve()
            .bodyToMono(Map.class)
            .map(response -> (String) response.get("TOKEN"))
            .doOnNext(token -> log.info("Token received: {}", token))
            .doOnError(e -> log.error("Error in authentication: {}", e.getMessage()));
    }


    public Mono<IfuResponse> checkingIfu(String numIfu, String token) {
        IfuRequest ifuRequest = new IfuRequest(numIfu, token);
    
        return webClient.post()
            .uri("/numifu")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(ifuRequest)
            .retrieve()
            .bodyToMono(IfuResponse.class)
            .doOnNext(response -> log.info("IFU response received: {}", response))
            .doOnError(e -> log.error("Error in checking IFU: {}", e.getMessage()));
    }
    







//*********************************************************************************** */
//*********************************************************************************** */

    // private HttpHeaders gethttpHeaders(){
    //     HttpHeaders headers = new HttpHeaders();
    //     headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    //     headers.setContentType(MediaType.APPLICATION_JSON);
    //     return headers;
    //  }
     
    // public String authenticate() {

    //     HttpHeaders headers = new HttpHeaders();
    //     headers.setContentType(MediaType.APPLICATION_JSON);

    //     String requestBody = String.format("{\"LOGIN\":\"%s\", \"MOTDEPASSE\":\"%s\"}", appConfig.getLogin(), appConfig.getPassword());
    //     HttpEntity<String> request = new HttpEntity<>(requestBody, gethttpHeaders());

    //     ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
    //         appConfig.getIfuAuthUrl(), 
    //         HttpMethod.POST, 
    //         request, 
    //         new ParameterizedTypeReference<Map<String, Object>>() {}
    //     );

    //     return (String) response.getBody().get("TOKEN");

    // }
    
    //  // Méthode pour vérifier l'IFU avec un token et un numéro IFU
    //  public ResponseEntity<String> checkingIfu(String numIfu, String token) {
    //     IfuRequest ifuRequest = new IfuRequest(numIfu, token);
    //     HttpEntity<IfuRequest> entity = new HttpEntity<>(ifuRequest, gethttpHeaders());
    
    //     ResponseEntity<String> response = restTemplate.exchange(
    //         appConfig.getIfuNumifuUrl(),
    //         HttpMethod.POST, 
    //         entity,
    //         String.class
    //     );
    
    //     log.info("Response from IFU service: {}", response.getBody());

    //         // Log de la réponse pour débogage
    //     log.debug("Response Status: {}", response.getStatusCode());
    //     log.debug("Response Body: {}", response.getBody());
    //     return response;
    // }
    

    // public ResponseEntity<?> verifyIfu(String numIfu, String token) {
    //     IfuRequest ifuRequest = new IfuRequest(numIfu, token);
    //     HttpEntity<IfuRequest> entity = new HttpEntity<>(ifuRequest, gethttpHeaders());
    
    //     ResponseEntity<String> response = restTemplate.exchange(
    //         appConfig.getIfuNumifuUrl(),
    //         HttpMethod.POST,
    //         entity,
    //         String.class
    //     );
    
    //     log.info("verifyIfu-verifyIfu-verifyIfu: {}", response.getBody());
    //         // Log de la réponse pour débogage
    //     log.debug("Response Status: {}", response.getStatusCode());
    //     log.debug("Response Body: {}", response.getBody());

    //     // Vérifie si la réponse est en JSON et essaie de la mapper vers IfuResponse
    //     if (response.getStatusCode().is2xxSuccessful() && response.getHeaders().getContentType() != null &&
    //         response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON)) {
    //         try {
    //             IfuResponse ifuResponse = objectMapper.readValue(response.getBody(), IfuResponse.class);
    //             return ResponseEntity.ok(ifuResponse);
    //         } catch (JsonProcessingException e) {
    //             log.error("Erreur de parsing JSON", e);
    //             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur de parsing JSON");
    //         }
    //     } else {
    //         log.warn("Réponse inattendue: {}", response.getBody());
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Réponse inattendue du serveur");
    //     }
        
    // }

}
