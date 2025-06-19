package com.telia.Lease_management.security;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import com.telia.Lease_management.utils.AppConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtConverter implements Converter<Jwt, AbstractAuthenticationToken>{

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    // private JwtConverterProperties jwtConverterProperties;
    private AppConfig appConfig;

    public JwtConverter(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    public String getPrincipalClaimName() {
        if (appConfig == null) {
            throw new IllegalStateException("appConfig is not initialized");
        }
        return appConfig.getPrincipleAttribute();
    }


    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        Collection<GrantedAuthority> authorities= Stream.concat(
            jwtGrantedAuthoritiesConverter.convert(jwt).stream(), 
            extractRessourceRoles(jwt).stream()
        ).collect(Collectors.toSet());
            
        return new JwtAuthenticationToken(jwt, authorities, getPrincipalClaimName(jwt));
    }
     
            
    private String  getPrincipalClaimName(Jwt jwt) {
        String claimName= JwtClaimNames.SUB;

        if (appConfig.getPrincipleAttribute() != null){
            claimName = appConfig.getPrincipleAttribute();
        }
        
        return jwt.getClaim(claimName);

    }

           
    private Collection<GrantedAuthority> extractRessourceRoles(Jwt jwt) {
        log.info("****** Enter ****************");
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
      
        if (resourceAccess == null) {
            return Set.of();
        }
        
        // Extraction des rôles pour l'application spécifiée
        Map<String, Object> resource = (Map<String, Object>) resourceAccess.get(appConfig.getResourceId());
        if (resource == null) {
            return Set.of();
        }
        
        Collection<String> resourceRoles = (Collection<String>) resource.get("roles");
        if (resourceRoles == null) {
            return Set.of();
        }
        
        log.info("****** Roles for resource: {}", resourceRoles);
        
        // Conversion en GrantedAuthority avec préfixe "ROLE_"
        return resourceRoles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());

    }

    
}
