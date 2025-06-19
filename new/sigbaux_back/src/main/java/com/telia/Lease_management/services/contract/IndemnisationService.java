package com.telia.Lease_management.services.contract;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import com.telia.Lease_management.dto.requests.IndemnisationDto;
import com.telia.Lease_management.dto.responses.IndemnisationResponse;
import com.telia.Lease_management.entity.contract.CancelledContrat;
import com.telia.Lease_management.entity.contract.Contract;
import com.telia.Lease_management.entity.contract.Indemnisation;
import com.telia.Lease_management.entity.contract.Invoice;
import com.telia.Lease_management.repository.contract.CancelledContratRepository;
import com.telia.Lease_management.repository.contract.ContractRepository;
import com.telia.Lease_management.repository.contract.IndemnisationRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class IndemnisationService {

    private CancelledContratRepository cancelledContratRepo;
    private IndemnisationRepository indemnisationRepository;
    private ContractRepository contractRepo;

    
    // private final static Path uploadKeyReport = Paths.get("uploadsKeyReport");  
    private final static Path uploadKeyReport = Paths.get("/opt/leaseManagement/uploadsKeyReport/");
    
    
    private void createUploadKeyReportDirIfNotExists() {
        try {
            if (Files.notExists(uploadKeyReport)) {
                Files.createDirectories(uploadKeyReport);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create report Key Handover directory", e);
        }
    }

    private IndemnisationResponse mapToResponseDto(Indemnisation indemnisation) {
        IndemnisationResponse dto = new IndemnisationResponse();
        dto.setId(indemnisation.getId());
        dto.setDate(indemnisation.getDate());
        dto.setJustificationFilePath(indemnisation.getJustificationFilePath());
        dto.setComment(indemnisation.getComment());
        dto.setJustificationUploaded(indemnisation.isJustificationUploaded());
        return dto;
    }


    public IndemnisationResponse saveIndemnisation(IndemnisationDto indemnisationDto, MultipartFile indemnisationFile) {
        boolean isFilePresent =false;
        System.out.println(indemnisationDto);
        log.info("Id contrat: {}", indemnisationDto.getContratId());

        Contract existingContract = contractRepo.findById(indemnisationDto.getContratId())
                .orElseThrow(() -> new EntityNotFoundException("Contract not found, for this Indemnisation!"));

        CancelledContrat existingResiliationContract = cancelledContratRepo.findByContract(existingContract)
                .orElseThrow(() -> new EntityNotFoundException("This contract is not yet Resiliated. Not Found!"));

        if (existingResiliationContract.isIndemnisation()){
            throw new EntityNotFoundException("Indemnisation already Justify ! ");
        }

        if (indemnisationDto.getDate() == null) {
            throw new EntityNotFoundException("Date for Indemnisation is missing ! ");
        }

        if (indemnisationDto.getComment() == null || indemnisationDto.getComment().isEmpty()) {
            throw new EntityNotFoundException("Comment for Indemnisation is missing ! ");
        }
        
        if (!indemnisationFile.isEmpty()) {
            isFilePresent= true;
        }  

        Indemnisation indemnisation = new Indemnisation();
        indemnisation.setDate(indemnisationDto.getDate());
        indemnisation.setResiliationContract(existingResiliationContract);
        indemnisation.setJustificationFilePath("");
        indemnisation.setComment(indemnisationDto.getComment());
        indemnisation.setJustificationUploaded(false);

        Indemnisation savedIndemnisation = indemnisationRepository.save(indemnisation);

        if (isFilePresent){
            uploadJustification(savedIndemnisation.getId(), indemnisationFile);
        }

        return mapToResponseDto(savedIndemnisation);
    }


    public Optional<IndemnisationResponse> getIndemnisationById(Long id) {
        return indemnisationRepository.findById(id)
            .map(this::mapToResponseDto);
    }

    public IndemnisationResponse uploadJustification(Long indemnisationId, MultipartFile indemnisationFile){
        Optional<Indemnisation> optionalIndemnisation = indemnisationRepository.findById(indemnisationId);

        if (optionalIndemnisation.isPresent()) {
            createUploadKeyReportDirIfNotExists();

            // Generate a unique name for the file
            String originalFilename = indemnisationFile.getOriginalFilename();
            String uniqueFilename = "indemnisation-" + UUID.randomUUID().toString() + "-" + originalFilename;
            Path destinationFile = uploadKeyReport.resolve(uniqueFilename);

            try {
                //Save the file to disk
                Files.copy(indemnisationFile.getInputStream(), destinationFile);

                Indemnisation indemnisation = optionalIndemnisation.get();
                String filePath = "/uploadsKeyReport/" + uniqueFilename;

                //Update Resiliation Contract
                CancelledContrat cancelledContrat = indemnisation.getResiliationContract();
                cancelledContrat.setIndemnisation(true);
                cancelledContratRepo.save(cancelledContrat);
                //Update Contract
                Contract contract = cancelledContrat.getContract();
                contract.setIndemnisation(true);
                contractRepo.save(contract);
                //Update Idemnisation Contract
                indemnisation.setJustificationFilePath(filePath);
                indemnisation.setJustificationUploaded(true);
                Indemnisation updatedIndemnisation = indemnisationRepository.save(indemnisation);
                return mapToResponseDto(updatedIndemnisation);
    
            } catch (IOException e) {
                throw new InvalidOperationException("Failed to save file: " + e.getMessage(), e);
            }   
        } else {
            throw new RuntimeException("Indemnisation not found");
        }
    }

    public List<IndemnisationResponse> getAllIndemnisations() {
            return indemnisationRepository.findAll().stream()
                    .map(this::mapToResponseDto)
                    .collect(Collectors.toList());
        }
    
    public ResponseEntity<byte[]> getIndemnisationPdf(Long indemnisationId) {
        Indemnisation existingIndemnisation = indemnisationRepository.findById(indemnisationId)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found"));

        String pathUrl = existingIndemnisation.getJustificationFilePath();
        if (pathUrl == null || pathUrl.isEmpty()) {
            throw new EntityNotFoundException("No PDF found for this Indemnisation!");
        }

        Path filePath = uploadKeyReport.resolve(Paths.get(pathUrl).getFileName().toString());
        try {
            // Lire le fichier en tant que tableau d'octets
            byte[] pdfContents = Files.readAllBytes(filePath);
            
            // Définir les en-têtes HTTP
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filePath.getFileName().toString());

            // Retourner la réponse avec le tableau d'octets
            return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
        } catch (IOException e) {
            // Gérer les exceptions en cas d'erreur de lecture
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
