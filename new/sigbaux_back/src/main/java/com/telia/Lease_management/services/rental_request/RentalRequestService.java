package com.telia.Lease_management.services.rental_request;

import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import com.telia.Lease_management.dto.requests.RentalRequestDto;
import com.telia.Lease_management.dto.requests.RequestAndCharacteristicsDto;
import com.telia.Lease_management.dto.requests.TypeUsageDto;
import com.telia.Lease_management.dto.responses.RentalRequestResponse;
import com.telia.Lease_management.entity.MinisterialStructure;
import com.telia.Lease_management.entity.Ministry;
import com.telia.Lease_management.entity.Users;
import com.telia.Lease_management.entity.common.RentalStatus;
import com.telia.Lease_management.entity.common.RoleType;
import com.telia.Lease_management.entity.rental_offer.Building;
import com.telia.Lease_management.entity.rental_request.RentalRequest;
import com.telia.Lease_management.entity.rental_request.RequestAndCharacteristics;
import com.telia.Lease_management.entity.rental_request.TypeUsage;
import com.telia.Lease_management.repository.MinisterialStrutureRepository;
import com.telia.Lease_management.repository.UsersRepository;
import com.telia.Lease_management.repository.rental_offer.BuildingRepository;
import com.telia.Lease_management.repository.rental_request.RentalRequestRepository;
import com.telia.Lease_management.repository.rental_request.RequestAndCharacteristicsRepository;
import com.telia.Lease_management.repository.rental_request.TypeUsageRepository;
import com.telia.Lease_management.utils.NotificationService;
import com.telia.Lease_management.utils.ValidateForms;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class RentalRequestService {
    
    private RentalRequestRepository rentalRequestRepo;
    private MinisterialStrutureRepository ministerialStrutureRepo;
    private TypeUsageRepository typeUsageRepo;
    private BuildingRepository buildingRepos;
    private RequestAndCharacteristicsRepository rAndCharacteristicsRepo;
    private UsersRepository usersRepo;
    private NotificationService notificationService;

    
    @Transactional(rollbackFor = Exception.class)
    public RentalRequestResponse createRentalRequest(RentalRequestDto rentalRequestDto) throws Exception{
        //Check the forms
        List<String> errors= ValidateForms.validateRentalRequest(rentalRequestDto);
        if (!errors.isEmpty()){
            log.error("Rental Request  is not valid {}: ", rentalRequestDto);
            log.error("Liste des erreurs {}: ", errors);
            throw new Exception("Rental Request is not valid !");
        }

        //Create an instance to save
        RentalRequest rentalRequestToComplete = new RentalRequest();

        // Update fields
        rentalRequestToComplete.setDateRequest(rentalRequestDto.getDateRequest());
        rentalRequestToComplete.setDescription(rentalRequestDto.getDescription());
        rentalRequestToComplete.setMotivationRequest(rentalRequestDto.getMotivationRequest());
        rentalRequestToComplete.setStructureCurrentPosition(rentalRequestDto.getStructureCurrentPosition());
        rentalRequestToComplete.setAgentsNumber(rentalRequestDto.getAgentsNumber());
        rentalRequestToComplete.setManagersNumber(rentalRequestDto.getManagersNumber());
        rentalRequestToComplete.setLeasePortfolioMinistry(rentalRequestDto.getLeasePortfolioMinistry());
        rentalRequestToComplete.setBuildingsOccupancyStatus(rentalRequestDto.getBuildingsOccupancyStatus());
        // Update current geographical location
        rentalRequestToComplete.setRegion(rentalRequestDto.getRegion());
        rentalRequestToComplete.setProvince(rentalRequestDto.getProvince());
        rentalRequestToComplete.setCommune(rentalRequestDto.getCommune());
        rentalRequestToComplete.setCity(rentalRequestDto.getCity());
        rentalRequestToComplete.setDistrict(rentalRequestDto.getDistrict());
        rentalRequestToComplete.setSector(rentalRequestDto.getSector());
        // Update geographical location of choice
        rentalRequestToComplete.setRegionDesired(rentalRequestDto.getRegionDesired());
        rentalRequestToComplete.setProvinceDesired(rentalRequestDto.getProvinceDesired());
        rentalRequestToComplete.setCommuneDesired(rentalRequestDto.getCommuneDesired());
        rentalRequestToComplete.setCityDesired(rentalRequestDto.getCityDesired());
        rentalRequestToComplete.setDistrictDesired(rentalRequestDto.getDistrictDesired());
        rentalRequestToComplete.setSectorDesired(rentalRequestDto.getSectorDesired());
        // Set the characteristics values
        rentalRequestToComplete.setNumberOfRoom(rentalRequestDto.getNumberOfRoom());
        rentalRequestToComplete.setNumberOfRoomMeeting(rentalRequestDto.getNumberOfRoomMeeting());
        rentalRequestToComplete.setNumberOfBathroom(rentalRequestDto.getNumberOfBathroom());

        //Check if Structure is not null and save it
         if (rentalRequestDto.getStructure().getId()!= null) {
            Long id = rentalRequestDto.getStructure().getId();
            MinisterialStructure structure = ministerialStrutureRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Structure not found"));  

            rentalRequestToComplete.setStructure(structure); 
            //log.info("***************** Strucutre OK: {}", structure);
        }

        rentalRequestToComplete.setStatus(RentalStatus.NEW);
        rentalRequestToComplete = rentalRequestRepo.save(rentalRequestToComplete);
     
        //Save Request Characteristics Usage (List) 
        List<RequestAndCharacteristics> listRequestAndCharacteristics = new ArrayList<>();
        if (rentalRequestDto.getListRequestAndCharacteristicsDtos() != null && !rentalRequestDto.getListRequestAndCharacteristicsDtos().isEmpty()){
            for (RequestAndCharacteristicsDto requestAndCharacteristicsDto: rentalRequestDto.getListRequestAndCharacteristicsDtos()){              
                RequestAndCharacteristics requestAndCharacteristics = RequestAndCharacteristicsDto.toEntity(requestAndCharacteristicsDto);
                if (requestAndCharacteristics.getId() == null) {
                    // if the requestAndCharacteristics's ID is null, then it is soo save it
                    requestAndCharacteristics.setRentalRequest(rentalRequestToComplete);
                    rAndCharacteristicsRepo.save(requestAndCharacteristics);
                }else {
                    // If not, looking for the existing requestAndCharacteristics
                    Optional<RequestAndCharacteristics> existingRequestAndCharacteristics = rAndCharacteristicsRepo.findById(requestAndCharacteristics.getId());
                    if (existingRequestAndCharacteristics.isPresent()) {
                        existingRequestAndCharacteristics.get().setValues(requestAndCharacteristics.getValues());
                        existingRequestAndCharacteristics.get().setRentalRequest(rentalRequestToComplete);
                        requestAndCharacteristics= rAndCharacteristicsRepo.save(existingRequestAndCharacteristics.get());
                        // log.info("****************** updateCharacteristicsUsage: {}", characteristicsUsage);
                    } else {
                        throw new IllegalArgumentException("characteristics Usage with id " + requestAndCharacteristics.getId() + " not found");
                    }
                }
                // rAndCharacteristicsRepo.save(requestAndCharacteristics);
                listRequestAndCharacteristics.add(requestAndCharacteristics);
            }
        }

         // Save RentalRequest first
         rentalRequestToComplete.setListRequestAndCharacteristics(listRequestAndCharacteristics);
         rentalRequestToComplete = rentalRequestRepo.save(rentalRequestToComplete);

        List<TypeUsage> listBuildingsUsageToSave = new ArrayList<>();
        //Save Building usage (Type Usage)
        if (!rentalRequestDto.getListBuildingUsageDto().isEmpty()){
            for (TypeUsageDto buildinUsageDto: rentalRequestDto.getListBuildingUsageDto()){
                TypeUsage buildingUsage= TypeUsageDto.toEntity(buildinUsageDto);

                if (buildingUsage.getId() == null) {
                    buildingUsage.setRentalRequest(rentalRequestToComplete);
                    TypeUsage saveTypeUsage =typeUsageRepo.save(buildingUsage);
                    buildingUsage = saveTypeUsage;
                }else {
                    TypeUsage existingBuildingUsage = typeUsageRepo.findById(buildingUsage.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Building usage with id " + buildinUsageDto.getId() + " not found"));
                        existingBuildingUsage.setRentalRequest(rentalRequestToComplete);
                        buildingUsage= typeUsageRepo.save(existingBuildingUsage);
                }              
                listBuildingsUsageToSave.add(buildingUsage);
            }            
        }else{
            throw new Exception("Building usage is not found !");
        }

        rentalRequestToComplete.setListBuildingUsage(listBuildingsUsageToSave);       
        //Save Rental Request    
        RentalRequest saveRentalRequest = rentalRequestRepo.save(rentalRequestToComplete);

        return RentalRequestResponse.fromEntity(saveRentalRequest);

    }

    
    public List<RentalRequestResponse> findAll(String userConnectedEmail) {

        Users currentUser= this.usersRepo.findByEmail(userConnectedEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));  

         // Check role
         if (currentUser.getRole() == RoleType.CPM || currentUser.getRole() == RoleType.ORDON) {
            if (currentUser.getMinisterialStructure()!= null){
                  // Filter requests according to the user's structure
                  List<RentalRequest> rentalRequests= rentalRequestRepo.findByStructure(currentUser.getMinisterialStructure());
                  return rentalRequests.stream()
                        .map(RentalRequestResponse::fromEntity)
                        .collect(Collectors.toList());
            }else{
                throw new RuntimeException("Impossible! User with role CPM or ORDON does not belong to a Structure" + currentUser.getMinisterialStructure()); 
            }
        }

        // Retrieving the list of all Rental Request  from BDD
        List<RentalRequest> rentalRequests = rentalRequestRepo.findAll();
        
        // Mapping des RentalRequest vers RentalRequestDto
        List<RentalRequestResponse> rentalRequestDtoList = rentalRequests.stream()
            .map(RentalRequestResponse::fromEntity) 
            .collect(Collectors.toList());

         return rentalRequestDtoList;
    }


    public RentalRequestResponse findById(Long id, String userConnectedEmail) {
        if (id == null) {
            log.error("Rental Request ID is null");
            return null;
        }

        RentalRequest optionalRentalRequest= rentalRequestRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("optionalRentalRequest not found")); 

        Users currentUser= this.usersRepo.findByEmail(userConnectedEmail)
                .orElseThrow(() -> new RuntimeException("User not found")); 

        
        // Check role
         if (currentUser.getRole() == RoleType.CPM || currentUser.getRole() == RoleType.ORDON) {
            if (currentUser.getMinisterialStructure()== optionalRentalRequest.getStructure()){
                return RentalRequestResponse.fromEntity(optionalRentalRequest);
            }else{
                throw new RuntimeException("Impossible! User with role CPM or ORDON cannot search this Request Rental" + optionalRentalRequest.getStructure()); 
            }
        }
        
        return RentalRequestResponse.fromEntity(optionalRentalRequest);
   
    }


    @Transactional
    public RentalRequestResponse updateRentalRequest(Long id, RentalRequestDto rentalRequestDto) {
        //Retrieve rental request
        RentalRequest rentalRequest = rentalRequestRepo.findById(id)
             .orElseThrow(() -> new IllegalArgumentException("Rental Request with id " + id + " not found"));

        //Check if the Rental Request form is valid
        List<String> errors= ValidateForms.validateRentalRequest(rentalRequestDto);
        if (!errors.isEmpty()){
            log.error("Rental Request is not valid {}:", rentalRequestDto);
            log.error("Liste des erreurs {}: ", errors);
            throw new IllegalArgumentException("Rental Request is not valid !");
        }     
        

        if (rentalRequestDto.getStructure().getId()!= null) {
            Long idStructure = rentalRequestDto.getStructure().getId();
            MinisterialStructure structure = ministerialStrutureRepo.findById(idStructure)
                    .orElseThrow(() -> new RuntimeException("Structure not found! "));
            rentalRequest.setStructure(structure);
        }

          // Update fields
        rentalRequest.setDateRequest(rentalRequestDto.getDateRequest());
        rentalRequest.setDescription(rentalRequestDto.getDescription());
        rentalRequest.setMotivationRequest(rentalRequestDto.getMotivationRequest());
        rentalRequest.setStructureCurrentPosition(rentalRequestDto.getStructureCurrentPosition());
        rentalRequest.setAgentsNumber(rentalRequestDto.getAgentsNumber());
        rentalRequest.setManagersNumber(rentalRequestDto.getManagersNumber());
        rentalRequest.setLeasePortfolioMinistry(rentalRequestDto.getLeasePortfolioMinistry());
        rentalRequest.setBuildingsOccupancyStatus(rentalRequestDto.getBuildingsOccupancyStatus());
        // Update current geographical location
        rentalRequest.setRegion(rentalRequestDto.getRegion());
        rentalRequest.setProvince(rentalRequestDto.getProvince());
        rentalRequest.setCommune(rentalRequestDto.getCommune());
        rentalRequest.setCity(rentalRequestDto.getCity());
        rentalRequest.setDistrict(rentalRequestDto.getDistrict());
        rentalRequest.setSector(rentalRequestDto.getSector());
        // Update geographical location of choice
        rentalRequest.setRegionDesired(rentalRequestDto.getRegionDesired());
        rentalRequest.setProvinceDesired(rentalRequestDto.getProvinceDesired());
        rentalRequest.setCommuneDesired(rentalRequestDto.getCommuneDesired());
        rentalRequest.setCityDesired(rentalRequestDto.getCityDesired());
        rentalRequest.setDistrictDesired(rentalRequestDto.getDistrictDesired());
        rentalRequest.setSectorDesired(rentalRequestDto.getSectorDesired());
        // Set the characteristics values
        rentalRequest.setNumberOfRoom(rentalRequestDto.getNumberOfRoom());
        rentalRequest.setNumberOfRoomMeeting(rentalRequestDto.getNumberOfRoomMeeting());
        rentalRequest.setNumberOfBathroom(rentalRequestDto.getNumberOfBathroom());

        rentalRequestRepo.save(rentalRequest);
        
        // Update List Building Usage
        if (rentalRequestDto.getListBuildingUsageDto() != null && !rentalRequestDto.getListBuildingUsageDto().isEmpty()){
            List<TypeUsage> listTypeUsages= TypeUsageDto.toEntities(rentalRequestDto.getListBuildingUsageDto());
            
            //Set rentalRequest
            for (TypeUsage typeUsage : listTypeUsages) {
                typeUsage.setRentalRequest(rentalRequest);
            }

            // Completely replace the TypeUsage list of the rentalRequest entity
            rentalRequest.getListBuildingUsage().clear(); 
            rentalRequest.getListBuildingUsage().addAll(listTypeUsages); 
        }else {
            throw new IllegalArgumentException("This Rental Request must Have Building Type Usage:  " + rentalRequestDto.getListBuildingUsageDto());
        }

        rentalRequestRepo.save(rentalRequest);

        // Update List Characteristics Usage
        if (rentalRequestDto.getListRequestAndCharacteristicsDtos() != null && !rentalRequestDto.getListRequestAndCharacteristicsDtos().isEmpty()){
            List<RequestAndCharacteristics> listRequestAndCharacteristics = RequestAndCharacteristicsDto.toEntities(rentalRequestDto.getListRequestAndCharacteristicsDtos());
            
            // Set rentalRequest for each CharacteristicsUsage
            for (RequestAndCharacteristics requestAndCharacteristics : listRequestAndCharacteristics) {
                requestAndCharacteristics.setRentalRequest(rentalRequest);
            }       
            // Clear and add all Characteristics Usage to rentalRequest
            rentalRequest.getListRequestAndCharacteristics().clear(); 
            rentalRequest.getListRequestAndCharacteristics().addAll(listRequestAndCharacteristics); 
        }

        // Save the updated rentalRequest entity
        rentalRequest = rentalRequestRepo.save(rentalRequest); // Save the updated entity

        return RentalRequestResponse.fromEntity(rentalRequest);
    }


    public List<RentalRequestDto> getApprovalRentalRequestsForStructure(Long structureId) {
        MinisterialStructure structure = ministerialStrutureRepo.findById(structureId)
                .orElseThrow(() -> new RuntimeException("Structure not found!"));
        // log.info("****Structure: {}", structure.getId());
        
        //Search only for request rental with status APPROVAL by DGAIE
        List<RentalRequest> rentalRequests = rentalRequestRepo.findByStructureAndStatus(structure, RentalStatus.APPROVAL);
        // log.info("****rentalRequests: {}", rentalRequests.size());
        return rentalRequests.stream()
                .map(RentalRequestDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional    
    public void attachBuildingsToRentalRequest(Long rentalRequestId, List<Long> buildingIds) {
        if (buildingIds == null || buildingIds.isEmpty()) {
            throw new IllegalArgumentException("Building IDs list cannot be null or empty !");
        }
        
        RentalRequest rentalRequest = rentalRequestRepo.findById(rentalRequestId)
            .orElseThrow(() -> new IllegalArgumentException("Rental Request not found with id: " + rentalRequestId));
    
        List<Building> listBuildings = buildingRepos.findAllById(buildingIds);
        if (listBuildings.size() != buildingIds.size()) {
            log.error("Mismatch in list sizes: listBuildings.size={} and buildingIds.size={}", listBuildings.size(), buildingIds.size());
            throw new IllegalArgumentException("Some buildings not found with given IDs !");
        }
    
        for (Building building : listBuildings) {
            if (building.getStatus() != RentalStatus.AVAILABLE) {
                throw new IllegalArgumentException("Building with ID " + building.getId() + " is not available for an attachment !");
            }
            building.setStatus(RentalStatus.RETAINED);
            building.setRentalRequest(rentalRequest);
        }
    
        buildingRepos.saveAll(listBuildings);
        rentalRequest.setStatus(RentalStatus.BATIMENT_FOUND);
        rentalRequestRepo.save(rentalRequest);
    }


    public void validateRequest(Long idRentalRequest) {
        // Retreive the rental demand
        RentalRequest rentalRequestToValidate = rentalRequestRepo.findById(idRentalRequest)     
            .orElseThrow(() -> new RuntimeException("Rental Request not found"));
        
           // Check if the status is NEW
        if (!(rentalRequestToValidate.getStatus().equals(RentalStatus.NEW) ||
                rentalRequestToValidate.getStatus().equals(RentalStatus.COMPLEMENT))) {
            throw new RuntimeException("Only requests with status NEW or COMPLEMENT can be validated");
        }

        rentalRequestToValidate.setStatus(RentalStatus.VALIDATE);
        rentalRequestRepo.save(rentalRequestToValidate);
        
    }

    public void sendRentalRequest(Long idRentalRequest) {
        // Retreive the rental demand
        RentalRequest rentalRequestToSend = rentalRequestRepo.findById(idRentalRequest)     
             .orElseThrow(() -> new RuntimeException("Rental Request not found"));
         
        // Check if the status is VALIDATE
        if (!rentalRequestToSend.getStatus().equals(RentalStatus.VALIDATE)) {
             throw new RuntimeException("Only requests with status VALIDATE can be send");
        }
 
        rentalRequestToSend.setStatus(RentalStatus.SEND);
        rentalRequestRepo.save(rentalRequestToSend);
        notifyDGAIEUsers(rentalRequestToSend);  //Send mail to DGAIE for new Rental Request
    }

    public void needComplementRentalRequest(Long idRentalRequest) {
        // Retreive the rental demand
        RentalRequest rentalRequestNeedComplement = rentalRequestRepo.findById(idRentalRequest)     
            .orElseThrow(() -> new RuntimeException("Rental Request not found"));
            
        // Check if the status is SEND
        if (!rentalRequestNeedComplement.getStatus().equals(RentalStatus.SEND)) {
            throw new RuntimeException("Only requests with status SEND need Complement! ");
        }
    
        rentalRequestNeedComplement.setStatus(RentalStatus.COMPLEMENT);
        rentalRequestRepo.save(rentalRequestNeedComplement);
        notifyComplementRequestToOrdonnator(rentalRequestNeedComplement);  //Send mail to Ordonnateur for Complete the Rental Request

    }

    // public void rejectRentalRequest(Long idRentalRequest) {
    //     // Retreive the rental demand
    //     RentalRequest rentalRequestToReject = rentalRequestRepo.findById(idRentalRequest)     
    //         .orElseThrow(() -> new RuntimeException("Rental Request not found!"));
            
    //     // Check if the status is SEND
    //     if (!rentalRequestToReject.getStatus().equals(RentalStatus.SEND)) {
    //         throw new RuntimeException("Only requests with status SEND can be rejected! ");
    //     }
    
    //     rentalRequestToReject.setStatus(RentalStatus.REJECTED);
    //         rentalRequestRepo.save(rentalRequestToReject);

    // }

    public void approvalDemandByDGAIE(Long idRentalRequest) {
        // Retreive the rental demand
        RentalRequest rentalRequestNeedApproval = rentalRequestRepo.findById(idRentalRequest)     
            .orElseThrow(() -> new RuntimeException("Rental Request not found"));
                
        // Check if the status is SEND
        if (!rentalRequestNeedApproval.getStatus().equals(RentalStatus.SEND)) {
            throw new RuntimeException("Only requests with status SEND need Approuval ! ");
        }
        
        rentalRequestNeedApproval.setStatus(RentalStatus.APPROVAL);
        rentalRequestRepo.save(rentalRequestNeedApproval);
        notifyApprovalRequestToOrdonnator(rentalRequestNeedApproval);  //Send mail to Ordonnateur for approval the Rental Request
        notifyApprovalRequestToCNOI(rentalRequestNeedApproval);  //Send mail to CNOI for approval the Rental Request
    }


    public void rejectDemandByCNOI(Long idRentalRequest, String rejectionReason) {
        // Retreive the rental demand
        RentalRequest rentalRequestToReject = rentalRequestRepo.findById(idRentalRequest)     
           .orElseThrow(() -> new RuntimeException("Rental Request not found"));
               
               
        if (rejectionReason==null || StringUtils.isEmpty(rejectionReason)){
            throw new IllegalArgumentException("RejectionReason is not valid !");
        }

        // Check if the status is APPROVAL
        if (!rentalRequestToReject.getStatus().equals(RentalStatus.APPROVAL)) {
           throw new RuntimeException("Only requests with status APPROVAL can be rejected ! Not this:  " + rentalRequestToReject.getStatus());
        }
       
        // rentalRequestToReject.setStatus(RentalStatus.REJECTED);
        rentalRequestToReject.setStatus(RentalStatus.SEND); //Send to DGAIE to transfert to the concern structure
        rentalRequestToReject.setRejectionReason(rejectionReason);
        rentalRequestRepo.save(rentalRequestToReject);

        rentalRejectRequestToOrdonnator(rentalRequestToReject);  //Send mail to Ordonnateur for REJECTED the Rental Request
        notifyRejectRequestToDgaie(rentalRequestToReject);  //Send mail to DGAIE for REJECTED the Rental Request
    }


    public void heldDemandByCNOI(Long idRentalRequest) {
        // Retreive the rental demand
        RentalRequest rentalRequestToHeld = rentalRequestRepo.findById(idRentalRequest)     
           .orElseThrow(() -> new RuntimeException("Rental Request not found"));
               
        // Check if the status is APPROVAL
        if (!rentalRequestToHeld.getStatus().equals(RentalStatus.APPROVAL)) {
           throw new RuntimeException("Only requests with status APPROVAL can be helded ! ");
        }
       
        rentalRequestToHeld.setStatus(RentalStatus.HELD);
        rentalRequestRepo.save(rentalRequestToHeld);
        rentalHeldRequestToOrdonnator(rentalRequestToHeld);  //Send mail to Ordonnateur for HELD the Rental Request
        notifyHeldRequestToDgaie(rentalRequestToHeld);  //Send mail to DGAIE for HELD the Rental Request
    }


    public List<RentalRequestResponse> getRentalRequestsByStatus(RentalStatus status) {
        List<RentalRequest> rentalRequests = rentalRequestRepo.findByStatus(status);
        return rentalRequests.stream()
                .map(RentalRequestResponse::fromEntity)
                .collect(Collectors.toList());
    }


    public Long countAllRentalRequests() {
        return rentalRequestRepo.count();
    }


    public List<RentalRequestResponse> getRequestsByStatusAndValidation(RentalStatus status, boolean isValidatedCouncilMinister) {
         List<RentalRequest> listRentalRequest= rentalRequestRepo.findByStatusAndIsValidatedCouncilMinister(status, isValidatedCouncilMinister);

         return listRentalRequest.stream()
                .map(RentalRequestResponse::fromEntity)
                .collect(Collectors.toList());

    }


    private void notifyDGAIEUsers(RentalRequest rentalRequestToSend) {
        List<Users> listDgaieUsers = usersRepo.findByRoleAndCanReceiveNewProjectEmail(RoleType.DAIE, true);
        for (Users user : listDgaieUsers) {
            try {
                notificationService.sendRequestNotificationToDgaieUser(user, rentalRequestToSend);
            } catch (Exception e) {
                // Log the exception and continue with the next user
                log.error("Failed to send email to {}: {}", user.getEmail(), e.getMessage());
                throw new InvalidOperationException("Failed to send validate Rental Request email to DGAIE !");
            }
        }
    }

    private void notifyComplementRequestToOrdonnator(RentalRequest rentalRequestToSend) {
        List<Users> listOrdonatorUsers = usersRepo.findByRoleAndCanReceiveNewProjectEmail(RoleType.ORDON, true);
        for (Users user : listOrdonatorUsers) {
            try {
                notificationService.sendRequestNotificationToOrdonatorUser(user, rentalRequestToSend);
            } catch (Exception e) {
                // Log the exception and continue with the next user
                log.error("Failed to send email to {}: {}", user.getEmail(), e.getMessage());
                throw new InvalidOperationException("Failed to send Complement Rental Request email to Ordonnator !");
            }
        }
    }

    private void notifyApprovalRequestToCNOI(RentalRequest rentalRequestToSend) {
        List<Users> listCnoiUsers = usersRepo.findByRoleAndCanReceiveNewProjectEmail(RoleType.CNOI, true);
        for (Users user : listCnoiUsers) {
            try {
                notificationService.sendApprovalRequestToCNOIUser(user, rentalRequestToSend);
            } catch (Exception e) {
                // Log the exception and continue with the next user
                log.error("Failed to send email to {}: {}", user.getEmail(), e.getMessage());
                throw new InvalidOperationException("Failed to send Approval Rental Request email to Ordonnator !");
            }
        }
    }
   
    private void notifyApprovalRequestToOrdonnator(RentalRequest rentalRequestToSend) {
        List<Users> listOrdonatorUsers = usersRepo.findByRoleAndCanReceiveNewProjectEmail(RoleType.ORDON, true);
        for (Users user : listOrdonatorUsers) {
            try {
                notificationService.sendApprovalRequestToOrdonnatorUser(user, rentalRequestToSend);
            } catch (Exception e) {
                // Log the exception and continue with the next user
                log.error("Failed to send email to {}: {}", user.getEmail(), e.getMessage());
                throw new InvalidOperationException("Failed to send Approval Rental Request email to Ordonnator !");
            }
        }
    }

    private void notifyRejectRequestToDgaie(RentalRequest rentalRequestToSend) {
        List<Users> listOrdonatorUsers = usersRepo.findByRoleAndCanReceiveNewProjectEmail(RoleType.DAIE, true);
        for (Users user : listOrdonatorUsers) {
            try {
                notificationService.sendRejectRequestToDgaieUser(user, rentalRequestToSend);
            } catch (Exception e) {
                // Log the exception and continue with the next user
                log.error("Failed to send email to {}: {}", user.getEmail(), e.getMessage());
                throw new InvalidOperationException("Failed to send Approval Rental Request email to Ordonnator !");
            }
        }
    }
  
    private void rentalRejectRequestToOrdonnator(RentalRequest rentalRequestToSend) {
        List<Users> listOrdonatorUsers = usersRepo.findByRoleAndCanReceiveNewProjectEmail(RoleType.ORDON, true);
        for (Users user : listOrdonatorUsers) {
            try {
                notificationService.sendRejectRequestToOrdonnatorUser(user, rentalRequestToSend);
            } catch (Exception e) {
                // Log the exception and continue with the next user
                log.error("Failed to send email to {}: {}", user.getEmail(), e.getMessage());
                throw new InvalidOperationException("Failed to send Approval Rental Request email to Ordonnator !");
            }
        }
    }
 
    private void rentalHeldRequestToOrdonnator(RentalRequest rentalRequestToSend) {
        List<Users> listOrdonatorUsers = usersRepo.findByRoleAndCanReceiveNewProjectEmail(RoleType.ORDON, true);
        for (Users user : listOrdonatorUsers) {
            try {
                notificationService.sendHeldRequestToOrdonnatorUser(user, rentalRequestToSend);
            } catch (Exception e) {
                // Log the exception and continue with the next user
                log.error("Failed to send email to {}: {}", user.getEmail(), e.getMessage());
                throw new InvalidOperationException("Failed to send Approval Rental Request email to Ordonnator !");
            }
        }
    }
  
    private void notifyHeldRequestToDgaie(RentalRequest rentalRequestToSend) {
        List<Users> listDgaieUsers = usersRepo.findByRoleAndCanReceiveNewProjectEmail(RoleType.DAIE, true);
        for (Users user : listDgaieUsers) {
            try {
                notificationService.sendHeldRequestToDgaierUser(user, rentalRequestToSend);
            } catch (Exception e) {
                // Log the exception and continue with the next user
                log.error("Failed to send email to {}: {}", user.getEmail(), e.getMessage());
                throw new InvalidOperationException("Failed to send Approval Rental Request email to Ordonnator !");
            }
        }
    }





}
