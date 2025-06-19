package com.telia.Lease_management.controllers.interfaceApi;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.telia.Lease_management.entity.CNOI.RentPrice;
import com.telia.Lease_management.entity.rental_offer.Building;

@CrossOrigin
@RequestMapping("/rentPrices")
public interface RentPriceApi {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RentPrice> createRentPrice(@RequestBody RentPrice rentPrice);

    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RentPrice>> getAllRentPrices();

    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RentPrice> getRentPriceById(@PathVariable Long id);

    @PutMapping(value="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RentPrice> updateRentPrice(@PathVariable Long id, @RequestBody RentPrice rentPrice);

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRentPrice(@PathVariable Long id);

    @PreAuthorize("hasAnyAuthority('ROLE_CNOI', 'ROLE_DAIE', 'ROLE_SUPER')")
    @GetMapping("/{id}/estimate-rent")
    public ResponseEntity<Double> estimateRent(@PathVariable Long id);
    
}
