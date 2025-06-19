package com.telia.Lease_management.controllers.implementApi;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.telia.Lease_management.controllers.interfaceApi.ContractApi;
import com.telia.Lease_management.dto.requests.ContractDto;
import com.telia.Lease_management.dto.requests.IndemnisationDto;
import com.telia.Lease_management.dto.responses.AuthResponse;
import com.telia.Lease_management.dto.responses.BuildingWithProvisionalRentAmount;
import com.telia.Lease_management.dto.responses.CancelledContratResponse;
import com.telia.Lease_management.dto.responses.ContractResponse;
import com.telia.Lease_management.dto.responses.ContractRevisionResponse;
import com.telia.Lease_management.dto.responses.IndemnisationResponse;
import com.telia.Lease_management.dto.responses.InvoiceResponse;
import com.telia.Lease_management.dto.responses.RecordsTerminatedLeases;
import com.telia.Lease_management.entity.common.RentalStatus;
import com.telia.Lease_management.entity.contract.Contract;
import com.telia.Lease_management.entity.rental_offer.Building;
import com.telia.Lease_management.services.contract.ContractRevisionService;
import com.telia.Lease_management.services.contract.ContractService;
import com.telia.Lease_management.services.contract.IndemnisationService;
import com.telia.Lease_management.services.rental_offer.BuildingService;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
public class ContractController implements ContractApi{

    private ContractService contractService;
    private ContractRevisionService contractRevisionService;
    private BuildingService buildingService;
     private IndemnisationService indemnisationService;

    @Override
    public ResponseEntity<ContractResponse> createContract(ContractDto contractDto) {
        ContractResponse createdContract = contractService.createContract(contractDto);
        return new ResponseEntity<>(createdContract, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ContractResponse> getContract(Long id) {
        ContractResponse contractDto = contractService.getContract(id);
        return new ResponseEntity<>(contractDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ContractResponse> updateContract(Long id, ContractDto contractDto) {
        try {
            ContractResponse updatedContract = contractService.updateContract(id, contractDto);
            return ResponseEntity.ok(updatedContract);
        } catch (IllegalArgumentException e) {
            log.info("Une erreur est survenue: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }catch (Exception e) {
            log.info("Une erreur est survenue : {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    @Override
    public ResponseEntity<ContractResponse> disableContract(Long id) {
        ContractResponse disabledContract = contractService.disableContract(id);
        return new ResponseEntity<>(disabledContract, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<ContractResponse>> getAllContracts() {
        List<ContractResponse> contracts = contractService.getAllContracts();
        return new ResponseEntity<>(contracts, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Long> countContracts() {
        long count = contractService.countContracts();
        // long count = 12;
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ContractRevisionResponse> addContractRevision(Long contractId, String revisionDetails) {
        try {
            ContractRevisionResponse createdRevision = contractRevisionService.addContractRevision(contractId, revisionDetails);
            return ResponseEntity.ok(createdRevision);
        } catch (IllegalArgumentException e) {
            log.info("Une erreur est survenue: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<List<ContractRevisionResponse>> getContractRevisions(Long contractId) {
        List<ContractRevisionResponse> contractRevisions = contractRevisionService.getContractRevisions(contractId);
        return ResponseEntity.ok(contractRevisions);
    }

    @Override
    public ResponseEntity<ContractRevisionResponse> deactivateContractRevision(Long revisionId) {
        ContractRevisionResponse contractRevisionDTO = contractRevisionService.deactivateContractRevision(revisionId);
        return ResponseEntity.ok(contractRevisionDTO);
    }

    @Override
    public ResponseEntity<CancelledContratResponse> terminateContract(Long contractId, String reasonForTermination) {
        CancelledContratResponse terminatedContract = contractService.terminateContract(contractId, reasonForTermination);
        return ResponseEntity.ok(terminatedContract);
    }

    @Override
    public ResponseEntity<BuildingWithProvisionalRentAmount> sendProvisionalRentAmount(Long idBuilding, Long provisionalRentAmount) {
        BuildingWithProvisionalRentAmount updatedBuilding = buildingService.saveProvisionalRentAmount(idBuilding, provisionalRentAmount);
        return ResponseEntity.ok(updatedBuilding);
    }

    @Override
    public ResponseEntity<List<BuildingWithProvisionalRentAmount>> getBuildingToCreateContract() {
        List<BuildingWithProvisionalRentAmount> buildings = buildingService.getBuildingsWithSatisfactoryRentalRequest();
        return ResponseEntity.ok(buildings);
    }

    @Override
    public ResponseEntity<Double> calculateIndemnity(Long contractId) {
        Double indemnityAmount = contractService.calculateIndemnity(contractId);
        return ResponseEntity.ok(indemnityAmount);
    }

    @Override
    public ResponseEntity<ContractResponse> renewContractPeriodicity(Long idContract, int periodicityInMonths, String revisionDetails) {
        try {
            ContractResponse createdRenew = contractRevisionService.revisonContractPeriodicity(idContract, periodicityInMonths, revisionDetails);
            return ResponseEntity.ok(createdRenew);
        } catch (IllegalArgumentException e) {
            log.info("Une erreur est survenue: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<AuthResponse> attachDocumentToContract(Long contractId, MultipartFile contractFile) {
        AuthResponse response = new AuthResponse();
        
        if (contractFile.isEmpty()) {
            throw new InvalidOperationException("The file is empty");
        }  

        try {
            // Check if file is a PDF
            if (!isPdfFile(contractFile)) {
                throw new InvalidOperationException("Le fichier doit être au format PDF.");
            }

            String fileUrl = contractService.attachDocumentToContract(contractId, contractFile);
            log.info("Document attaché avec succès ! URL:  {}", fileUrl);
            response.setStatus(HttpStatus.ACCEPTED.value());
            response.setAnswer("Contrat PDF ajoutés avec succès !");
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setAnswer("Une erreur est survenue: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (InvalidOperationException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setAnswer("Une erreur est survenue: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setAnswer("Une erreur est survenue: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }

    @Override
    public ResponseEntity<Resource> getDocument(String filename) {
        Resource file = contractService.loadFileAsResource(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(file);
    }

    @Override
    public ResponseEntity<Resource> getDocument(Long contractId) {
        return contractService.getDocumentByContractId(contractId);
    }

    @Override
    public ResponseEntity<List<ContractResponse>> getAllActivatedContracts() {
        List<ContractResponse> contracts = contractService.getAllActivatedContracts(RentalStatus.ENABLE);
        return new ResponseEntity<>(contracts, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<ContractResponse>> getAllDisabledContracts() {
        List<ContractResponse> contracts = contractService.getAllActivatedContracts(RentalStatus.DISABLED);
        return new ResponseEntity<>(contracts, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<RecordsTerminatedLeases>> getAllBilanTerminatedContracts() {
        List<RecordsTerminatedLeases> contracts = contractService.getAllBilanTerminatedContracts();
        return new ResponseEntity<>(contracts, HttpStatus.OK);
    }

    
    @Override
    public ResponseEntity<List<ContractRevisionResponse>> getEnabledAmendmentsByContractId(Long contractId) {
        List<ContractRevisionResponse> ListAmendments = contractRevisionService.getEnabledAmendmentsByContractId(contractId);
        return ResponseEntity.ok(ListAmendments);
    }



    //********************************************************************************** */

    private boolean isPdfFile(MultipartFile file) {
        // Verify file extension
        String filename = file.getOriginalFilename();
        return filename != null && filename.endsWith(".pdf");
    }

    @Override
    public ResponseEntity<AuthResponse> attachKeyHandoverReportToContract(Long contractId, MultipartFile reportFile) {
        AuthResponse response = new AuthResponse();
        
        if (reportFile.isEmpty()) {
            throw new InvalidOperationException("The file  to attach key handover report is empty");
        }  

        try {
            // Check if file is a PDF
            if (!isPdfFile(reportFile)) {
                throw new InvalidOperationException("Le fichier PV doit être au format PDF!");
            }

            String fileUrl = contractService.attachKeyHandoverReportToContract(contractId, reportFile);
            log.info("Document attaché avec succès ! URL:  {}", fileUrl);
            response.setStatus(HttpStatus.ACCEPTED.value());
            response.setAnswer("Procès verbal de remise des clés, Sauvegardé !");
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setAnswer("Une erreur est survenue: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (InvalidOperationException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setAnswer("Une erreur est survenue: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setAnswer("Une erreur est survenue: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }

    @Override
    public ResponseEntity<Resource> getKeyHandoverReport(Long contractId) {

        return contractService.getKeyHandoverReportByContractId(contractId);
    }

    @Override
    public ResponseEntity<Double> getIndemnity(Long contractId) {
        try {
            double indemnity = contractService.calculateIndemnity(contractId);
            return ResponseEntity.ok(indemnity);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<IndemnisationResponse> createIndemnisation(IndemnisationDto indemnisationDto,
            MultipartFile file) {
        
        IndemnisationResponse createdIndemnisation = indemnisationService.saveIndemnisation(indemnisationDto, file);
        return new ResponseEntity<>(createdIndemnisation, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<IndemnisationResponse> getIndemnisationById(Long id) {
        Optional<IndemnisationResponse> indemnisation = indemnisationService.getIndemnisationById(id);
        return indemnisation.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    public ResponseEntity<IndemnisationResponse> uploadJustification(Long id, MultipartFile file) {
        try {
            IndemnisationResponse indemnisation = indemnisationService.uploadJustification(id, file);
            return new ResponseEntity<>(indemnisation, HttpStatus.OK);
        } catch (Exception e) {
            log.info("Une erreur est survenue : {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Override
    public ResponseEntity<byte[]> downloadJustification(Long id) {
        return indemnisationService.getIndemnisationPdf(id);
    }

    @Override
    public ResponseEntity<List<ContractResponse>> getAllTerminatedContracts() {
        List<ContractResponse> contracts = contractService.getAllTerminatedContracts();
        return new ResponseEntity<>(contracts, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<IndemnisationResponse>> getAllIndemnisations() {
        List<IndemnisationResponse> indemnisationList = indemnisationService.getAllIndemnisations();
        if (indemnisationList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(indemnisationList);
    }

    @Override
    public ResponseEntity<AuthResponse> renewContractRentalAmount(Long idContract, String revisionDetails) {
        AuthResponse response = new AuthResponse();
        try {
            Boolean isUpdated = contractRevisionService.renewContractRentalAmount(idContract, revisionDetails);
            if (isUpdated) {
                response.setStatus(HttpStatus.ACCEPTED.value());
                response.setAnswer("Le contrat a été désactivé avec succès ! Il est à présent en attente d'une nouvelle Inspection !");
            } else {
                response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
                response.setAnswer("Impossible de désactiver le contrat !");
            }
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Erreur lors de la mise à jour du contrat : {}", e.getMessage(), e);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setAnswer("Erreur de validation : " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            log.error("Erreur inattendue : {}", e.getMessage(), e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setAnswer("Une erreur inattendue est survenue.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Override
    public ResponseEntity<ContractResponse> updateBuildingContractRentAmount(ContractDto contractDto) {
        ContractResponse createdContract = contractService.updateBuildingContractRentAmount(contractDto);
        return new ResponseEntity<>(createdContract, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<AuthResponse> attachNoticeTerminationToContract(Long contractId, MultipartFile noticeFile) {
        AuthResponse response = new AuthResponse();
        
        if (noticeFile.isEmpty()) {
            throw new InvalidOperationException("The file is empty");
        }  

        try {
            // Check if file is a PDF
            if (!isPdfFile(noticeFile)) {
                throw new InvalidOperationException("Le fichier doit être au format PDF.");
            }

            String fileUrl = contractService.attachNoticeToContract(contractId, noticeFile);
            log.info("Document attaché avec succès ! URL:  {}", fileUrl);
            response.setStatus(HttpStatus.ACCEPTED.value());
            response.setAnswer("Note de fin de contrat ajouté avec succès !");
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setAnswer("Une erreur est survenue: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (InvalidOperationException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setAnswer("Une erreur est survenue: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setAnswer("Une erreur est survenue: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Override
    public ResponseEntity<AuthResponse> attachApplyCompensationForIre(Long contractId, Long landlordId,
            MultipartFile applyFile) {
        AuthResponse response = new AuthResponse();
        
        if (applyFile.isEmpty()) {
            throw new InvalidOperationException("The file is empty");
        }  
        
        try {
            // Check if file is a PDF
            if (!isPdfFile(applyFile)) {
                throw new InvalidOperationException("Le fichier doit être au format PDF.");
            }
        
            String fileUrl = contractService.attachApplyCompensationForIre(contractId, landlordId, applyFile);
            log.info("Document attaché avec succès ! URL:  {}", fileUrl);
            response.setStatus(HttpStatus.ACCEPTED.value());
            response.setAnswer("Note de fin de contrat ajouté avec succès !");
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setAnswer("Une erreur est survenue: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
       } catch (InvalidOperationException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setAnswer("Une erreur est survenue: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
             response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setAnswer("Une erreur est survenue: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

   



    
    
}
