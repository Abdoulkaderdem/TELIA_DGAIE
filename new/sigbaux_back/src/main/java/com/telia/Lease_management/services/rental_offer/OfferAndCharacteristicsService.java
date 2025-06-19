package com.telia.Lease_management.services.rental_offer;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.telia.Lease_management.dto.requests.OfferAndCharacteristicsDto;
import com.telia.Lease_management.dto.requests.RentalCharacteristicsDto;
import com.telia.Lease_management.dto.responses.OfferAndCharacteristicsResponse;
import com.telia.Lease_management.entity.rental_offer.Building;
import com.telia.Lease_management.entity.rental_offer.OfferAndCharacteristics;
import com.telia.Lease_management.entity.rental_offer.RentalCharacteristics;
import com.telia.Lease_management.entity.rental_offer.TypeOfferAndCharacteristics;
import com.telia.Lease_management.repository.rental_offer.OfferAndCharacteristicsRepository;
import com.telia.Lease_management.repository.rental_offer.RentalCharacteristicsRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class OfferAndCharacteristicsService {

    private OfferAndCharacteristicsRepository offerAndCharacteristicsRepository;
    private RentalCharacteristicsRepository rentalCharacteristicsRepository;
    

    public OfferAndCharacteristicsResponse  createOfferAndCharacteristics(OfferAndCharacteristicsDto dto) {
        OfferAndCharacteristics entity = OfferAndCharacteristicsDto.toEntity(dto);
        entity = offerAndCharacteristicsRepository.save(entity);
        return OfferAndCharacteristicsResponse.fromEntity(entity);
    }

    public List<OfferAndCharacteristicsResponse> getAllOfferAndCharacteristics() {
        return offerAndCharacteristicsRepository.findAll().stream()
                .map(OfferAndCharacteristicsResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public OfferAndCharacteristicsResponse getOfferAndCharacteristicsById(Long id) {
        OfferAndCharacteristics entity = offerAndCharacteristicsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("OfferAndCharacteristics not found"));
                return OfferAndCharacteristicsResponse.fromEntity(entity);
    }

    
    @Transactional
    public OfferAndCharacteristicsResponse updateOfferAndCharacteristics(Long id, OfferAndCharacteristicsDto dto) {
        OfferAndCharacteristics existingEntity = offerAndCharacteristicsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("OfferAndCharacteristics not found"));

        existingEntity.setValues(dto.getValues());
        if (dto.getCharacteristics() != null) {
            RentalCharacteristics characteristics = rentalCharacteristicsRepository.findById(dto.getCharacteristics().getId())
                    .orElseThrow(() -> new IllegalArgumentException("RentalCharacteristics not found"));
            existingEntity.setCharacteristics(characteristics);
        }
        // if (dto.getBuilding() != null) {
        //     Building building = buildingRepository.findById(dto.getBuilding().getId())
        //             .orElseThrow(() -> new IllegalArgumentException("Building not found"));
        //     existingEntity.setBuilding(building);
        // }

        existingEntity = offerAndCharacteristicsRepository.save(existingEntity);
        return OfferAndCharacteristicsResponse.fromEntity(existingEntity);
    }


    // private OfferAndCharacteristicsResponse toResponse(OfferAndCharacteristics entity) {
    //     Long buildingId = (entity.getBuilding() != null) ? entity.getBuilding().getId() : null;

    //     return new OfferAndCharacteristicsResponse(
    //             entity.getId(),
    //             entity.getValues(),
    //             // entity.getType(),
    //             RentalCharacteristicsDto.fromEntityRentalCharacteristics(entity.getCharacteristics()),
    //             buildingId
    //     );
    // }

    public List<OfferAndCharacteristics> getOffersByBuilding(Building building) {
        return offerAndCharacteristicsRepository.findByBuildingAndType(building, TypeOfferAndCharacteristics.OFFER);
    }

    public List<OfferAndCharacteristics> getInspectionsByBuilding(Building building) {
        return offerAndCharacteristicsRepository.findByBuildingAndType(building, TypeOfferAndCharacteristics.INSPECTION);
    }
    
}
