package com.telia.Lease_management.controllers.implementApi;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import com.telia.Lease_management.controllers.interfaceApi.AuthenticationApi;
import com.telia.Lease_management.dto.requests.AuthentificationDTO;
import com.telia.Lease_management.dto.requests.UserDto;
import com.telia.Lease_management.dto.responses.AuthResponse;
import com.telia.Lease_management.dto.responses.EntityResponse;
import com.telia.Lease_management.dto.responses.RefreshTokenRequest;
import com.telia.Lease_management.entity.Users;
import com.telia.Lease_management.entity.common.RoleType;
import com.telia.Lease_management.entity.common.TypeUser;
import com.telia.Lease_management.services.UsersService;
import com.telia.Lease_management.utils.AppConfig;
import com.telia.Lease_management.utils.NotificationService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Controller
@Slf4j
public class AuthenticationController implements AuthenticationApi{
    
    // private AuthenticationManager authenticationManager;
    // private JwtService jwtService;
    private UsersService usersService;
    private AppConfig appConfig;

    //NotificationService notificationService;

    @Override
    public  ResponseEntity<EntityResponse<Map<String, Object>>>  getProfile(Jwt jwt) {

        Map<String, Object> data = new HashMap<>();
        data.put("firstName", jwt.getClaimAsString("given_name"));
        data.put("lastName", jwt.getClaimAsString("family_name"));
        data.put("email", jwt.getClaimAsString("email"));
        data.put("actif", true); 
        data.put("enabled", jwt.getClaimAsBoolean("email_verified"));
        data.put("username", jwt.getClaimAsString("preferred_username"));

        Collection<GrantedAuthority> listAuthorities= null;
        Collection<String> resourceRoles= null;
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        Map<String, Object> resource = (Map<String, Object>) resourceAccess.get(appConfig.getResourceId());

        if (resource == null) {
            resource= null;
        }else{
             resourceRoles = (Collection<String>) resource.get("roles");
             listAuthorities= resourceRoles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
        }

        data.put("role", resourceRoles);
        data.put("authorities", listAuthorities);

        System.out.println("Le Principal: " + data);

        // Créer la réponse avec EntityResponse
        EntityResponse<Map<String, Object>> response = new EntityResponse<>(
            200,
            "User profile retrieved successfully",
            data
        );

        return ResponseEntity.ok(response);
    }


    @Override
    public  ResponseEntity<?> connexion(AuthentificationDTO authDto) {
        String username = authDto.getUsername();
        String password = authDto.getPassword();

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", appConfig.getResourceId());
        body.add("username", username);
        body.add("password", password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    appConfig.getTokenKeycloakUrl(),
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid credentials or error during authentication.");
        }

    }


    @Override
    public ResponseEntity<?> refreshToken(RefreshTokenRequest refreshTokenRequest, Jwt jwt) {
        String refresh_token = refreshTokenRequest.getRefresh_token();  // Extraire le refresh_token
        log.info("********** refresh_token: {}", refresh_token);

       if (refresh_token == null || refresh_token.isEmpty()) {
        return ResponseEntity.badRequest().body("Refresh token is empty");
    }

        String username = jwt.getClaimAsString("preferred_username");

        log.info("********** username: {}", username);

        if (username == null) {
            return ResponseEntity.status(401).body("User not authenticated");
        }

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", appConfig.getResourceId());
        body.add("username", username);
        body.add("refresh_token", refresh_token);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    appConfig.getTokenKeycloakUrl(),
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid credentials or error during authentication.");
        }
    }

    
    
}
