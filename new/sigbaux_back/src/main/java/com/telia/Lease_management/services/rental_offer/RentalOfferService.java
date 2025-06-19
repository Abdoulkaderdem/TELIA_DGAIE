package com.telia.Lease_management.services.rental_offer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.telia.Lease_management.dto.requests.BuildingDto;
import com.telia.Lease_management.dto.requests.LandLordDto;
import com.telia.Lease_management.dto.requests.OfferAndCharacteristicsDto;
import com.telia.Lease_management.dto.requests.RentalOfferDto;
import com.telia.Lease_management.dto.requests.TypeUsageDto;
import com.telia.Lease_management.dto.responses.BuildingResponse;
import com.telia.Lease_management.dto.responses.RentalOfferResponse;
import com.telia.Lease_management.entity.common.RentalStatus;
import com.telia.Lease_management.entity.rental_offer.Building;
import com.telia.Lease_management.entity.rental_offer.LandLord;
import com.telia.Lease_management.entity.rental_offer.OfferAndCharacteristics;
import com.telia.Lease_management.entity.rental_offer.RentalCharacteristics;
import com.telia.Lease_management.entity.rental_offer.RentalOffer;
import com.telia.Lease_management.entity.rental_offer.TypeOfferAndCharacteristics;
import com.telia.Lease_management.entity.rental_request.RentalRequest;
import com.telia.Lease_management.entity.rental_request.TypeUsage;
import com.telia.Lease_management.repository.rental_offer.BuildingRepository;
import com.telia.Lease_management.repository.rental_offer.LandLordRepository;
import com.telia.Lease_management.repository.rental_offer.OfferAndCharacteristicsRepository;
import com.telia.Lease_management.repository.rental_offer.RentalCharacteristicsRepository;
import com.telia.Lease_management.repository.rental_offer.RentalOfferRepository;
import com.telia.Lease_management.repository.rental_request.RentalRequestRepository;
import com.telia.Lease_management.repository.rental_request.TypeUsageRepository;
import com.telia.Lease_management.utils.ValidateForms;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class RentalOfferService {
    
    private RentalOfferRepository rentalOfferRepository;
    private BuildingRepository buildingRepository;
    private LandLordRepository landLordRepository;
    private OfferAndCharacteristicsRepository offerAndCharacteristicsRepository;
    private RentalCharacteristicsRepository rentalCharacteristicsRepo;
    private RentalRequestRepository rentalRequestRepo;
    private TypeUsageRepository typeUsageRepo;
   
    // private final static Path uploadPath = Paths.get("uploadsDocuments"); 
    private final static Path uploadPath = Paths.get("/opt/leaseManagement/uploadsDocuments/"); 


    public RentalOfferResponse createRentalOffer(RentalOfferDto rentalOfferDto) throws Exception {

        // Validate RentalOfferDto fields
        validateRentalOfferDto(rentalOfferDto);        
        // Validate LandLordDto
        validateLandLord(LandLordDto.fromEntity(rentalOfferDto.getLandLord()));
        // Validate each building
        validateBuildings(rentalOfferDto.getBuildingDtos());

        // Enregistrement des documents
        String attestationPath = saveDocument(rentalOfferDto.getAttestationAttributionFile(), true);
        String permitPath = saveDocument(rentalOfferDto.getPermitExploitationFile(), false);

        String pathFileOffer="";
        boolean fileExist=false;

         // Set And save the landlord
         LandLord landLordToSave = rentalOfferDto.getLandLord();
        if (landLordToSave.getId() == null) {
            //Check if The IFU of new LandLord is already use      
            Optional<LandLord> existingLandlord = landLordRepository.findByIfu(landLordToSave.getIfu());
            if (existingLandlord.isPresent()) {
                throw new IllegalStateException("A user with this IFU already exists: " + landLordToSave.getIfu());
            }
            //Check if The EMAIL of new LandLord is already use 
            Optional<LandLord> existingEmailLandlord = landLordRepository.findByEmailAdress(landLordToSave.getEmailAdress());
            if (existingEmailLandlord.isPresent()) {
                throw new IllegalStateException("A user with this email already exists: " + landLordToSave.getEmailAdress());
            }
            landLordToSave.setStatus(RentalStatus.ENABLE);
            landLordToSave = landLordRepository.save(landLordToSave);
        } else {
            landLordToSave = landLordRepository.findById(landLordToSave.getId())
                    .orElseThrow(() -> new IllegalArgumentException("LandLord with id: " + rentalOfferDto.getLandLord().getId() + " not found"));
        }

        //Init and create Rental Offer
        RentalOffer rentalOfferToSave = new RentalOffer();
        rentalOfferToSave.setDateOffer(rentalOfferDto.getDateOffer());
        rentalOfferToSave.setRentalStatus(RentalStatus.AVAILABLE);
        rentalOfferToSave.setCode(rentalOfferDto.getCode());
        rentalOfferToSave.setAttestationAttributionPath(attestationPath);
        rentalOfferToSave.setPermitExploitationPath(permitPath);
        rentalOfferToSave.setLandLord(landLordToSave);
        if (fileExist){
            rentalOfferToSave.setUrlFileOffer(pathFileOffer);
        }

        // As we Check the buildings is present and valid
        List<Building> savedBuildings = new ArrayList<>();
        for (BuildingDto buildingDto : rentalOfferDto.getBuildingDtos()) {
            Building buildingToAdd = BuildingDto.toEntityBuilding(buildingDto);
            //buildingToAdd.setRentalOffer(rentalOfferToSave);

            // Manage ans save Offer And Characteristics
            List<OfferAndCharacteristics> listOfferAndCharacteristicsToAdd = new ArrayList<>();
            if (!buildingToAdd.getListOfferAndCharacteristics().isEmpty()){
                for (OfferAndCharacteristics oAndCharacteristics : buildingToAdd.getListOfferAndCharacteristics()) {
                    RentalCharacteristics rentalCharacteristics = oAndCharacteristics.getCharacteristics();
                    if (rentalCharacteristics.getId() == null) {
                        throw new IllegalArgumentException("Characteristics Rental with liblong " + rentalCharacteristics.getLibLong() + ", not found !");
                    }
                    oAndCharacteristics.setCharacteristics(rentalCharacteristics);
                    if (oAndCharacteristics.getId() == null) {
                        oAndCharacteristics.setType(TypeOfferAndCharacteristics.OFFER);
                        offerAndCharacteristicsRepository.save(oAndCharacteristics);
                    } else {
                        throw new IllegalArgumentException("Connot use an existing Offer and Characteristics:" + oAndCharacteristics.getId() + "! Create a new one!");
                    }
                    listOfferAndCharacteristicsToAdd.add(oAndCharacteristics);
                }
                // Set the building-specific list, as it's Characteristics record during the offer
                buildingToAdd.setListOfferAndCharacteristics(listOfferAndCharacteristicsToAdd);
            }
            
            //Save or update building 
            if (buildingToAdd.getId() == null) {
                buildingToAdd.setStatus(RentalStatus.AVAILABLE);
                buildingToAdd = buildingRepository.save(buildingToAdd);
            } else {
                Building existingBuilding = buildingRepository.findById(buildingToAdd.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Building with id: " + buildingDto.getId() + " not found"));
                existingBuilding.setListOfferAndCharacteristics(listOfferAndCharacteristicsToAdd);
                existingBuilding.setStatus(RentalStatus.AVAILABLE);
                buildingToAdd = buildingRepository.save(existingBuilding);
            }
            //Attach buils to offer and Characteristics Offer
            this.addOandCharacteristicsToBuilding(buildingToAdd);
            savedBuildings.add(buildingToAdd); //Add building to list
        }

        // Set list Buildings to rental offer
        rentalOfferToSave.setBuildings(savedBuildings);   
        // Save the rental offer with updated Landlord and buildings
        RentalOffer savedRentalOffer = rentalOfferRepository.save(rentalOfferToSave);
        
         //Checking type usage for rental offer
        List<TypeUsage> listBuildingsUsageToSave = new ArrayList<>();
        if (rentalOfferDto.getListBuildingOfferUsageDto() != null && !rentalOfferDto.getListBuildingOfferUsageDto().isEmpty()) {
            for (TypeUsageDto buildingTypeUsageDto : rentalOfferDto.getListBuildingOfferUsageDto()) {
                TypeUsage buildingTypeUsage = TypeUsageDto.toEntity(buildingTypeUsageDto);
                if (buildingTypeUsage.getId() == null) {
                    buildingTypeUsage.setRentalOffer(savedRentalOffer);
                    buildingTypeUsage.setRentalRequest(null);
                    buildingTypeUsage = typeUsageRepo.save(buildingTypeUsage);
                } else {
                    TypeUsage existingTypeUsage = typeUsageRepo.findById(buildingTypeUsage.getId())
                            .orElseThrow(() -> new IllegalArgumentException("Type Building usage with id: " + buildingTypeUsageDto.getId() + " not found"));
                    existingTypeUsage.setRentalOffer(savedRentalOffer);
                    buildingTypeUsage = typeUsageRepo.save(existingTypeUsage);
                }
                listBuildingsUsageToSave.add(buildingTypeUsage);
            }
            rentalOfferToSave.setListBuildingOfferUsage(listBuildingsUsageToSave);
            rentalOfferRepository.save(savedRentalOffer);  //save rental offer with these updates
        }

        // Buildings updated to include rental offer
        for (Building building : savedBuildings) {
            building.setRentalOffer(savedRentalOffer);
            buildingRepository.save(building);
        }

        // Convert the saved RentalOffer entity to RentalOfferResponse and return
        return RentalOfferResponse.fromEntity(savedRentalOffer);

    }

    private String saveDocument(MultipartFile offerFile, Boolean isAttestation) {
        // Generate a unique name for the file
        String originalFilename = offerFile.getOriginalFilename();
        String uniqueFilename="";

        if (isAttestation){
            uniqueFilename = "attestation-"+ UUID.randomUUID().toString() + "-" + originalFilename;
        }else {
            uniqueFilename = "permit-"+ UUID.randomUUID().toString() + "-" + originalFilename;
        }
        Path destinationFile = uploadPath.resolve(uniqueFilename);

        try {
            //Save the file to disk
            Files.copy(offerFile.getInputStream(), destinationFile);
            return "/uploadsDocuments/" + uniqueFilename;

        } catch (IOException e) {
            throw new InvalidOperationException("Failed to save file: " + e.getMessage(), e);
        }
    }

    private String saveDocumentForOfferToFolder(MultipartFile offerFile) {
        // Generate a unique name for the file
        String originalFilename = offerFile.getOriginalFilename();
        String uniqueFilename = "offer-"+ UUID.randomUUID().toString() + "-" + originalFilename;
        Path destinationFile = uploadPath.resolve(uniqueFilename);
        
        try {
            //Save the file to disk
            Files.copy(offerFile.getInputStream(), destinationFile);

            return "/uploadsDocuments/" + uniqueFilename;

        } catch (IOException e) {
            throw new InvalidOperationException("Failed to save file: " + e.getMessage(), e);
        }
    }


    private void validateRentalOfferDto(RentalOfferDto rentalOfferDto) {
        if (rentalOfferDto.getDateOffer() == null) {
            throw new IllegalArgumentException("Date offer is missing!");
        }
        if (rentalOfferDto.getCode() == null || rentalOfferDto.getCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Code is missing!");
        }
        if (rentalOfferDto.getAttestationAttributionFile() == null || rentalOfferDto.getAttestationAttributionFile().isEmpty()) {
            throw new IllegalArgumentException("Attestation d'Attribution is required!");
        }
        if (rentalOfferDto.getPermitExploitationFile() == null || rentalOfferDto.getPermitExploitationFile().isEmpty()) {
            throw new IllegalArgumentException("Permis d'Exploitation is required!");
        }
    }

    private void validateLandLord(LandLordDto landLordDto) {
        List<String> errors = ValidateForms.validateLandLord(landLordDto);
        if (!errors.isEmpty()) {
            log.error("Landlord is not valid {}: {}", landLordDto, errors);
            throw new IllegalArgumentException("Landlord is not valid: " + String.join(", ", errors));
        }

    }

    private void validateBuildings(List<BuildingDto> buildingDtos) {
        if (buildingDtos == null || buildingDtos.isEmpty()) {
            throw new IllegalArgumentException("At least one building must be provided!");
        }

        List<String> errors = buildingDtos.stream()
            .flatMap(buildingDto -> ValidateForms.validateBuilding(buildingDto).stream())
            .collect(Collectors.toList());

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Building is not valid: " + String.join(", ", errors));
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

    
    private boolean isPdfFile(MultipartFile file) {
        // Verify file extension
        String filename = file.getOriginalFilename();
        return filename != null && filename.endsWith(".pdf");
    }

    @Transactional
     public RentalOfferResponse updateRentalOffer(Long id, RentalOfferDto rentalOfferDto) {
         //Verify first if the Offer exist
         RentalOffer existingRentalOffer = rentalOfferRepository.findById(id)
         .orElseThrow(() -> new IllegalArgumentException("RentalOffer with id " + id + " not found"));

        // Update fields
        existingRentalOffer.setDateOffer(rentalOfferDto.getDateOffer());
        // existingRentalOffer.setRentalStatus(rentalOfferDto.getRentalStatus());
        existingRentalOffer.setCode(rentalOfferDto.getCode());

        //Update landLord if necessary
        if (rentalOfferDto.getLandLord() != null) {
            LandLord landLord = landLordRepository.findById(rentalOfferDto.getLandLord().getId())
                    .orElseThrow(() -> new IllegalArgumentException("LandLord with id " + rentalOfferDto.getLandLord().getId() + " not found"));
            existingRentalOffer.setLandLord(landLord);
        }else {
            throw new IllegalArgumentException("This Rental Offer must Have Landlord:  " + rentalOfferDto.getCode());
        }

        // Update Buildings
        if (rentalOfferDto.getBuildingDtos() != null && !rentalOfferDto.getBuildingDtos().isEmpty()) {
            List<Building> updatedBuildings = BuildingDto.toEntities(rentalOfferDto.getBuildingDtos());

            for (Building building : updatedBuildings) {
                building.setRentalOffer(existingRentalOffer);
            }            
            // Remplacez complètement la liste de buildings de l'entité RentalOffer
             existingRentalOffer.getBuildings().clear(); // Videz la liste actuelle
             existingRentalOffer.getBuildings().addAll(updatedBuildings); // Ajoutez les nouveaux buildings
        }else {
            throw new IllegalArgumentException("This Rental Offer must Have Building:  " + rentalOfferDto.getId());
        }
        
        rentalOfferRepository.save(existingRentalOffer);

        // Update Type usage
        if (rentalOfferDto.getListBuildingOfferUsageDto() != null  && !rentalOfferDto.getListBuildingOfferUsageDto().isEmpty()) {
            List<TypeUsage> listTypeUsages= TypeUsageDto.toEntities(rentalOfferDto.getListBuildingOfferUsageDto());
            
            //Set rentalOffert
            for (TypeUsage typeUsage : listTypeUsages) {
                typeUsage.setRentalOffer(existingRentalOffer);
            }

            // Completely replace the TypeUsage list of the rentalRequest entity
            existingRentalOffer.getListBuildingOfferUsage().clear(); 
            existingRentalOffer.getListBuildingOfferUsage().addAll(listTypeUsages); 
        }

        RentalOffer updatedRentalOffer = rentalOfferRepository.save(existingRentalOffer);
        //return RentalOfferDto.fromEntity(updatedRentalOffer);
        return RentalOfferResponse.fromEntity(updatedRentalOffer);
    }



    public RentalOfferResponse activateRentalOffer(Long id) {
        
        Optional<RentalOffer> optionalRentalOffer = rentalOfferRepository.findById(id);

        if (optionalRentalOffer.isPresent()) {
            RentalOffer rentalOffer = optionalRentalOffer.get();
            rentalOffer.setRentalStatus(RentalStatus.ENABLE); // Enable
            rentalOffer = rentalOfferRepository.save(rentalOffer);

            return RentalOfferResponse.fromEntity(rentalOffer);
        }
        return null;
    }


    public RentalOfferResponse deactivateRentalOffer(Long id) {
        Optional<RentalOffer> optionalRentalOffer = rentalOfferRepository.findById(id);
        if (optionalRentalOffer.isPresent()) {
            RentalOffer rentalOffer = optionalRentalOffer.get();
            rentalOffer.setRentalStatus(RentalStatus.DISABLED); // INACTIVE
            rentalOffer = rentalOfferRepository.save(rentalOffer);
            // return RentalOfferDto.fromEntity(rentalOffer);
            return RentalOfferResponse.fromEntity(rentalOffer);
        }
        return null;
    }

    public List<RentalOfferResponse> getAllRentalOffers() {
        List<RentalOffer> rentalOffers = rentalOfferRepository.findAll();

        return rentalOffers.stream()
            .map(RentalOfferResponse::fromEntity)
            .collect(Collectors.toList());
    }


    public RentalOfferResponse getRentalOfferById(Long id) {
        Optional<RentalOffer> optionalRentalOffer = rentalOfferRepository.findById(id);
        return optionalRentalOffer.map(RentalOfferResponse::fromEntity).orElse(null);
    }

    @Transactional
    public void changeStatus(Long id, RentalStatus status) {
        Optional<RentalOffer> rentalOfferOptional = rentalOfferRepository.findById(id);
        
        if (rentalOfferOptional.isPresent()) {
            RentalOffer rentalOffer = rentalOfferOptional.get();
            rentalOffer.setRentalStatus(status);
            rentalOfferRepository.save(rentalOffer);
        }
    } 

        
    private void addOandCharacteristicsToBuilding(Building building) {

        List<OfferAndCharacteristics> listOfferAndCharacteristicsTosave = building.getListOfferAndCharacteristics();
        List<OfferAndCharacteristics> listOfferAndCharacteristics = new ArrayList<>();

        for(OfferAndCharacteristics offerAndCharacteristics: listOfferAndCharacteristicsTosave){
            offerAndCharacteristics.setBuilding(building);
            offerAndCharacteristicsRepository.save(offerAndCharacteristics);

            listOfferAndCharacteristics.add(offerAndCharacteristics);
        }
        
        building.getListOfferAndCharacteristics().addAll(listOfferAndCharacteristics);
        buildingRepository.save(building);
    }

    @Transactional  
    public List<BuildingResponse> filterOffersByRentalRequest(Long idRentalRequest) {
        // Retrieve the Rental Request selected
        RentalRequest rentalRequest = rentalRequestRepo.findById(idRentalRequest)
            .orElseThrow(() -> new IllegalArgumentException("Rental Request not found with id: " + idRentalRequest));
        
        //Rental Request must have RentalStatus = HELD
        if (rentalRequest.getStatus() != RentalStatus.HELD){
            new IllegalArgumentException("Rental Request must have RentalStatus = HELD : " + rentalRequest.getId());
        }

        // Build search criteria based on rental request
        Set<String> criteriaSet = new HashSet<>();
        criteriaSet.add(rentalRequest.getRegion());
        criteriaSet.add(rentalRequest.getProvince());
        criteriaSet.add(rentalRequest.getCommune());
        criteriaSet.add(rentalRequest.getCity());
        criteriaSet.add(rentalRequest.getDistrict());

        // Filter buildings
        List<Building> filteredBuildings = buildingRepository.findBuildingsByCriteriaAndStatus(criteriaSet, RentalStatus.AVAILABLE);

        // Mapper les résultats en DTO pour l'API
        return filteredBuildings.stream()
                .map(BuildingResponse::fromEntity)
                .collect(Collectors.toList());
    }



    public List<RentalOfferResponse> getRentalOffersByStatus(RentalStatus status) {
        List<RentalOffer> rentalOffers = rentalOfferRepository.findByRentalStatus(status);
        return rentalOffers.stream()
                           .map(RentalOfferResponse::fromEntity)
                           .collect(Collectors.toList());
    }

    public long countAllRentalOffer() {
        return rentalOfferRepository.count();
    }

}
