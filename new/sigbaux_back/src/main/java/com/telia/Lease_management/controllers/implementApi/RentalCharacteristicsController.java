package com.telia.Lease_management.controllers.implementApi;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.telia.Lease_management.controllers.interfaceApi.RentalCharacteristicsApi;
import com.telia.Lease_management.dto.requests.RentalCharacteristicsDto;
import com.telia.Lease_management.entity.rental_offer.RentalCharacteristics;
import com.telia.Lease_management.services.rental_offer.RentalCharacteristicsService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
public class RentalCharacteristicsController implements RentalCharacteristicsApi{
    
    private RentalCharacteristicsService rentalCharacteristicsService;

    @Override
    public ResponseEntity<RentalCharacteristicsDto> createRentalCharacteristics(
            RentalCharacteristicsDto rentalCharacteristicsDto) {

                try {
                    RentalCharacteristicsDto savedRentalCharacteristicsDto = rentalCharacteristicsService.createRentalCharacteristics(rentalCharacteristicsDto);
                    return ResponseEntity.ok(savedRentalCharacteristicsDto);
                } catch (Exception e) {
                    return ResponseEntity.badRequest().body(null);  // Or handle the error in a better way
                }
    }

    @Override
    public ResponseEntity<List<RentalCharacteristicsDto>> getAllRentalCharacteristics() {
        List<RentalCharacteristicsDto> savedRentalCharacteristicsDto = rentalCharacteristicsService.getAllRentalCharacteristics();
        return ResponseEntity.ok(savedRentalCharacteristicsDto);
    }

    @Override
    public ResponseEntity<RentalCharacteristicsDto> getRentalCharacteristicsById(Long id) {
        RentalCharacteristicsDto rentalCharacteristicsDto= rentalCharacteristicsService.getRentalCharacteristicsById(id);
        return ResponseEntity.ok(rentalCharacteristicsDto);
    }

    @Override
    public ResponseEntity<RentalCharacteristicsDto> updateRentalCharacteristics(Long id,
            RentalCharacteristicsDto updatedRentalCharacteristics) {

                RentalCharacteristicsDto updateRentalCharacteristicsDto = rentalCharacteristicsService.updateRentalCharacteristics(id, updatedRentalCharacteristics);
                return ResponseEntity.ok(updateRentalCharacteristicsDto);
    }

    @Override
    public ResponseEntity<Void> deleteRentalCharacteristics(Long id) {
        rentalCharacteristicsService.deleteRentalCharacteristics(id);
        return ResponseEntity.noContent().build();
    }
    
}
