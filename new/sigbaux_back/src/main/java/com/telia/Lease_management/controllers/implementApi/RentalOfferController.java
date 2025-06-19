package com.telia.Lease_management.controllers.implementApi;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.telia.Lease_management.controllers.interfaceApi.RentalOfferApi;
import com.telia.Lease_management.dto.requests.BuildingDto;
import com.telia.Lease_management.dto.requests.RentalOfferDto;
import com.telia.Lease_management.dto.requests.RentalRequestDto;
import com.telia.Lease_management.dto.requests.UserDto;
import com.telia.Lease_management.dto.responses.AuthResponse;
import com.telia.Lease_management.dto.responses.BuildingResponse;
import com.telia.Lease_management.dto.responses.EntityResponse;
import com.telia.Lease_management.dto.responses.RentalOfferResponse;
import com.telia.Lease_management.entity.common.RentalStatus;
import com.telia.Lease_management.entity.rental_request.RentalRequest;
import com.telia.Lease_management.services.rental_offer.RentalOfferService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
public class RentalOfferController implements RentalOfferApi{

    private RentalOfferService rentalOfferService;

    @Override
    public ResponseEntity<RentalOfferResponse> createOffer(RentalOfferDto rentalOfferDto) {
        
        try {

            log.info("*********************** Received request: {}", rentalOfferDto); // Log de la requête reçue
            return ResponseEntity.status(HttpStatus.CREATED).body(rentalOfferService.createRentalOffer(rentalOfferDto));
            // return null;
        } catch (Exception e) {
            log.error("Error while processing request", e); // Log de l'erreur
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

    @Override
    public ResponseEntity<List<RentalOfferResponse>> findAllOffer() {
        List<RentalOfferResponse> rentalOffers = rentalOfferService.getAllRentalOffers();
        return ResponseEntity.ok(rentalOffers);
    }

    @Override
    public ResponseEntity<RentalOfferResponse> findOfferById(Long id) {
        RentalOfferResponse rentalOffer = rentalOfferService.getRentalOfferById(id);
        return rentalOffer != null ? ResponseEntity.ok(rentalOffer) : ResponseEntity.notFound().build();
    }


    @Override
    public ResponseEntity<RentalOfferResponse> updateRentalOffer(Long id, RentalOfferDto rentalOfferDto) {
        //rentalOfferDto.setId(id);
        RentalOfferResponse updatedRentalOffer = rentalOfferService.updateRentalOffer(id, rentalOfferDto);
        return ResponseEntity.ok(updatedRentalOffer);
    }


    @Override
    public ResponseEntity<Void> changeRentalOfferStatus(Long id, RentalStatus status) {
        rentalOfferService.changeStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<RentalOfferResponse>> getRentalOffersByStatus(RentalStatus status) {
        List<RentalOfferResponse> rentalOffers = rentalOfferService.getRentalOffersByStatus(status);
        return ResponseEntity.ok(rentalOffers);
    }

    @Override
    public ResponseEntity<Long> countAllRentalRequests() {
        long count = rentalOfferService.countAllRentalOffer();
        return ResponseEntity.ok(count);
    }

    
    
}
