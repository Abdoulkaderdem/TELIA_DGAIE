package com.telia.Lease_management.services.CNOI;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.telia.Lease_management.entity.CNOI.RentPrice;
import com.telia.Lease_management.entity.common.Locality;
import com.telia.Lease_management.entity.common.TypeBuilding;
import com.telia.Lease_management.entity.common.Zone;
import com.telia.Lease_management.entity.rental_offer.Building;
import com.telia.Lease_management.entity.rental_offer.OfferAndCharacteristics;
import com.telia.Lease_management.entity.rental_offer.TypeOfferAndCharacteristics;
import com.telia.Lease_management.repository.CNOI.RentPriceRepository;
import com.telia.Lease_management.repository.rental_offer.BuildingRepository;
import com.telia.Lease_management.repository.rental_offer.OfferAndCharacteristicsRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class RentPriceService {

    private RentPriceRepository rentPriceRepository;
    private OfferAndCharacteristicsRepository oAndCharacteristicsRepo;
    private BuildingRepository buildingRepo;

    public RentPrice saveRentPrice(RentPrice rentPrice) {
        return rentPriceRepository.save(rentPrice);
    }
    
    public List<RentPrice> getAllRentPrices() {
        return rentPriceRepository.findAll();
    }
    
    public RentPrice getRentPrice(Long id) {
        return rentPriceRepository.findById(id).orElse(null);
    }
    

    public RentPrice updateRentPrice(Long id, RentPrice rentPrice) {
        RentPrice rentPriceToUpdate = rentPriceRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Rental Price not found with id: " + rentPrice.getId()));

        rentPriceToUpdate.setLocality(rentPrice.getLocality());
        rentPriceToUpdate.setZone(rentPrice.getZone());
        rentPriceToUpdate.setTypeBuilding(rentPrice.getTypeBuilding());
        rentPriceToUpdate.setPricePerSquareMeter(rentPrice.getPricePerSquareMeter());

        return  rentPriceRepository.save(rentPriceToUpdate);  

    }

    public void deleteRentPrice(Long id) {
        rentPriceRepository.deleteById(id);
    }
    
    public Optional<RentPrice> findByZoneAndLocalityAndTypeBuilding(Zone zone, Locality locality, TypeBuilding typeBuilding) {
        return rentPriceRepository.findByZoneAndLocalityAndTypeBuilding(zone, locality, typeBuilding);
    }

        
    public double calculateEstimatedRent(Long id) {
        //Prix loyer estimatif = (Prix de base par m²/m³) * (Surface/Volume du bâtiment) + Somme des coûts additionnels

        Building buildingToEstimate = buildingRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Building not found with id: " + id));

        // if (buildingToEstimate)
        double basePrice = getBasePrice(buildingToEstimate); //get Price Per SquareMeter 
        double buildingSurface = getBuildingSurface(buildingToEstimate); //get Building Surface
        double additionalCosts = getAdditionalCosts(buildingToEstimate); //get Additional Costs
        // log.info("basePrice: {}", basePrice);
        // log.info("buildingSurface: {}", buildingSurface);
        // log.info("additionalCosts: {}", additionalCosts);
        return (basePrice * buildingSurface) + additionalCosts;
    }
    

    private double getBasePrice(Building building) {
        Optional<RentPrice> optionalRentPrice = rentPriceRepository.findByZoneAndLocalityAndTypeBuilding(
            building.getZone(), building.getLocality(), building.getTypeBuilding()
        );

        if (optionalRentPrice.isPresent()) {
            return optionalRentPrice.get().getPricePerSquareMeter();
        } else {
            throw new RuntimeException("Rent price not found for the specified locality, zone, and type building.");
        }
    }


    private double getBuildingSurface(Building building) {
        // Suppose that buildingValue stores the surface area of the building as a String.
        String buildingValue = building.getBuildingArea();

        try {
            // Convert the value to double.
            return Double.parseDouble(buildingValue);
        } catch (NumberFormatException e) {
            // Handle the case where buildingValue is not a valid double.
            throw new NumberFormatException("Building value is not a valid double: " + building.getBuildingValue());
        }
    }


    private double getAdditionalCosts(Building building) {
        //Retreive All building characteristics add by inspection
        List<OfferAndCharacteristics> listCharacteristics= oAndCharacteristicsRepo.findByBuildingAndType(building, TypeOfferAndCharacteristics.INSPECTION);
                
        // List<OfferAndCharacteristics> listCharacteristics = oAndCharacteristicsRepo.findByBuilding(building);
    
        // Vérifie si la liste est vide
        if (listCharacteristics.isEmpty()) {
            return 0.0;
        }   
        // Calcul des coûts additionnels en utilisant des streams
        double additionalCosts = listCharacteristics.stream()
                .mapToDouble(this::calculateAdditionalCost)
                .sum();
    
        return additionalCosts;
    }
    
    private double calculateAdditionalCost(OfferAndCharacteristics offer) {
        if (offer.getId() != null) {
            // String characteristic = offer.getCharacteristics().getLibLong();
            double costByUnit = offer.getCharacteristics().getUnitPrice();
            return costByUnit * offer.getValues();
        } else {
            // Handle the case where the OfferAndCharacteristics is not found in the database
            throw new RuntimeException("Offer And Characteristics cost not found for characteristic: " + offer.toString());
        }

        // switch (offer.getCharacteristics().getLibCourt()) {
        //     case "VENTILATOR":
        //         return 1000 * offer.getValues();
        //     case "AIR_CONDITIONER":
        //         return 6000 * offer.getValues();
        //     case "SPLIT_SYSTEM":
        //         return 8000 * offer.getValues();
        //     case "BATHTUB":
        //         return 2000 * offer.getValues();
        //     case "WATER_HEATER":
        //         return 3000 * offer.getValues();
        //     case "WATER_TOWER":
        //         return 8000 * offer.getValues();
        //     case "SWIMMING_POOL":
        //         return 18000 * offer.getValues();
        //     case "ELEVATOR":
        //         return 75000 * offer.getValues();
        //     case "GENERATOR":
        //         return 15000 * offer.getValues();
        //     case "SOLAR_SYSTEM":
        //         return 15000 * offer.getValues();
        //     case "INTERNET_CABLE":
        //         return 0.01 * offer.getValues();
        //     case "PAVED_AREA":
        //         return 250 * offer.getValues();
        //     case "TILED_AREA":
        //         return 200 * offer.getValues();
        //     default:
        //         return 0;
        // }
    }


}
