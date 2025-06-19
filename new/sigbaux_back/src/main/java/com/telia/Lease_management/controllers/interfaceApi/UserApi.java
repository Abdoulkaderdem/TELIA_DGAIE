package com.telia.Lease_management.controllers.interfaceApi;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.telia.Lease_management.dto.requests.MinistryDto;
import com.telia.Lease_management.dto.requests.UserDto;
import com.telia.Lease_management.dto.responses.AuthResponse;
import com.telia.Lease_management.dto.responses.EntityResponse;

@RequestMapping("/user")
@CrossOrigin
public interface UserApi {
    
    @PreAuthorize("hasAnyAuthority('ROLE_DSI', 'ROLE_ADMIN','ROLE_SUPER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/inscription", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AuthResponse> saveUser(@RequestBody UserDto dto);
    
    @PreAuthorize("hasAnyAuthority('ROLE_DSI', 'ROLE_ADMIN','ROLE_SUPER')")
    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    List<UserDto> findAllUsers();

    @PreAuthorize("hasAnyAuthority('ROLE_DSI', 'ROLE_ADMIN','ROLE_SUPER')")
    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping(value = "/{idUser}", produces = MediaType.APPLICATION_JSON_VALUE)
    UserDto findUserById(@PathVariable("idUser") Long id);
   
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<EntityResponse<UserDto>> updateUserProfile(@RequestBody UserDto dto);

}
