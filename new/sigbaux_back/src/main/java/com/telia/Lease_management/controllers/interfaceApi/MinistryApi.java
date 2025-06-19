package com.telia.Lease_management.controllers.interfaceApi;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.telia.Lease_management.dto.responses.AuthResponse;
import com.telia.Lease_management.entity.Ministry;

@CrossOrigin
@PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_SUPER')")
@RequestMapping("/ministry")
public interface MinistryApi {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AuthResponse> createMinistry(@RequestBody MinistryDto dto);
    
    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    List<MinistryDto> findAllMinistry();

    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping(value = "/{idMinistry}", produces = MediaType.APPLICATION_JSON_VALUE)
    MinistryDto findMinistryById(@PathVariable("idMinistry") Long id);

     @PutMapping(value="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MinistryDto> updateMinistry(@PathVariable("id") Long id, @RequestBody MinistryDto ministryDto);

    @PutMapping(value="/{id}/activate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MinistryDto> activateMinistry(@PathVariable Long id);
    
    @PutMapping(value="/{id}/deactivate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MinistryDto> deactivateMinistry(@PathVariable Long id);
    
}
