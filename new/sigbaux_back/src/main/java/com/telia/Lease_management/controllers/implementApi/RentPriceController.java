package com.telia.Lease_management.controllers.implementApi;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.telia.Lease_management.controllers.interfaceApi.RentPriceApi;
import com.telia.Lease_management.entity.CNOI.RentPrice;
import com.telia.Lease_management.entity.rental_offer.Building;
import com.telia.Lease_management.services.CNOI.RentPriceService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
public class RentPriceController implements RentPriceApi{

     private RentPriceService rentPriceService;

    @Override
    public ResponseEntity<RentPrice> createRentPrice(RentPrice rentPrice) {
        return ResponseEntity.ok(rentPriceService.saveRentPrice(rentPrice));
    }

    @Override
    public ResponseEntity<List<RentPrice>> getAllRentPrices() {
        return ResponseEntity.ok(rentPriceService.getAllRentPrices());
    }

    @Override
    public ResponseEntity<RentPrice> getRentPriceById(Long id) {
        RentPrice rentPriceFind=  rentPriceService.getRentPrice(id);
        if (rentPriceFind.getId()!= null){
            return ResponseEntity.ok(rentPriceFind);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @Override
    public ResponseEntity<RentPrice> updateRentPrice(Long id, RentPrice rentPrice) {
        RentPrice rentPriceUpdated= rentPriceService.updateRentPrice(id, rentPrice);
        if (rentPriceUpdated.getId()!= null){
            return ResponseEntity.ok(rentPriceUpdated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteRentPrice(Long id) {
        rentPriceService.deleteRentPrice(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Double> estimateRent(Long id) {
        double estimatedRent = rentPriceService.calculateEstimatedRent(id);
        return ResponseEntity.ok(estimatedRent);
    }




    
}
