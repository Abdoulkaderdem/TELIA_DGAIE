package com.telia.Lease_management.services.contract;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.telia.Lease_management.dto.requests.ContractDto;
import com.telia.Lease_management.dto.responses.CancelledContratResponse;
import com.telia.Lease_management.dto.responses.ContractResponse;
import com.telia.Lease_management.dto.responses.ContractRevisionResponse;
import com.telia.Lease_management.dto.responses.RecordsTerminatedLeases;
import com.telia.Lease_management.entity.MinisterialStructure;
import com.telia.Lease_management.entity.Ministry;
import com.telia.Lease_management.entity.common.RentalStatus;
import com.telia.Lease_management.entity.contract.ApplyForCompensation;
import com.telia.Lease_management.entity.contract.CancelledContrat;
import com.telia.Lease_management.entity.contract.Contract;
import com.telia.Lease_management.entity.rental_offer.Building;
import com.telia.Lease_management.entity.rental_offer.LandLord;
import com.telia.Lease_management.entity.rental_request.RentalRequest;
import com.telia.Lease_management.repository.contract.CancelledContratRepository;
import com.telia.Lease_management.repository.contract.ContractRepository;
import com.telia.Lease_management.repository.rental_offer.BuildingRepository;
import com.telia.Lease_management.repository.rental_offer.LandLordRepository;
import com.telia.Lease_management.repository.rental_request.RentalRequestRepository;
import com.telia.Lease_management.utils.DateUtils;
import com.telia.Lease_management.utils.ValidateForms;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class ContractService {
    
    private ContractRepository contractRepository;
    private BuildingRepository buildingRepository;
    private RentalRequestRepository rentalRequestRepo;
    private CancelledContratRepository cancelledContratRepo;
    private LandLordRepository landLordRepo;

    // private final static Path uploadPath = Paths.get("uploadsDocuments"); 
    // private final static Path uploadKeyReport = Paths.get("uploadsKeyReport"); 
    private final static Path uploadPath = Paths.get("/opt/leaseManagement/uploadsDocuments/"); 
    private final static Path uploadKeyReport = Paths.get("/opt/leaseManagement/uploadsKeyReport/"); 


    public ContractResponse createContract(ContractDto contractDto) {
        Building existingBuilding = buildingRepository.findById(contractDto.getBuildingId())
                .orElseThrow(() -> new RuntimeException("Building not found"));
             
        if (!existingBuilding.getStatus().equals(RentalStatus.MATCHED)) {
            throw new IllegalStateException("Building statut must be 'MATCHED' to create a contract! NOT: "+ existingBuilding.getStatus());
        }

        //Retreive the rental request for building
        RentalRequest rentalRequest = existingBuilding.getRentalRequest();
        if (rentalRequest == null) {
            throw new IllegalStateException("Rental request not found, for this contract !" );
        }

        if (!rentalRequest.getStatus().equals(RentalStatus.SATISFACTORY)) {
            throw new IllegalStateException("Rental request statut must be 'SATISFACTORY' to create a contract! NOT: " + rentalRequest.getStatus());
        }

        //Check if the contract form is valid
        List<String> errors= ValidateForms.validateContract(contractDto);
        if (!errors.isEmpty()){
            log.error("Contract is not valid {}:", contractDto);
            log.error("Liste des erreurs {}: ", errors);
            throw new IllegalArgumentException("Contract is not valid !");
        }
        
        //Change Rental Request Status and save it
        rentalRequest.setStatus(RentalStatus.FINALIZED);
        rentalRequestRepo.save(rentalRequest);

        //Change Building Status and save it 
        existingBuilding.setStatus(RentalStatus.RENT);
        buildingRepository.save(existingBuilding);
        // Create a lease Contract Number
        String leaseContractNumber = generateContractNumber();

         // Calculate the end date
        Instant startDate = contractDto.getStartDate();
        int periodicityInMonths = contractDto.getContractPeriodicity();
        Instant endDate = DateUtils.calculateEndDate(startDate, periodicityInMonths);


        Contract contract = new Contract();
        contract.setBuilding(existingBuilding);
        contract.setLeaseContractNumber(leaseContractNumber);
        contract.setBankAccountNumber(contractDto.getBankAccountNumber());
        contract.setRentAmount(existingBuilding.getProvisionalRentAmount());
        contract.setInitialRent(existingBuilding.getProvisionalRentAmount());
        contract.setStartDate(contractDto.getStartDate());
        contract.setEndDate(endDate);
        contract.setTerms(contractDto.getTerms());
        contract.setContractPeriodicity(contractDto.getContractPeriodicity());
        contract.setPresident(contractDto.getPresident());
        contract.setReporter(contractDto.getReporter());
        contract.setContractingAuthority(contractDto.getContractingAuthority());
        contract.setStatus(RentalStatus.DISABLED);
        contract.setIreAmount(0.0);
        //Save contract
        contract = contractRepository.save(contract);

        return ContractResponse.fromEntity(contract);
    }

    
    private double calculateIRE(Long rentAmount, Instant startDate, Instant endDate) {
        long durationInYears = Duration.between(startDate, endDate).toDays() / 365;
        durationInYears = durationInYears == 0 ? 1 : durationInYears; // Si la durée est moins d'un an, considérer comme 1 an

        double coefficient;
        if (durationInYears <= 3) {
            coefficient = 1;
        } else if (durationInYears <= 5) {
            coefficient = 2;
        } else if (durationInYears <= 10) {
            coefficient = 3;
        } else if (durationInYears <= 15) {
            coefficient = 4.5;
        } else {
            coefficient = 6;
        }

        return rentAmount * coefficient;
    }


    private String generateContractNumber() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = currentDate.format(formatter);

        Random random = new Random();
        int randomNumber = random.nextInt(90000000) + 10000000; // for a 8 digits number
        // int randomNumber = random.nextInt(90) + 10; // Pour un nombre à 2 chiffres

        return formattedDate + "-" + String.valueOf(randomNumber);
    }
    

    public ContractResponse getContract(Long id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found"));

        return ContractResponse.fromEntity(contract);
    }


    public ContractResponse updateContract(Long id, ContractDto contractDto) {
        Contract existingContract = contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found"));

        Building existingBuilding = buildingRepository.findById(contractDto.getBuildingId())
                .orElseThrow(() -> new RuntimeException("Building not found"));

         //Checking ...
         if (existingContract.getStatus() == RentalStatus.DISABLED || existingContract.isTerminated()){
            throw new IllegalArgumentException("Cannot revise a terminated or disabled contract");
        }

        //Updating
        // existingContract.setBuilding(existingBuilding);
        // existingContract.setRentAmount(contractDto.getRentAmount());
        // existingContract.setStartDate(contractDto.getStartDate());
        // existingContract.setEndDate(contractDto.getEndDate());
        existingContract.setTerms(contractDto.getTerms());
        // existingContract.setContractPeriodicity(contractDto.getContractPeriodicity());

        existingContract = contractRepository.save(existingContract);
        return ContractResponse.fromEntity(existingContract);
    }


    // public void deleteContract(Long id) {
    //     Contract contract = contractRepository.findById(id)
    //             .orElseThrow(() -> new RuntimeException("Contract not found"));
    //     contractRepository.delete(contract);
    // }


    public ContractResponse disableContract(Long id) {
        Contract contractToDisabled = contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found"));

        contractToDisabled.setStatus(RentalStatus.DISABLED); 

        contractToDisabled = contractRepository.save(contractToDisabled);
        return ContractResponse.fromEntity(contractToDisabled);
    }


    public List<ContractResponse> getAllContracts() {
        List<Contract> contracts = contractRepository.findAll();

        return contracts.stream()
                .map(contract -> new ContractResponse(
                        contract.getId(),
                        contract.getBuilding().getId(),
                        contract.getRentAmount(),
                        contract.getStartDate(),
                        contract.getEndDate(),
                        contract.getTerms(),
                        contract.getContractPeriodicity(),
                        // contract.getPresident(),
                        // contract.getReporter(),
                        // contract.getContractingAuthority(),
                        //contract.getIreAmount() != null? contract.getIreAmount():0.0,
                        contract.getStatus(),
                        contract.isRevised(),
                        contract.isTerminated(),
                        contract.isKeyHandoverReport(),
                        contract.isIndemnisation()
                ))
                .collect(Collectors.toList());
    }



    public long countContracts() {
        return contractRepository.count();
    }



    public CancelledContratResponse terminateContract(Long contractId, String reasonForTermination) {
        Contract existingContract = contractRepository.findById(contractId)
                .orElseThrow(() -> new EntityNotFoundException("Contract not found"));
        
        if (existingContract.isTerminated()){
            throw new IllegalArgumentException("This contract is Already Resiliated !!! ");
        }

        if (reasonForTermination==null || reasonForTermination==""){
            throw new IllegalArgumentException("Reason For Termination is not valid !");
       }

        //Retreive the  building
        Building existingBuilding = buildingRepository.findById(existingContract.getBuilding().getId())
                .orElseThrow(() -> new RuntimeException("Building not found"));

        //Retreive the rental request for building
        RentalRequest rentalRequest = existingBuilding.getRentalRequest();

         //Update Rental Request Status and save it
         rentalRequest.setStatus(RentalStatus.DISABLED);
         rentalRequestRepo.save(rentalRequest);
 
         //Update Building Status and save it 
         existingBuilding.setStatus(RentalStatus.AVAILABLE);
         buildingRepository.save(existingBuilding);

        existingContract.setTerminated(true);
        existingContract.setStatus(RentalStatus.DISABLED);
        contractRepository.save(existingContract);

        //Create an entity of CancelleContract
        CancelledContrat cancelledContrat=new CancelledContrat();
        //retreive indemnity
        Double resultIndemnity= calculateIndemnity(existingContract.getId());
        cancelledContrat.setContract(existingContract);
        cancelledContrat.setIndemnityAmount(resultIndemnity);
        cancelledContrat.setReasonForTermination(reasonForTermination);
        cancelledContratRepo.save(cancelledContrat); //saving

        return CancelledContratResponse.fromEntity(cancelledContrat);
  
    }

    public double calculateIndemnity(Long contractId) {
        //Here we calculate IRE (indemnité Forfetaire de Remise en Etat)
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new RuntimeException("Contract not found"));

        long years = Duration.between(contract.getStartDate(), contract.getEndDate()).toDays() / 365;
        long rentAmount = contract.getRentAmount();
        double coefficient = getCoefficient(years);

        double indemnityAmount = rentAmount * coefficient;

        return indemnityAmount;
    }

    private double getCoefficient(long years) {
        if (years <= 3) return 1;
        if (years <= 5) return 2;
        if (years <= 10) return 3;
        if (years <= 15) return 4.5;
        return 6;
    }


    // public ContractResponse revisonContractPeriodicity(Long idContract, int periodicityInMonths) {
    //     Contract existingContract = contractRepository.findById(idContract)
    //             .orElseThrow(() -> new RuntimeException("Contract not found"));

    //     if (periodicityInMonths < 1 ){
    //         new RuntimeException("The value is not correct");
    //     }
    //     // Calculate the new end date
    //     Instant startDate = existingContract.getStartDate();
    //     Instant newEndDate = DateUtils.calculateEndDate(startDate, periodicityInMonths);

    //     //set and save
    //     existingContract.setEndDate(newEndDate);
    //     contractRepository.save(existingContract);

    //     return ContractResponse.fromEntity(existingContract);
    // }


    public String  attachDocumentToContract(Long contractId, MultipartFile contractFile) {
          Contract existingContract = contractRepository.findById(contractId)
                .orElseThrow(() -> new EntityNotFoundException("Contract not found"));

        if (contractFile.isEmpty()) {
            throw new InvalidOperationException("The file is empty");
        }  

        createUploadDirIfNotExists();

        // Generate a unique name for the file
        String originalFilename = contractFile.getOriginalFilename();
        String uniqueFilename = UUID.randomUUID().toString() + "-" + originalFilename;
        Path destinationFile = uploadPath.resolve(uniqueFilename);

        try {
            //Save the file to disk
            Files.copy(contractFile.getInputStream(), destinationFile);

            // Update the contract with the file path
            existingContract.setDocumentUrl("/uploadsDocuments/" + uniqueFilename); 
            existingContract.setStatus(RentalStatus.ENABLE);
            contractRepository.save(existingContract);

            return "/uploadsDocuments/" + uniqueFilename;

        } catch (IOException e) {
            throw new InvalidOperationException("Failed to save file: " + e.getMessage(), e);
        }

    }

    private void createUploadDirIfNotExists() {
        try {
            if (Files.notExists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    
     public Resource loadFileAsResource(String filename) {
        try {
            Path filePath = uploadPath.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found " + filename);
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not load file " + filename, e);
        }
    }


    public String getDocumentUrlFromContract(Long contractId) {
        // Trouver le contrat dans la base de données
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new EntityNotFoundException("Contract not found with id: " + contractId));

        // Récupérer l'URL du document
        return contract.getDocumentUrl();
    }

    
    public ResponseEntity<Resource> getDocumentByContractId(Long contractId) {
        Contract existingContract = contractRepository.findById(contractId)
                .orElseThrow(() -> new EntityNotFoundException("Contract not found"));

        String documentUrl = existingContract.getDocumentUrl();
        if (documentUrl == null || documentUrl.isEmpty()) {
            throw new EntityNotFoundException("No document found for this contract");
        }

        Path filePath = uploadPath.resolve(Paths.get(documentUrl).getFileName().toString());
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new EntityNotFoundException("Could not read the file");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }
    }





    public List<ContractResponse> getAllActivatedContracts(RentalStatus status) {
        List<Contract> contracts = contractRepository.findByStatus(status);

        return contracts.stream()
                .map(contract -> new ContractResponse(
                        contract.getId(),
                        contract.getBuilding().getId(),
                        contract.getRentAmount(),
                        contract.getStartDate(),
                        contract.getEndDate(),
                        contract.getTerms(),
                        contract.getContractPeriodicity(),
                        // contract.getPresident(),
                        // contract.getReporter(),
                        // contract.getContractingAuthority(),
                        //contract.getIreAmount() != null? contract.getIreAmount():0.0,
                        contract.getStatus(),
                        contract.isRevised(),
                        contract.isTerminated(),
                        contract.isKeyHandoverReport(),
                        contract.isIndemnisation()
                ))
                .collect(Collectors.toList());
    }


    public List<ContractResponse> getAllTerminatedContracts() {
        List<Contract> listContractsTerminated = contractRepository.findByIsTerminated(true);
        return listContractsTerminated.stream()
                .map(contract -> new ContractResponse(
                        contract.getId(),
                        contract.getBuilding().getId(),
                        contract.getRentAmount(),
                        contract.getStartDate(),
                        contract.getEndDate(),
                        contract.getTerms(),
                        contract.getContractPeriodicity(),
                        // contract.getPresident(),
                        // contract.getReporter(),
                        // contract.getContractingAuthority(),
                        //contract.getIreAmount() != null? contract.getIreAmount():0.0,
                        contract.getStatus(),
                        contract.isRevised(),
                        contract.isTerminated(),
                        contract.isKeyHandoverReport(),
                        contract.isIndemnisation()
                ))
                .collect(Collectors.toList());

    }
    
    public List<RecordsTerminatedLeases> getAllBilanTerminatedContracts() {
        List<Contract> listContractsTerminated = contractRepository.findByIsTerminated(true);

        List <RecordsTerminatedLeases> lTerminatedLeases = listContractsTerminated.stream()
                            .map(contract ->{                                
                                Building building = contract.getBuilding(); //retreive building
                                LandLord landLord = building.getRentalOffer().getLandLord();  //retreive Landlord
                                MinisterialStructure structure= building.getRentalRequest().getStructure();
                                Ministry ministry = structure.getMinistry();
                                CancelledContrat cancelledContrat = cancelledContratRepo.findByContract(contract)
                                         .orElseThrow(() -> new EntityNotFoundException("Resilaiation Contract not found! " + contract.getId()));
                                
                                String fullNameLandlord = landLord.getLastname() + " " + landLord.getFirstname();
                                return new RecordsTerminatedLeases(
                                    landLord.getId(),
                                    contract.getId(),
                                    fullNameLandlord,
                                    contract.getLeaseContractNumber(),
                                    building.getRegion(),
                                    building.getProvince(),
                                    building.getCommune(),
                                    building.getCity(),
                                    building.getDistrict(),
                                    building.getSector(),
                                    building.getSection(),
                                    building.getIlot(),
                                    building.getPlot(),
                                    building.getLocality(),
                                    structure.getName(),
                                    ministry.getName(),
                                    cancelledContrat.getReasonForTermination(),
                                    cancelledContrat.getDateCreated(),
                                    contract.getRentAmount());
                            })
                            .collect(Collectors.toList());
        
        return lTerminatedLeases;
        
    }


    public String attachKeyHandoverReportToContract(Long contractId, MultipartFile reportFile) {
        Contract existingContract = contractRepository.findById(contractId)
             .orElseThrow(() -> new EntityNotFoundException("Contract not found"));

        if (reportFile.isEmpty()) {
            throw new InvalidOperationException("The file is empty");
        }  

        createUploadKeyReportDirIfNotExists(); 

        // Generate a unique name for the file
        String originalFilename = reportFile.getOriginalFilename();
        String uniqueFilename = UUID.randomUUID().toString() + "-" + originalFilename;
        Path destinationFile = uploadKeyReport.resolve(uniqueFilename);

        try {
            //Save the file to disk
            Files.copy(reportFile.getInputStream(), destinationFile);

            // Update the contract with the file path
            existingContract.setReportKeyUrl("/uploadsKeyReport/" + uniqueFilename); 
            existingContract.setKeyHandoverReport(true);
            contractRepository.save(existingContract);

            return "/uploadsKeyReport/" + uniqueFilename;

        } catch (IOException e) {
            throw new InvalidOperationException("Failed to save file: " + e.getMessage(), e);
        }

    }


    private void createUploadKeyReportDirIfNotExists() {
        try {
            if (Files.notExists(uploadKeyReport)) {
                Files.createDirectories(uploadKeyReport);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create report Key Handover directory", e);
        }
    }


    public ResponseEntity<Resource> getKeyHandoverReportByContractId(Long contractId) {
        Contract existingContract = contractRepository.findById(contractId)
                .orElseThrow(() -> new EntityNotFoundException("Contract not found"));

        String pathtUrl = existingContract.getReportKeyUrl();
        if (pathtUrl == null || pathtUrl.isEmpty()) {
            throw new EntityNotFoundException("No report handover pdf found for this contract! ");
        }

        Path filePath = uploadKeyReport.resolve(Paths.get(pathtUrl).getFileName().toString());
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new EntityNotFoundException("Could not read the file report pdf !");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }
    }


    public ContractResponse updateBuildingContractRentAmount(ContractDto contractDto) {
        Building existingBuilding = buildingRepository.findById(contractDto.getBuildingId())
            .orElseThrow(() -> new RuntimeException("Building not found"));
     
        if (!existingBuilding.getStatus().equals(RentalStatus.MATCHED)) {
            throw new IllegalStateException("Building statut must be 'MATCHED' to create a contract! ");
        }

        //Retreive the rental request for building
        RentalRequest rentalRequest = existingBuilding.getRentalRequest();
        if (rentalRequest == null) {
            throw new IllegalStateException("Rental request not found, for this contract !" );
        }

        if (!rentalRequest.getStatus().equals(RentalStatus.SATISFACTORY)) {
            throw new IllegalStateException("Rental request statut must be 'SATISFACTORY' to create a contract: " + rentalRequest.getId());
        }

        //Check if the contract form is valid
        List<String> errors= ValidateForms.validateContract(contractDto);
        if (!errors.isEmpty()){
            log.error("Contract is not valid {}:", contractDto);
            log.error("Liste des erreurs {}: ", errors);
            throw new IllegalArgumentException("Contract is not valid !");
        }

        //Retreive the contract to update it
        Contract existingContract = contractRepository.findByBuilding(existingBuilding)
            .orElseThrow(() -> new RuntimeException("ERROR! Connot find the Building to update Contract. "));


        //Change Rental Request Status and save it
        rentalRequest.setStatus(RentalStatus.FINALIZED);
        rentalRequestRepo.save(rentalRequest);

        //Change Building Status and save it 
        existingBuilding.setStatus(RentalStatus.RENT);
        buildingRepository.save(existingBuilding);
        // Create a lease Contract Number
        // String leaseContractNumber = generateContractNumber();

        // Calculate the end date
        Instant startDate = contractDto.getStartDate();
        int periodicityInMonths = contractDto.getContractPeriodicity();
        Instant endDate = DateUtils.calculateEndDate(startDate, periodicityInMonths);

        //Set existing Contract
        existingContract.setBuilding(existingBuilding);
        existingContract.setBankAccountNumber(contractDto.getBankAccountNumber());
        existingContract.setRentAmount(existingBuilding.getProvisionalRentAmount());
        existingContract.setStartDate(contractDto.getStartDate());
        existingContract.setEndDate(endDate);
        existingContract.setTerms(contractDto.getTerms());
        existingContract.setContractPeriodicity(contractDto.getContractPeriodicity());
        existingContract.setStatus(RentalStatus.DISABLED);
        existingContract.setIreAmount(0.0);
        //Save contract
        existingContract = contractRepository.save(existingContract);

        return ContractResponse.fromEntity(existingContract);
    }


    public void checkContractStatus() {
        Instant now = Instant.now();
        List<Contract> contracts = contractRepository.findAll();

        for (Contract contract : contracts) {
            if (contract.getEndDate().isBefore(now) && !contract.isTerminated()) {
                terminateContract(contract.getId(), "Le contrat est arrivé à son terme!");
                // contract.setStatus(RentalStatus.DISABLED); 
                // contract.setTerminated(true);
                // contractRepository.save(contract);
            }
        }
    }


    public String attachNoticeToContract(Long contractId, MultipartFile noticeFile) {
        Contract existingContract = contractRepository.findById(contractId)
            .orElseThrow(() -> new EntityNotFoundException("Contract not found! "));

        if (noticeFile.isEmpty()) {
            throw new InvalidOperationException("The file is empty! ");
        }  

        createUploadDirIfNotExists();

        // Generate a unique name for the file
        String originalFilename = noticeFile.getOriginalFilename();
        String uniqueFilename = "Notice_Terminate-" +UUID.randomUUID().toString() + "-" + originalFilename;
        Path destinationFile = uploadPath.resolve(uniqueFilename);

        try {
            //Save the file to disk
            Files.copy(noticeFile.getInputStream(), destinationFile);

            // Update the contract with the file path
            existingContract.setNoticeUrl("/uploadsDocuments/" + uniqueFilename); 
            contractRepository.save(existingContract);

            return "/uploadsDocuments/" + uniqueFilename;

        } catch (IOException e) {
            throw new InvalidOperationException("Failed to save file: " + e.getMessage(), e);
        }

    }


    public String attachApplyCompensationForIre(Long contractId, Long landlordId, MultipartFile applyFile) {
        Contract existingContract = contractRepository.findById(contractId)
            .orElseThrow(() -> new EntityNotFoundException("Contract not found! "));
        
        LandLord existingLandlord = landLordRepo.findById(landlordId)
            .orElseThrow(() -> new EntityNotFoundException("Landlord not found ! "));

        if (applyFile.isEmpty()) {
            throw new InvalidOperationException("The file is empty! ");
        }  

        createUploadDirIfNotExists();

        // Generate a unique name for the file
        String originalFilename = applyFile.getOriginalFilename();
        String uniqueFilename = "Apply_IRE-" +UUID.randomUUID().toString() + "-" + originalFilename;
        Path destinationFile = uploadPath.resolve(uniqueFilename);

        try {
            //Save the file to disk
            Files.copy(applyFile.getInputStream(), destinationFile);

            ApplyForCompensation applyForCompensation= new ApplyForCompensation();
            applyForCompensation.setContract(existingContract);
            applyForCompensation.setLandLord(existingLandlord);
            applyForCompensation.setApplyUrl("/uploadsDocuments/" + uniqueFilename);

            return "/uploadsDocuments/" + uniqueFilename;

        } catch (IOException e) {
            throw new InvalidOperationException("Failed to save file: " + e.getMessage(), e);
        }
    }

}
