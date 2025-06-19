package com.telia.Lease_management.controllers.interfaceApi;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.telia.Lease_management.dto.ifuDto.IfuResponse;
import com.telia.Lease_management.dto.requests.LandLordDto;
import com.telia.Lease_management.entity.common.RentalStatus;
import com.telia.Lease_management.entity.rental_offer.LandLord;

import reactor.core.publisher.Mono;

@RequestMapping("/landlord")
@CrossOrigin
public interface LandLordApi {
    
    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_SUPER')")
    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LandLordDto>> getAllLandLords();

    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_LANDLORD', 'ROLE_SUPER')")
    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LandLordDto> getLandLordById(@PathVariable Long id);

    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_SUPER')")
    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping(value = "/{ifu}/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LandLordDto> getLandLordByIfu(@PathVariable String ifu);

    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_LANDLORD', 'ROLE_SUPER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LandLordDto> createLandLord(@RequestBody LandLordDto landLordDto);

    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_LANDLORD', 'ROLE_SUPER')")
    @PutMapping(value="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LandLordDto> updateLandLord(@PathVariable Long id, @RequestBody LandLordDto landLordDto);

    @PatchMapping("/{id}/statussssssss")
    public ResponseEntity<Void> changeLandLordStatus(@PathVariable Long id, @RequestParam RentalStatus status);

    // @PostMapping("/ifu/{numIfu}/check")
    // public ResponseEntity<?> verifierIfu(@PathVariable String numIfu) ;
    
    // @PostMapping("/ifu/{numIfu}/checking")
    // public ResponseEntity<?> verifierIfuV2(@PathVariable String numIfu) ;

    @PostMapping("/ifu/{numIfu}/checking")
    public Mono<ResponseEntity<IfuResponse>> verifierIfuV2(@PathVariable String numIfu); 


}
