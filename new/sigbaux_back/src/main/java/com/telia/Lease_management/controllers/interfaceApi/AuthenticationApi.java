package com.telia.Lease_management.controllers.interfaceApi;

import java.util.Map;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.telia.Lease_management.dto.requests.AuthentificationDTO;
import com.telia.Lease_management.dto.requests.MinistryDto;
import com.telia.Lease_management.dto.requests.UserDto;
import com.telia.Lease_management.dto.responses.AuthResponse;
import com.telia.Lease_management.dto.responses.EntityResponse;
import com.telia.Lease_management.dto.responses.RefreshTokenRequest;
import com.telia.Lease_management.entity.Users;

@RequestMapping("/auth")
@CrossOrigin
public interface AuthenticationApi {
    
    @PostMapping(value = "/connexion", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<?> connexion(@RequestBody AuthentificationDTO authDto);

        
    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<EntityResponse<Map<String, Object>>> getProfile(@AuthenticationPrincipal Jwt principal);
        

    @PostMapping(value = "/refresh-token", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest, @AuthenticationPrincipal Jwt principal);
}
