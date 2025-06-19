package com.telia.Lease_management.controllers.interfaceApi;

import java.time.Instant;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.telia.Lease_management.dto.requests.ContractDto;
import com.telia.Lease_management.dto.requests.IndemnisationDto;
import com.telia.Lease_management.dto.responses.AuthResponse;
import com.telia.Lease_management.dto.responses.BuildingWithProvisionalRentAmount;
import com.telia.Lease_management.dto.responses.CancelledContratResponse;
import com.telia.Lease_management.dto.responses.ContractResponse;
import com.telia.Lease_management.dto.responses.ContractRevisionResponse;
import com.telia.Lease_management.dto.responses.IndemnisationResponse;
import com.telia.Lease_management.dto.responses.RecordsTerminatedLeases;
import com.telia.Lease_management.entity.contract.Indemnisation;
import com.telia.Lease_management.entity.rental_offer.Building;

@CrossOrigin
@RequestMapping("/contract")
public interface ContractApi {
    
    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_SUPER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ContractResponse> createContract(@RequestBody ContractDto contractDto);

    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_SUPER')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping(value = "/{contractId}/attach-document")
    public ResponseEntity<AuthResponse> attachDocumentToContract(@PathVariable("contractId") Long contractId, @RequestParam("file") MultipartFile contractFile);

    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_SUPER')")
    @GetMapping("/{contractId}/document")
    public ResponseEntity<Resource> getDocument(@PathVariable Long contractId);

    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_SUPER')")
    @GetMapping("/documents/{filename}")
    public ResponseEntity<Resource> getDocument(@PathVariable String filename);

    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_SUPER')")
    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ContractResponse> getContract(@PathVariable Long id);

    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_SUPER')")
    @PutMapping(value="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ContractResponse> updateContract(@PathVariable Long id, @RequestBody ContractDto contractDto);

    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_SUPER')")
    @PutMapping("/{id}/disable")
    public ResponseEntity<ContractResponse> disableContract(@PathVariable Long id);

    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_SUPER')")
    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ContractResponse>> getAllContracts();
    
    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_SUPER')")
    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping(value = "/activated/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ContractResponse>> getAllActivatedContracts();
    
    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_SUPER')")
    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping(value = "/disabled/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ContractResponse>> getAllDisabledContracts();


    @GetMapping("/count")
    public ResponseEntity<Long> countContracts();

    @PreAuthorize("hasAnyAuthority('ROLE_CNOI', 'ROLE_SUPER')")
    @PutMapping("/{idBuilding}/provisional-rent-amount") 
    public ResponseEntity<BuildingWithProvisionalRentAmount> sendProvisionalRentAmount(@PathVariable Long idBuilding, @RequestParam Long provisionalRentAmount); 

    @GetMapping("/request-rental/make-contract")
    public ResponseEntity<List<BuildingWithProvisionalRentAmount>> getBuildingToCreateContract();

    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_SUPER')")
    @GetMapping("/{contractId}/calculate-indemnity")
    public ResponseEntity<Double> calculateIndemnity(@PathVariable Long contractId);

    
    //*****************************Revision Contract ************************************ */
    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_CNOI', 'ROLE_SUPER')")
    @PostMapping("/{contractId}/revisions")
    public ResponseEntity<ContractRevisionResponse> addContractRevision( @PathVariable Long contractId, @RequestParam String revisionDetails);

    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_CNOI', 'ROLE_SUPER')")
    @GetMapping("/{contractId}/revisions/all")
    public ResponseEntity<List<ContractRevisionResponse>> getEnabledAmendmentsByContractId(@PathVariable Long contractId);

    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_CNOI', 'ROLE_SUPER')")
    @GetMapping("/{contractId}/revisions")
    public ResponseEntity<List<ContractRevisionResponse>> getContractRevisions(@PathVariable Long contractId);

    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_SUPER')")
    @PutMapping("/renew/{idContract}/period-in-month")
    public ResponseEntity<ContractResponse> renewContractPeriodicity(@PathVariable Long idContract, @RequestParam int periodicityInMonths, @RequestParam String revisionDetails);

    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_SUPER')")
    @PutMapping("/renew/{idContract}/rental-amount")
    public ResponseEntity<AuthResponse> renewContractRentalAmount(@PathVariable Long idContract, @RequestParam String revisionDetails);

    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_SUPER')")
    @PutMapping("/revisions/{revisionId}/deactivate")
    public ResponseEntity<ContractRevisionResponse> deactivateContractRevision(@PathVariable Long revisionId);
     
    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_SUPER')")
    @PutMapping(value = "/update-building/rental-amount", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ContractResponse> updateBuildingContractRentAmount(@RequestBody ContractDto contractDto);


    // @PatchMapping("/status/{idBuilding}/finalise")
    // public ResponseEntity<AuthResponse> finaliseDemandRentalByCNOI(@PathVariable("idBuilding") Long idBuilding);

    //****************Resiliation Contract ******************* */
    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_SUPER')")
    @PutMapping("/{contractId}/terminate")
    public ResponseEntity<CancelledContratResponse> terminateContract(@PathVariable Long contractId, @RequestParam String reasonForTermination);
            
    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_SUPER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/bilan/terminated/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RecordsTerminatedLeases>> getAllBilanTerminatedContracts();
             
    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_SUPER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/terminated/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ContractResponse>> getAllTerminatedContracts();

    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_SUPER')")
    @GetMapping("/{contractId}/indemnity")
    public ResponseEntity<Double> getIndemnity(@PathVariable Long contractId);

    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_SUPER')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping(value = "/{contractId}/landlord/{landlordId}/attach-apply")
    public ResponseEntity<AuthResponse> attachApplyCompensationForIre(
            @PathVariable("contractId") Long contractId, 
            @PathVariable("landlordId") Long landlordId,
            @RequestParam("file") MultipartFile applyFile);

    
    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_SUPER')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping(value = "/{contractId}/attach-notice-termination")
    public ResponseEntity<AuthResponse> attachNoticeTerminationToContract(
            @PathVariable("contractId") Long contractId,
            @RequestParam("file") MultipartFile noticeFile);



    //****************Indemnisation After resiliation Contract ******************* */
    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_SUPER')")
    @PostMapping(value = "/indemnisation/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IndemnisationResponse> createIndemnisation(@ModelAttribute IndemnisationDto indemnisationDto,  @RequestParam(value = "file", required = false) MultipartFile file) ;

    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_SUPER')")
    @GetMapping("/indemnisation/{id}")
    public ResponseEntity<IndemnisationResponse> getIndemnisationById(@PathVariable Long id);

    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_SUPER')")
    @PutMapping("/indemnisation/{id}/upload-justification")
    public ResponseEntity<IndemnisationResponse> uploadJustification(
            @PathVariable Long id,
            @RequestParam(value = "file", required = true) MultipartFile file);

    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_SUPER')")
    @GetMapping("/indemnisation/{id}/download-justification")
    public ResponseEntity<byte[]> downloadJustification(@PathVariable Long id); 

    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_SUPER')")
    @GetMapping("/indemnisation/all")
    public ResponseEntity<List<IndemnisationResponse>> getAllIndemnisations();

    //*********************** REMISE DES Clés ************ */
    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_SUPER')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping(value = "/attach/{contractId}/key-handover-report")
    public ResponseEntity<AuthResponse> attachKeyHandoverReportToContract(@PathVariable("contractId") Long contractId, @RequestParam("file") MultipartFile reportFile);
    
    @PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_SUPER')")
    @GetMapping("/{contractId}/key-handover-report")
    public ResponseEntity<Resource> getKeyHandoverReport(@PathVariable Long contractId);

     

}
