package com.telia.Lease_management.controllers.interfaceApi;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.telia.Lease_management.dto.requests.BuildingDto;
import com.telia.Lease_management.dto.requests.RentalOfferDto;
import com.telia.Lease_management.dto.responses.RentalOfferResponse;
import com.telia.Lease_management.entity.common.RentalStatus;


@CrossOrigin
@RequestMapping("/rental")
//@PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_LANDLORD','ROLE_SUPER')")
public interface RentalOfferApi {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<RentalOfferResponse> createOffer(@RequestBody RentalOfferDto dto);

    // @ResponseStatus(HttpStatus.CREATED)
    // @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    // ResponseEntity<RentalOfferResponse> createOffer(@ModelAttribute RentalOfferDto dto, @RequestParam(value = "file", required = false) MultipartFile offerFile);
    
    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<RentalOfferResponse>> findAllOffer();

    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping(value = "/{idOffer}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<RentalOfferResponse> findOfferById(@PathVariable("idOffer") Long id);

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/{id}")
    public ResponseEntity<RentalOfferResponse> updateRentalOffer(@PathVariable Long id, @RequestBody RentalOfferDto rentalOfferDto);

    @PatchMapping("/{id}/statusessssssssss")
    public ResponseEntity<Void> changeRentalOfferStatus(@PathVariable Long id, @RequestParam RentalStatus status);


    @GetMapping("/by-status/{status}")
    public ResponseEntity<List<RentalOfferResponse>> getRentalOffersByStatus(@PathVariable RentalStatus status);

    @GetMapping("/count")
    public ResponseEntity<Long> countAllRentalRequests();
}
