package com.telia.Lease_management.controllers.interfaceApi;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.telia.Lease_management.dto.requests.AttachBuildingsToRequestDto;
import com.telia.Lease_management.dto.requests.RentalRequestDto;
import com.telia.Lease_management.dto.responses.AuthResponse;
import com.telia.Lease_management.dto.responses.EntityResponse;
import com.telia.Lease_management.dto.responses.RentalRequestResponse;
import com.telia.Lease_management.entity.common.RentalStatus;
import com.telia.Lease_management.entity.rental_request.RentalRequest;

@CrossOrigin
@RequestMapping("/request_rental")
public interface RentalRequestApi {
    
    @PreAuthorize("hasAnyAuthority('ROLE_CPM', 'ROLE_SUPER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RentalRequestResponse> create(@RequestBody RentalRequestDto dto);

    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RentalRequestResponse> getById(@PathVariable Long id);

    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_CPM', 'ROLE_ORDON', 'ROLE_SUPER')")
    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RentalRequestResponse>> getAll();

    @PreAuthorize("hasAnyAuthority('ROLE_CPM', 'ROLE_SUPER')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping(value="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RentalRequestResponse> update(@PathVariable Long id, @RequestBody RentalRequestDto dto);
    
    @GetMapping("/approval/{structureId}/structure")
    public ResponseEntity<EntityResponse<List<RentalRequestDto>>> findApprovalRentalRequestsForStructure(@PathVariable Long structureId);


    @PreAuthorize("hasAnyAuthority('ROLE_CNOI', 'ROLE_SUPER')")
    @PostMapping("/attach-buildings") 
    public ResponseEntity<AuthResponse> attachBuildingsToRentalRequest(@RequestBody AttachBuildingsToRequestDto attachBuildingsDto);

    @GetMapping("/by-status/{status}")
    public ResponseEntity<List<RentalRequestResponse>> getRentalRequestsByStatus(@PathVariable RentalStatus status);

    @GetMapping("/count")
    public ResponseEntity<Long> countAllRentalRequests();


    //--------------------------------- Change status rental request ----------------------------------// 
    @PreAuthorize("hasAnyAuthority('ROLE_ORDON', 'ROLE_SUPER')")
    @PatchMapping("/status/{id}/validate")
    public ResponseEntity<AuthResponse> validateDemandByOfficer(@PathVariable("id") Long idRentalRequest);

    @PreAuthorize("hasAnyAuthority('ROLE_CPM', 'ROLE_SUPER')")
    @PatchMapping("/status/{id}/send")
    public ResponseEntity<AuthResponse> sendValidateRentalRequest(@PathVariable("id") Long idRentalRequest);
   
    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_SUPER')")
    @PatchMapping("/status/{id}/complement")
    public ResponseEntity<AuthResponse> needComplementRentalRequest(@PathVariable("id") Long idRentalRequest);

    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_SUPER')")
    @PatchMapping("/status/{id}/approval")
    public ResponseEntity<AuthResponse> approveDemandByDGAIE(@PathVariable("id") Long idRentalRequest);

    @PreAuthorize("hasAnyAuthority('ROLE_CNOI', 'ROLE_SUPER')")
    @PostMapping("/status/{id}/reject")
    public ResponseEntity<AuthResponse> rejectDemandByCNOI(@PathVariable("id") Long idRentalRequest, @RequestParam String rejectionReason);

    @PreAuthorize("hasAnyAuthority('ROLE_CNOI', 'ROLE_SUPER')")
    @PatchMapping("/status/{id}/held")
    public ResponseEntity<AuthResponse> heldDemandByCNOI(@PathVariable("id") Long idRentalRequest);

    @PreAuthorize("hasAnyAuthority('ROLE_CNOI', 'ROLE_SUPER')")
    @GetMapping("status/satisfactory-not-validated")
    public List<RentalRequestResponse> getSatisfactoryNotValidatedRequests();

    @PreAuthorize("hasAnyAuthority('ROLE_CNOI', 'ROLE_SUPER')")
    @GetMapping("status/satisfactory-validated")
    public List<RentalRequestResponse> getSatisfactoryValidatedRequests() ;
    
}
