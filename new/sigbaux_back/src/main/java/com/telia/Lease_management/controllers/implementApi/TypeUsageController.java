package com.telia.Lease_management.controllers.implementApi;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.telia.Lease_management.controllers.interfaceApi.TypeUsageApi;
import com.telia.Lease_management.dto.requests.TypeUsageDto;
import com.telia.Lease_management.services.rental_request.TypeUsageService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@RestController
@Slf4j
public class TypeUsageController implements TypeUsageApi{

     private TypeUsageService typeUsageService;

    @Override
    public ResponseEntity<TypeUsageDto> createTypeUsage(TypeUsageDto typeUsageDto) {
        try {
            //log.info("*********************** Received request: {}", dto); // Log de la requête reçue
            return ResponseEntity.status(HttpStatus.CREATED).body(typeUsageService.createTypeUsage(typeUsageDto));
        } catch (Exception e) {
            log.error("Error while processing request", e); // Log de l'erreur
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Override
    public ResponseEntity<List<TypeUsageDto>> getAllTypeUsages() {
        return ResponseEntity.ok(typeUsageService.getAllTypeUsages());

    }

    @Override
    public ResponseEntity<TypeUsageDto> getTypeUsageById(Long id) {
       // return typeUsageService.getTypeUsageById(id);
        return ResponseEntity.ok(typeUsageService.getTypeUsageById(id));
    }

    @Override
    public ResponseEntity<TypeUsageDto> updateTypeUsage(Long id, TypeUsageDto typeUsageDto) {
        //typeUsageService.updateTypeUsage(id, typeUsageDto);
        return ResponseEntity.ok(typeUsageService.updateTypeUsage(id, typeUsageDto));
    }

    @Override
    public ResponseEntity<Void> deleteTypeUsage(Long id) {
        typeUsageService.deleteTypeUsage(id);
        return ResponseEntity.noContent().build();
    }


    
}
