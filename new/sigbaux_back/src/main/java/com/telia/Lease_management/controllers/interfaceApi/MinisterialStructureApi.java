package com.telia.Lease_management.controllers.interfaceApi;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.telia.Lease_management.dto.requests.MinisterialStructureDto;
import com.telia.Lease_management.dto.requests.MinistryDto;
import com.telia.Lease_management.dto.requests.RentalRequestDto;
import com.telia.Lease_management.dto.responses.AuthResponse;
import com.telia.Lease_management.dto.responses.StructureWithRequestRentalResponse;
import com.telia.Lease_management.entity.MinisterialStructure;

@CrossOrigin
//@PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_SUPER')")
@RequestMapping("/structure")
public interface MinisterialStructureApi {
    
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AuthResponse> createStructure(@RequestBody MinisterialStructureDto dto);
    
    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    List<MinisterialStructureDto> findAllStructure();

    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    MinisterialStructureDto findStructureById(@PathVariable("id") Long id);

    @PutMapping(value="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MinisterialStructureDto> updateMinisterialStructure(@PathVariable("id") Long id, @RequestBody MinisterialStructureDto structureDto);

    
    @PutMapping(value="/{id}/activate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MinisterialStructureDto> activateStructure(@PathVariable Long id);
    
    
    @PutMapping(value="/{id}/deactivate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MinisterialStructureDto> deactivateStructure(@PathVariable Long id);


  //--------------------------------- Manage rental request ----------------------------------// 
     @GetMapping("/{structureId}/new-rental-requests")
    public ResponseEntity<List<RentalRequestDto>> getNewRentalRequests(@PathVariable Long structureId);
   
    @GetMapping("/{structureId}/validated-rental-requests")
    public ResponseEntity<List<RentalRequestDto>> getValidatedRentalRequests(@PathVariable Long structureId);

    @GetMapping("/{structureId}/send-rental-requests")
    public ResponseEntity<List<RentalRequestDto>> getSendedRentalRequests(@PathVariable Long structureId);
    
    @GetMapping("/{structureId}/complement-rental-requests")
    public ResponseEntity<List<RentalRequestDto>> getComplementRentalRequests(@PathVariable Long structureId);
    
    @GetMapping("/{structureId}/approval-rental-requests")
    public ResponseEntity<List<RentalRequestDto>> getApprovalRentalRequests(@PathVariable Long structureId);
    
    @GetMapping("/{structureId}/rejected-rental-requests")
    public ResponseEntity<List<RentalRequestDto>> getRejectedRentalRequests(@PathVariable Long structureId);
    
    @GetMapping("/{structureId}/held-rental-requests")
    public ResponseEntity<List<RentalRequestDto>> getHeldRentalRequests(@PathVariable Long structureId);
}
