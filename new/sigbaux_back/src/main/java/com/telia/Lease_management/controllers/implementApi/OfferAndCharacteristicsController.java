package com.telia.Lease_management.controllers.implementApi;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.telia.Lease_management.controllers.interfaceApi.OfferAndCharacteristicsApi;
import com.telia.Lease_management.dto.requests.OfferAndCharacteristicsDto;
import com.telia.Lease_management.dto.responses.OfferAndCharacteristicsResponse;
import com.telia.Lease_management.services.rental_offer.OfferAndCharacteristicsService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
public class OfferAndCharacteristicsController implements OfferAndCharacteristicsApi{

     private OfferAndCharacteristicsService offerAndCharacteristicsService;

    @Override
    public ResponseEntity<OfferAndCharacteristicsResponse> create(@RequestBody OfferAndCharacteristicsDto dto) {
        OfferAndCharacteristicsResponse createdResponse = offerAndCharacteristicsService.createOfferAndCharacteristics(dto);
        return new ResponseEntity<>(createdResponse, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<OfferAndCharacteristicsResponse>> getAll() {
        List<OfferAndCharacteristicsResponse> responses = offerAndCharacteristicsService.getAllOfferAndCharacteristics();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @Override
   public ResponseEntity<OfferAndCharacteristicsResponse> getById(@PathVariable Long id) {
        OfferAndCharacteristicsResponse response = offerAndCharacteristicsService.getOfferAndCharacteristicsById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<OfferAndCharacteristicsResponse> update(@PathVariable Long id, @RequestBody OfferAndCharacteristicsDto dto) {
        OfferAndCharacteristicsResponse updatedResponse = offerAndCharacteristicsService.updateOfferAndCharacteristics(id, dto);
        return new ResponseEntity<>(updatedResponse, HttpStatus.OK);
    }

     
    
}
