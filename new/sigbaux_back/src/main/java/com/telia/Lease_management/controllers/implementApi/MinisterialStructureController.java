package com.telia.Lease_management.controllers.implementApi;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.telia.Lease_management.controllers.interfaceApi.MinisterialStructureApi;
import com.telia.Lease_management.dto.requests.MinisterialStructureDto;
import com.telia.Lease_management.dto.requests.MinistryDto;
import com.telia.Lease_management.dto.requests.RentalRequestDto;
import com.telia.Lease_management.dto.responses.AuthResponse;
import com.telia.Lease_management.dto.responses.StructureWithRequestRentalResponse;
import com.telia.Lease_management.entity.MinisterialStructure;
import com.telia.Lease_management.entity.Ministry;
import com.telia.Lease_management.services.MinisterialStructureService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@RestController
@Slf4j
public class MinisterialStructureController implements MinisterialStructureApi{

    private MinisterialStructureService structureService;

    @Override
    public ResponseEntity<AuthResponse> createStructure(MinisterialStructureDto dto) {
        try {
            // log.info("Inscription");
            
            if (this.structureService.createStructure(dto)){
                return ResponseEntity.ok(new AuthResponse(HttpStatus.OK.value(), "Structure créée avec succès!"));
            } else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(HttpStatus.BAD_REQUEST.value(), "Formulaire invalide"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(HttpStatus.UNAUTHORIZED.value(), "Une erreur est survenue / Données non conformes !"));
        }
    }

    @Override
    public List<MinisterialStructureDto> findAllStructure() {
        return structureService.findAll();
    }

    @Override
    public MinisterialStructureDto findStructureById(Long id) {
        return structureService.findById(id);
    }

    @Override
    public ResponseEntity<MinisterialStructureDto> updateMinisterialStructure(Long id, MinisterialStructureDto structureDto) {
        try {
            MinisterialStructureDto updatedStructure = structureService.updateMinisterialStructure(id, structureDto);
            return ResponseEntity.ok(updatedStructure);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

        
    @Override
    @PutMapping("/{id}/activate")
    public ResponseEntity<MinisterialStructureDto> activateStructure(@PathVariable Long id) {
        MinisterialStructureDto updatedMinistry = structureService.setActivationStatus(id, true);
        return ResponseEntity.ok(updatedMinistry);
    }
    
    @Override
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<MinisterialStructureDto> deactivateStructure(@PathVariable Long id) {
        MinisterialStructureDto updatedMinistry = structureService.setActivationStatus(id, false);
        return ResponseEntity.ok(updatedMinistry);
    }

    @Override
    public ResponseEntity<List<RentalRequestDto>> getValidatedRentalRequests(Long structureId) {
        List<RentalRequestDto> rentalRequests = structureService.getValidatedRentalRequests(structureId);
        return ResponseEntity.ok(rentalRequests);
    }

    @Override
    public ResponseEntity<List<RentalRequestDto>> getNewRentalRequests(Long structureId) {
        List<RentalRequestDto> rentalRequests = structureService.getNewRentalRequests(structureId);
        return ResponseEntity.ok(rentalRequests);
    }

    @Override
    public ResponseEntity<List<RentalRequestDto>> getSendedRentalRequests(Long structureId) {
        List<RentalRequestDto> rentalRequests = structureService.getSendedRentalRequests(structureId);
        return ResponseEntity.ok(rentalRequests);
    }

    @Override
    public ResponseEntity<List<RentalRequestDto>> getComplementRentalRequests(Long structureId) {
        List<RentalRequestDto> rentalRequests = structureService.getComplementRentalRequests(structureId);
        return ResponseEntity.ok(rentalRequests);
    }

    @Override
    public ResponseEntity<List<RentalRequestDto>> getApprovalRentalRequests(Long structureId) {
        List<RentalRequestDto> rentalRequests = structureService.getApprovalRentalRequests(structureId);
        return ResponseEntity.ok(rentalRequests);
    }

    @Override
    public ResponseEntity<List<RentalRequestDto>> getRejectedRentalRequests(Long structureId) {
        List<RentalRequestDto> rentalRequests = structureService.getRejectedRentalRequests(structureId);
        return ResponseEntity.ok(rentalRequests);
    }

    @Override
    public ResponseEntity<List<RentalRequestDto>> getHeldRentalRequests(Long structureId) {
        List<RentalRequestDto> rentalRequests = structureService.getHeldRentalRequests(structureId);
        return ResponseEntity.ok(rentalRequests);
    }
    
    
}
