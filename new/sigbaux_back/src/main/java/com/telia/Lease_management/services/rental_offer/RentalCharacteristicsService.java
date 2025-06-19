package com.telia.Lease_management.services.rental_offer;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.telia.Lease_management.dto.requests.RentalCharacteristicsDto;
import com.telia.Lease_management.entity.rental_offer.RentalCharacteristics;
import com.telia.Lease_management.repository.rental_offer.RentalCharacteristicsRepository;
import com.telia.Lease_management.utils.ValidateForms;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class RentalCharacteristicsService {
    
    private RentalCharacteristicsRepository characteristicsRepo;

    
    public RentalCharacteristicsDto createRentalCharacteristics(RentalCharacteristicsDto characteristicsDto) throws Exception{
        
        //Check the forms
        List<String> errors= ValidateForms.validateRentalCharacteristics(characteristicsDto);
        if (!errors.isEmpty()){
            log.error("Rental Characteristics  is not valid {}:", characteristicsDto);
            throw new Exception("Rental Characteristics is not valid !");
        }
        
        //Save Rental Characteristics 
        RentalCharacteristics rentalCharacteristics = characteristicsRepo.save(RentalCharacteristicsDto.toEntityRentalCharacteristics(characteristicsDto));

        return RentalCharacteristicsDto.fromEntityRentalCharacteristics(rentalCharacteristics);

    }

    
    public List<RentalCharacteristicsDto> getAllRentalCharacteristics() {
        // Retrieving the list of all Rental Request  from BDD
        List<RentalCharacteristics> characteristicsRentals = characteristicsRepo.findAll();
        
        List<RentalCharacteristicsDto> characteristicsRentalList = characteristicsRentals.stream()
                .map(characteristicsRental -> RentalCharacteristicsDto.fromEntityRentalCharacteristics(characteristicsRental))
                .collect(Collectors.toList());

        return characteristicsRentalList;
    }


    public RentalCharacteristicsDto getRentalCharacteristicsById(Long id) {
        if (id == null) {
            log.error("Rental Characteristics ID is null");
            return null;
        }
 
        Optional<RentalCharacteristics> optionalcharacteristicsRental= characteristicsRepo.findById(id);

        if (optionalcharacteristicsRental.isPresent()) {
            return RentalCharacteristicsDto.fromEntityRentalCharacteristics(optionalcharacteristicsRental.get());
        } else {
            throw new EntityNotFoundException(
                "No Rental Characteristics with the ID = " + id + " is in the DB"
            );
        }
    }

    
    @Transactional
    public RentalCharacteristicsDto updateRentalCharacteristics(Long id, RentalCharacteristicsDto updatedRentalCharacteristicsDto) {

        RentalCharacteristics existRentalCharacteristics = characteristicsRepo.findById(id)
                 .orElseThrow(() -> new IllegalArgumentException("Rental Characteristics with id " + id + " not found"));
        
        existRentalCharacteristics.setLibCourt(updatedRentalCharacteristicsDto.getLibCourt());
        existRentalCharacteristics.setLibLong(updatedRentalCharacteristicsDto.getLibLong());
        existRentalCharacteristics.setUnitPrice(updatedRentalCharacteristicsDto.getUnitPrice());
        // existRentalCharacteristics.setType_car(updatedRentalCharacteristicsDto.getType_car());

        return RentalCharacteristicsDto.fromEntityRentalCharacteristics(existRentalCharacteristics);

    }


    public void deleteRentalCharacteristics(Long id) {
        characteristicsRepo.deleteById(id);
    }


}
