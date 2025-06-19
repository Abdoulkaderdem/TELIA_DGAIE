package com.telia.Lease_management.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class ConfigurationSecuriteApplication {

    private final JwtConverter jwtAuthConverter;
    // private final BCryptPasswordEncoder bCryptPasswordEncoder;
    // private final JwtFilter jwtFilter;
    // private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // .csrf(csrf -> csrf.ignoringRequestMatchers("/auth/connexion"))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(
                        auth -> auth
                        .requestMatchers("/auth/connexion").permitAll()
                        .anyRequest().authenticated()) 
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)))
                // .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
                  
    }

        //Create an AuthenticationManager to manage all authentication, it use userDetailService to authenticate
        // @Bean
        // public AuthenticationManager authenticationManager (AuthenticationConfiguration authenticationConfiguration) throws Exception {
        //     return authenticationConfiguration.getAuthenticationManager();
        // }
    
        // Use to access in DBB and retrieve the account's informations
        // @Bean
        // public AuthenticationProvider authenticationProvider () {
        //     DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        //     daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        //     daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        //     return daoAuthenticationProvider;
        // }

    
    // public ConfigurationSecuriteApplication(
    //     BCryptPasswordEncoder bCryptPasswordEncoder, 
    //     JwtFilter jwtFilter,
    //     UserDetailsService userDetailsService) {
    //     this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    //     this.jwtFilter = jwtFilter;
    //     this.userDetailsService = userDetailsService;
    // }

  

    //     @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    //     return
    //             httpSecurity
    //                     .csrf(AbstractHttpConfigurer::disable)
    //                     .authorizeHttpRequests(
    //                             authorize ->
    //                                     authorize
    //                                             .requestMatchers("/auth/connexion").permitAll()
    //                                             // .requestMatchers("/user/inscription").permitAll()
    //                                             .anyRequest().authenticated()
    //                     )
    //                     .sessionManagement(httpSecuritySessionManagementConfigurer ->
    //                             httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS) //Because we want it to check the token on every request 

    //                     )
    //                     .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) //We want it, to use our filter before his own
    //                     .build();
    // }


}
