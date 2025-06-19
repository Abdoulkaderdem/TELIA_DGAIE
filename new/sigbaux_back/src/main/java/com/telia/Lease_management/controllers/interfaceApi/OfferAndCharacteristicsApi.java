package com.telia.Lease_management.controllers.interfaceApi;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.telia.Lease_management.dto.requests.OfferAndCharacteristicsDto;
import com.telia.Lease_management.dto.responses.OfferAndCharacteristicsResponse;

@CrossOrigin
@RequestMapping("/offerAndCharacteristics")
public interface OfferAndCharacteristicsApi {
    
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OfferAndCharacteristicsResponse> create(@RequestBody OfferAndCharacteristicsDto dto);

    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OfferAndCharacteristicsResponse>> getAll();

    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OfferAndCharacteristicsResponse> getById(@PathVariable Long id);

    @PutMapping(value="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OfferAndCharacteristicsResponse> update(@PathVariable Long id, @RequestBody OfferAndCharacteristicsDto dto);
}
