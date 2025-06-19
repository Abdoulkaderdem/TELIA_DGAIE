package com.telia.Lease_management.controllers.interfaceApi;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.telia.Lease_management.dto.requests.BuildingDto;
import com.telia.Lease_management.dto.requests.OfferAndCharacteristicsDto;
import com.telia.Lease_management.dto.responses.AuthResponse;
import com.telia.Lease_management.dto.responses.BuildingForInspection;
import com.telia.Lease_management.dto.responses.BuildingResponse;
import com.telia.Lease_management.dto.responses.BuildingWithProvisionalRentAmount;
import com.telia.Lease_management.dto.responses.EntityBooleanResponse;
import com.telia.Lease_management.dto.responses.EntityResponse;
import com.telia.Lease_management.entity.common.RentalStatus;
import com.telia.Lease_management.entity.rental_offer.Building;

@CrossOrigin
@RequestMapping("/building")
public interface BuildingApi {

    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_LANDLORD', 'ROLE_SUPER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BuildingResponse> createBuilding(@RequestBody BuildingDto buildingDto);

    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BuildingResponse> getBuildingById(@PathVariable Long id);

    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_LANDLORD', 'ROLE_SUPER')")
    @PutMapping(value="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BuildingResponse> updateBuilding(@PathVariable Long id, @RequestBody BuildingDto buildingDto);

    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BuildingResponse>> getAllBuildings() ;

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping(value = "/{buildingId}/offerAndCharacteristics", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addOfferAndCharacteristicsToBuilding(@PathVariable Long buildingId, @RequestBody List<OfferAndCharacteristicsDto> listOfferAndCharacteristicsDto);

    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping(value = "/{buildingId}/offerAndCharacteristics/{offerAndCharacteristicsId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> removeOfferAndCharacteristicsFromBuilding(@PathVariable Long buildingId, @PathVariable Long offerAndCharacteristicsId);
 
    @GetMapping("/by-status/{status}")
    public ResponseEntity<List<BuildingResponse>> getBuildingsByStatus(@PathVariable RentalStatus status);

    @GetMapping("/count")
    public ResponseEntity<Long> countAllBuildings();

//************************************************************************** */
    @PreAuthorize("hasAnyAuthority('ROLE_CNOI', 'ROLE_SUPER')")
    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping("/filter-by-rentalRequest/{rentalRequestId}")
    public ResponseEntity<EntityResponse<List<BuildingResponse>>> filterRentalOffersByBuilding(@PathVariable Long rentalRequestId);
     
    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping(value = "/all/for-inspection", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BuildingForInspection>> getAllBuildingsForInspection() ;

    @PreAuthorize("hasAnyAuthority('ROLE_DGAHCMAH', 'ROLE_SUPER')")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/inspection-finish/report") 
    public ResponseEntity<AuthResponse> updateBuildingAfterInspection(@RequestBody List<BuildingForInspection> listBuildingUpdatedAfterInspection);

    @PreAuthorize("hasAnyAuthority('ROLE_CNOI', 'ROLE_SUPER')")
    @GetMapping("/rental-request/{rentalRequestId}/conform")
    public ResponseEntity<List<BuildingResponse>> getMatchedBuildings(@PathVariable Long rentalRequestId);
    
    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping(value = "/{id}/estimated", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BuildingForInspection> getBuildingAlreadyAmountEstimatedById(@PathVariable Long id);  
    

    //***************************** Manage the provisional rental amount after council minsters ************************************ */
    @PreAuthorize("hasAnyAuthority('ROLE_CNOI', 'ROLE_SUPER')")
    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping(value = "/rental-request/{idRequest}/no-examaminated-council", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BuildingWithProvisionalRentAmount> getBuildingAlreadyEstimatedForUpdateReportCouncilMinister(@PathVariable Long idRequest);      
 
    @PreAuthorize("hasAnyAuthority('ROLE_CNOI', 'ROLE_SUPER')")
    @PatchMapping("/provisional-rent-amount/{idBuilding}/validate")
    public ResponseEntity<AuthResponse> validateProvisionalRentalAmount(@PathVariable Long idBuilding); 

    @PreAuthorize("hasAnyAuthority('ROLE_CNOI', 'ROLE_SUPER')")
    @PatchMapping("/provisional-rent-amount/{idBuilding}/reject")
    public ResponseEntity<AuthResponse> rejecteProvisionalRentalAmount(@PathVariable Long idBuilding); 

    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_SUPER')")
    @GetMapping("/rental-request/{rentalRequestId}/provisional-amount") 
    public ResponseEntity<BuildingWithProvisionalRentAmount> getBuildingWithProvisionalRentalAmount(@PathVariable Long rentalRequestId);
 
}
