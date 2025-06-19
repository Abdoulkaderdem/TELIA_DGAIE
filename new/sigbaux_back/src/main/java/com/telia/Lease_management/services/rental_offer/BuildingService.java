package com.telia.Lease_management.services.rental_offer;

import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.telia.Lease_management.dto.requests.BuildingDto;
import com.telia.Lease_management.dto.requests.OfferAndCharacteristicsDto;
import com.telia.Lease_management.dto.requests.RentalCharacteristicsDto;
import com.telia.Lease_management.dto.responses.BuildingForInspection;
import com.telia.Lease_management.dto.responses.BuildingResponse;
import com.telia.Lease_management.dto.responses.BuildingWithProvisionalRentAmount;
import com.telia.Lease_management.dto.responses.EntityBooleanResponse;
import com.telia.Lease_management.dto.responses.OfferAndCharacteristicsResponse;
import com.telia.Lease_management.entity.Users;
import com.telia.Lease_management.entity.common.BuildingStanding;
import com.telia.Lease_management.entity.common.Locality;
import com.telia.Lease_management.entity.common.RentalStatus;
import com.telia.Lease_management.entity.common.RoleType;
import com.telia.Lease_management.entity.common.TypeBuilding;
import com.telia.Lease_management.entity.common.Zone;
import com.telia.Lease_management.entity.rental_offer.Building;
import com.telia.Lease_management.entity.rental_offer.BuildingStandingEntity;
import com.telia.Lease_management.entity.rental_offer.OfferAndCharacteristics;
import com.telia.Lease_management.entity.rental_offer.RentalCharacteristics;
import com.telia.Lease_management.entity.rental_offer.TypeOfferAndCharacteristics;
import com.telia.Lease_management.entity.rental_request.RentalRequest;
import com.telia.Lease_management.repository.UsersRepository;
import com.telia.Lease_management.repository.rental_offer.BuildingRepository;
import com.telia.Lease_management.repository.rental_offer.BuildingStandingEntityRepository;
import com.telia.Lease_management.repository.rental_offer.OfferAndCharacteristicsRepository;
import com.telia.Lease_management.repository.rental_offer.RentalCharacteristicsRepository;
import com.telia.Lease_management.repository.rental_request.RentalRequestRepository;
import com.telia.Lease_management.services.CNOI.RentPriceService;
import com.telia.Lease_management.utils.NotificationService;
import com.telia.Lease_management.utils.ValidateForms;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class BuildingService {

    private BuildingRepository buildingRepository;
    private OfferAndCharacteristicsRepository offerAndCharacteristicsRepository;
    private OfferAndCharacteristicsService offerAndCharacteristicsService;
    private RentalCharacteristicsRepository rentalCharacteristicsRepo;
    private BuildingStandingEntityRepository buildingStandingEntityRepo;
    private RentalRequestRepository rentalRequestRepo;
    private RentPriceService rentPriceService;
    private UsersRepository usersRepo;
    private NotificationService notificationService;



    @Transactional
    public BuildingResponse createBuilding(BuildingDto buildingDto) throws Exception{

        //Check if the building form is valid
        List<String> errors= ValidateForms.validateBuilding(buildingDto);
        if (!errors.isEmpty()){
            log.error("Building is not valid {}:", buildingDto);
            log.error("Liste des erreurs {}: ", errors);
            throw new IllegalArgumentException("Building is not valid !"); 
        }     
        
        //Conversion
        Building building = BuildingDto.toEntityBuilding(buildingDto);

        //Check first step,  if OfferAndCharacteristics is new, then save it 
        List<OfferAndCharacteristics> listOfferAndCharacteristicsToAdd = new ArrayList<>();

        if (!buildingDto.getListOfferAndCharacteristicsDto().isEmpty() && buildingDto.getListOfferAndCharacteristicsDto()!= null){
            for (OfferAndCharacteristicsDto offerAndCharacteristicsDto: buildingDto.getListOfferAndCharacteristicsDto()){
                //Convert from DTO
                OfferAndCharacteristics oAndCharacteristics = OfferAndCharacteristicsDto.toEntity(offerAndCharacteristicsDto);
                RentalCharacteristics rentalCharacteristics = oAndCharacteristics.getCharacteristics();

                //Check first RentalCharacteristicsDto
                if (rentalCharacteristics.getId() == null){
                    // if ID is null, it is a new Characteristics Rental To Save.
                    rentalCharacteristicsRepo.save(rentalCharacteristics);
                    oAndCharacteristics.setCharacteristics(rentalCharacteristics);
                }else {
                    // If not, looking for the existing Characteristics Rental
                    Optional<RentalCharacteristics> existingRenatlCharacteristics = rentalCharacteristicsRepo.findById(rentalCharacteristics.getId());
                    if (existingRenatlCharacteristics.isPresent()) {
                        oAndCharacteristics.setCharacteristics(rentalCharacteristics);
                    } else {
                        throw new IllegalArgumentException("Characteristics Rental with id " + rentalCharacteristics.getId() + " not found");
                    }
                }

                //Check second OfferAndCharacteristics
                if (oAndCharacteristics.getId() == null) {
                    // if ID is null, it is a new OfferAndCharacteristics To Save.
                    offerAndCharacteristicsRepository.save(oAndCharacteristics);
                }else {
                    // If not, looking for the existing OfferAndCharacteristics
                    Optional<OfferAndCharacteristics> existingOandCharacteristics = offerAndCharacteristicsRepository.findById(oAndCharacteristics.getId());
                    if (existingOandCharacteristics.isPresent()) {
                        existingOandCharacteristics.get().setValues(oAndCharacteristics.getValues());
                        existingOandCharacteristics.get().setCharacteristics(oAndCharacteristics.getCharacteristics());
                        //save now
                        oAndCharacteristics= offerAndCharacteristicsRepository.save(existingOandCharacteristics.get());
                    } else {
                        throw new IllegalArgumentException("OfferAndCharacteristics with id " + oAndCharacteristics.getId() + " not found");
                    }
                }
                listOfferAndCharacteristicsToAdd.add(oAndCharacteristics);
            }
        }
        // else{
        //     throw new Exception("OfferAndCharacteristics is not found !");
        // }

        building.setStatus(RentalStatus.AVAILABLE);
        Building savedBuilding = buildingRepository.save(building);
        return BuildingResponse.fromEntity(savedBuilding);
    }



    @Transactional(readOnly = true)
    public BuildingResponse  getBuildingById(Long id) {
        Optional<Building> building = buildingRepository.findById(id);
        //log.info("Nombre d'OfferAndCharacteristics chargés: {}", building.get().getListOfferAndCharacteristics().size());
        return building.map(BuildingResponse::fromEntity).orElse(null);
    }



    public BuildingResponse updateBuilding(Long id, BuildingDto buildingDto) {
         Building buildingToUpdate = buildingRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("The building with id: " + id + " not found"));
        
        Building buildingConvert = BuildingDto.toEntityBuilding(buildingDto);

        // Update building fields 
        buildingToUpdate.setTypeBuilding(buildingDto.getTypeBuilding());
        buildingToUpdate.setTypePropertyTitle(buildingDto.getTypePropertyTitle());
        buildingToUpdate.setBuildingValue(buildingDto.getBuildingValue());
        buildingToUpdate.setBuildingArea(buildingDto.getBuildingArea());
        buildingToUpdate.setOtherInformation(buildingDto.getOtherInformation());
        buildingToUpdate.setRegion(buildingDto.getRegion());
        buildingToUpdate.setProvince(buildingDto.getProvince());
        buildingToUpdate.setCommune(buildingDto.getCommune());
        buildingToUpdate.setCity(buildingDto.getCity());
        buildingToUpdate.setDistrict(buildingDto.getDistrict());
        buildingToUpdate.setSector(buildingDto.getSector());
        buildingToUpdate.setSection(buildingDto.getSection());
        buildingToUpdate.setIlot(buildingDto.getIlot());
        buildingToUpdate.setPlot(buildingDto.getPlot());
        buildingToUpdate.setStreet(buildingDto.getStreet());
        buildingToUpdate.setDoornumber(buildingDto.getDoornumber());
        buildingToUpdate.setGeolocation(buildingDto.getGeolocation());
        buildingToUpdate.setRentPrice(buildingDto.getRentPrice());
        buildingToUpdate.setCode(buildingDto.getCode());
        buildingToUpdate.setNumberOfRoom(buildingDto.getNumberOfRoom());
        buildingToUpdate.setNumberOfRoomMeeting(buildingDto.getNumberOfRoomMeeting());
        buildingToUpdate.setNumberOfBathroom(buildingDto.getNumberOfBathroom());
        
        // Update building Standing
        if (!buildingDto.getListBuildingStanding().isEmpty()){
            //log.info("********Tableau: {}", buildingDto.getListBuildingStanding());
            buildingToUpdate.getListBuildingStandings().clear();
            
            List<BuildingStandingEntity> buildingStandingEntities = buildingDto.getListBuildingStanding().stream()
            .map(standing -> {
                BuildingStandingEntity entity = new BuildingStandingEntity();
                // log.info("********standing: {}", standing);
                entity.setBuildingStanding(standing);
                entity.setBuilding(buildingToUpdate);
                buildingStandingEntityRepo.save(entity);
                return entity;
            })
            .collect(Collectors.toList());
            
            buildingToUpdate.setListBuildingStandings(buildingStandingEntities);
        }

                // Save changes
        Building buildingTosave = buildingRepository.save(buildingToUpdate);
        
        //return BuildingResponse.fromEntity(buildingTosave);
         // Log the saved building
            System.out.println("Saved Building: " + buildingToUpdate);

           // BuildingResponse response = BuildingResponse.fromEntity(buildingToUpdate);
            BuildingResponse response = BuildingResponse.fromEntity(buildingToUpdate);
            
            // Log the response before returning
            System.out.println("BuildingResponse: " + response);

            return response;
    }


    @Transactional(readOnly = true)
    public List<BuildingResponse> getAllBuildings() {
        return buildingRepository.findAll().stream()
        .map(BuildingResponse::fromEntity)
        .collect(Collectors.toList());
    }


    @Transactional
    public void deleteBuildingById(Long id) {
        buildingRepository.deleteById(id);
    }

     
    public void addOfferAndCharacteristicsToBuilding(Long buildingId, List<OfferAndCharacteristicsDto> listOfferAndCharDto) {
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new RuntimeException("Building not found with id: " + buildingId));

        List<OfferAndCharacteristics> listOfferAndCharacteristics = new ArrayList<>();

        for(OfferAndCharacteristicsDto offerAndCharacteristicsDto: listOfferAndCharDto){
            OfferAndCharacteristics offerAndCharacteristics = OfferAndCharacteristicsDto.toEntity(offerAndCharacteristicsDto);
            offerAndCharacteristics.setBuilding(building);
            offerAndCharacteristicsRepository.save(offerAndCharacteristics);

            listOfferAndCharacteristics.add(offerAndCharacteristics);
        }
        
        building.getListOfferAndCharacteristics().addAll(listOfferAndCharacteristics);
        buildingRepository.save(building);
    }

    public void removeOfferAndCharacteristicsFromBuilding(Long buildingId, Long offerAndCharacteristicsId) {
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new RuntimeException("Building not found with id: " + buildingId));

        OfferAndCharacteristics offerAndCharacteristics = offerAndCharacteristicsRepository.findById(offerAndCharacteristicsId)
                .orElseThrow(() -> new RuntimeException("OfferAndCharacteristics not found with id: " + offerAndCharacteristicsId));

        building.getListOfferAndCharacteristics().remove(offerAndCharacteristics);
        offerAndCharacteristicsRepository.delete(offerAndCharacteristics);
    }


    public List<BuildingForInspection> getAllBuildingsInspection() {
        List<Building> retainedBuildings = buildingRepository.findBuildingsByStatus(RentalStatus.RETAINED);
    
        return retainedBuildings.stream()
                .map(BuildingForInspection::fromEntity)
                .collect(Collectors.toList());
    }

    public void updateBuildingsAfterInspection(
            List<BuildingForInspection> listBuildingUpdatedAfterInspection) {

        for (BuildingForInspection buildingAfterInspection : listBuildingUpdatedAfterInspection) {

            Building buildingToUpdate = buildingRepository.findById(buildingAfterInspection.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Building not found with id " + buildingAfterInspection.getId()));
            
            //Check attributes
            validateInspectionAttributes(buildingAfterInspection);
            // Mise à jour des attributs du bâtiment
            buildingToUpdate = updateBuildingAttributes(buildingToUpdate, buildingAfterInspection);
            log.info("Le Statut Phase 2 de l'inspection enregistré est {}: ",  buildingToUpdate.getStatus());

            // Traitement des Offres et Caractéristiques
            if (buildingAfterInspection.getListOfferAndCharacteristicsDto() != null && !buildingAfterInspection.getListOfferAndCharacteristicsDto().isEmpty()){
                List<OfferAndCharacteristicsResponse> oListResponse = updateOfferAndCharacteristics(buildingToUpdate, buildingAfterInspection.getListOfferAndCharacteristicsDto());
                
                // Set the building-specific list, as it's Characteristics record during the Inspection
                buildingToUpdate.setListOfferAndCharacteristics( OfferAndCharacteristicsResponse.toEntities(oListResponse));
                //Save Building
                buildingRepository.save(buildingToUpdate);
            }

            log.info("Le Statut Phase 3 de l'inspection enregistré est {}: ",  buildingToUpdate.getStatus());
            
        }
    }

    
    private  List<OfferAndCharacteristicsResponse> updateOfferAndCharacteristics(Building buildingToUpdate, List<OfferAndCharacteristicsResponse> listOAndCharacteristicsResponse) {
            
        List<OfferAndCharacteristics> listOAndCharacteristics = OfferAndCharacteristicsResponse.toEntities(listOAndCharacteristicsResponse);
        List<OfferAndCharacteristicsResponse> lOfferAndCharacteristicsResponses = new ArrayList<>();

        for (OfferAndCharacteristics oAndCharacteristics : listOAndCharacteristics) {
            oAndCharacteristics.setBuilding(buildingToUpdate);
            oAndCharacteristics.setType(TypeOfferAndCharacteristics.INSPECTION);
                
            OfferAndCharacteristicsResponse oResponse= offerAndCharacteristicsService.createOfferAndCharacteristics(OfferAndCharacteristicsDto.fromEntity(oAndCharacteristics));
            lOfferAndCharacteristicsResponses.add(oResponse);
        }

        return lOfferAndCharacteristicsResponses;
    }


    private Building updateBuildingAttributes(Building buildingToUpdate, BuildingForInspection buildingInspected) {
        if (buildingInspected.getStatus() == RentalStatus.MATCHED || buildingInspected.getStatus() == RentalStatus.NO_CONFORM){
            if (buildingInspected.getStatus() == RentalStatus.MATCHED){
                buildingToUpdate.setStatus(RentalStatus.MATCHED);
            }else{              
                buildingToUpdate.setStatus(RentalStatus.NO_CONFORM);
            }
           
        }else{
            throw new IllegalArgumentException("At this step Building can only have 'MATCHED' or 'NO_CONFORM' status! Not this:  " + buildingInspected.getStatus());
        }
        
        buildingToUpdate.setZone(buildingInspected.getZone());
        buildingToUpdate.setLocality(buildingInspected.getLocality());
        buildingToUpdate.setTypeBuilding(buildingInspected.getTypeBuilding());
        buildingToUpdate.setInspectionObservation(buildingInspected.getInspectionObservation());
        buildingToUpdate.setBuildingArea(buildingInspected.getBuildingArea());
        // buildingToUpdate.setStatus(buildingInspected.getStatus());

        //Retreive the rental request of attached to this building
        RentalRequest rentalRequestForBuilding = buildingToUpdate.getRentalRequest();

       if(buildingInspected.getStatus()==RentalStatus.MATCHED){
            rentalRequestForBuilding.setStatus(RentalStatus.BUILDING_CONFORM); //Update rental request Status
            rentalRequestRepo.save(rentalRequestForBuilding); //save rental request
            buildingRepository.save(buildingToUpdate);
        }else{
            //Do nothing the status of rental request remain RentalStatus.BATIMENT_FOUND, waiting other building to be attached
            buildingRepository.save(buildingToUpdate);
        }
        
        log.info("Le Statut Phase 1 de l'inspection enregistré est {}: ",  buildingToUpdate.getStatus());
        return buildingToUpdate;

    }


    private void validateInspectionAttributes(BuildingForInspection buildingInspected) {
        if (buildingInspected.getInspectionObservation() == null || buildingInspected.getInspectionObservation().isEmpty()) {
            throw new IllegalArgumentException("Inspection observation is required for building with id: " + buildingInspected.getId());
        }

        if (buildingInspected.getZone() == null || !EnumSet.allOf(Zone.class).contains(buildingInspected.getZone())) {
            throw new IllegalArgumentException("Zone for building with id: " + buildingInspected.getId() + " is incorrect or missing!");
        }

        if (buildingInspected.getLocality() == null || !EnumSet.allOf(Locality.class).contains(buildingInspected.getLocality())) {
            throw new IllegalArgumentException("Locality for building with id: " + buildingInspected.getId() + " is incorrect or missing!");
        }

        if (buildingInspected.getTypeBuilding() == null || !EnumSet.allOf(TypeBuilding.class).contains(buildingInspected.getTypeBuilding())) {
            throw new IllegalArgumentException("TypeBuilding for building with id: " + buildingInspected.getId() + " is incorrect or missing!");
        }

        if (buildingInspected.getBuildingArea() == null || buildingInspected.getBuildingArea().isEmpty()) {
            throw new IllegalArgumentException("BuildingArea for building with id: " + buildingInspected.getId() + " is incorrect or missing!");
        }

    }



    //************************************************** */
    // private BuildingResponse toResponse(Building building) {
    //     List<OfferAndCharacteristicsResponse> offerAndCharacteristicsResponses = building.getListOfferAndCharacteristics().stream()
    //             .map(this::toResponse)
    //             .collect(Collectors.toList());
        
    //     List<OfferAndCharacteristicsResponse> landlordOfferAndCharacteristicsResponses = building.getListLandlordOfferCharacteristics().stream()
    //             .map(OfferAndCharacteristicsResponse::fromEntity)
    //             .collect(Collectors.toList());    

    //        List<BuildingStanding> buildingStandings = building.getListBuildingStandings().stream()
    //         .map(BuildingStandingEntity::getBuildingStanding) 
    //         .collect(Collectors.toList());
    
    //             return new BuildingResponse(
    //             building.getId(),
    //             building.getTypeBuilding(),
    //             building.getTypePropertyTitle(),
    //             building.getStatus(),
    //             building.getBuildingValue(),
    //             building.getBuildingArea(),
    //             building.getOtherInformation(),
    //             building.getRegion(),
    //             building.getProvince(),
    //             building.getCommune(),
    //             building.getCity(),
    //             building.getDistrict(),
    //             building.getSector(),
    //             building.getSection(),
    //             building.getIlot(),
    //             building.getPlot(),
    //             building.getStreet(),
    //             building.getDoornumber(),
    //             building.getGeolocation(),
    //             building.getRentPrice(),
    //             building.getCode(),           
    //             building.getNumberOfRoom(),
    //             building.getNumberOfRoomMeeting(),
    //             building.getNumberOfBathroom(),
    //             offerAndCharacteristicsResponses,
    //             landlordOfferAndCharacteristicsResponses,
    //             buildingStandings
    //     );
    // }
    //************************************************** */

    // private OfferAndCharacteristicsResponse toResponse(OfferAndCharacteristics entity) {
    //     Long buildingId = (entity.getBuilding() != null) ? entity.getBuilding().getId() : null;
    //     return new OfferAndCharacteristicsResponse(
    //             entity.getId(),
    //             entity.getValues(),
    //             // entity.getType(),
    //             RentalCharacteristicsDto.fromEntityRentalCharacteristics(entity.getCharacteristics()),
    //             buildingId
    //     );
    // }



    public List<BuildingResponse> getBuildingsByStatus(RentalStatus status) {
        List<Building> buildings = buildingRepository.findByStatus(status);
        return buildings.stream()
                        .map(BuildingResponse::fromEntity)
                        .collect(Collectors.toList());
    }



    public long countAllBuildings() {
        return buildingRepository.count();
    }


    public BuildingWithProvisionalRentAmount saveProvisionalRentAmount(Long buildingId, Long provisionalRentAmount) {
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new IllegalArgumentException("Building not found with id: " + buildingId));
       
        //Retreive the rental request for building
        RentalRequest rentalRequest = building.getRentalRequest();
        if (rentalRequest == null) {
            throw new IllegalStateException("Rental request not found, for this contract !");
        }

        if (building.getStatus()!=RentalStatus.MATCHED){
            throw new IllegalArgumentException("Building must have 'MATCHED' status to save provisional Rent Amount! Not this: " + building.getStatus());
        }
        
        if (rentalRequest.getStatus()!=RentalStatus.BUILDING_CONFORM){
            throw new IllegalArgumentException("RentalRequest must have 'BUILDING_CONFORM' status to save provisional Rent Amount! Not this: " + rentalRequest.getStatus());
        }

        //Check if provisional rent amount is higher than estimate rent amount
        Double estimateRent= rentPriceService.calculateEstimatedRent(buildingId); 
        if (provisionalRentAmount > estimateRent){
            throw new IllegalStateException(" The provisional rent amount cannot be higher than estimate rent amount !");
        }

        // Detach all buildings from the rental request
        List<Building> buildingsToDetach = buildingRepository.findByRentalRequestId(rentalRequest.getId());
        for (Building b : buildingsToDetach) {
            b.setRentalRequest(null); //Detach building
            b.setStatus(RentalStatus.AVAILABLE); // Reset the status 
            buildingRepository.save(b); // Save the updated building
        }

        // Update the rental request status and save it
        rentalRequest.setStatus(RentalStatus.SATISFACTORY);
        rentalRequestRepo.save(rentalRequest);

         // Attach the current building to the rental request
        building.setProvisionalRentAmount(provisionalRentAmount); //set Amount rent
        building.setStatus(RentalStatus.MATCHED);  //Because when it was detached it took on the AVAILABLE status
        building.setRentalRequest(rentalRequest); //Attach to rental request
        buildingRepository.save(building);

        BuildingWithProvisionalRentAmount buildingProvisionalRentAmount = new BuildingWithProvisionalRentAmount();
        buildingProvisionalRentAmount.setIdBuilding(buildingId);
        buildingProvisionalRentAmount.setProvisionalRentAmount(provisionalRentAmount);

        NotifySendRequestToCouncilMinisterToOrdonnator(rentalRequest);  //Send mail to Ordonnateurfor sending request to Council Minister
        NotifySendRequestToCouncilMinisterToDgaie(rentalRequest);  //Send mail to DGAIE for sending request to Council Minister

        return buildingProvisionalRentAmount;
    }


    public List<BuildingWithProvisionalRentAmount> getBuildingsWithSatisfactoryRentalRequest() {
        List<Building> listBuildings = buildingRepository.findByRentalRequestStatus(RentalStatus.SATISFACTORY);
    
        if (!listBuildings.isEmpty()) {
            return listBuildings.stream()
                .map(building -> {
                    BuildingWithProvisionalRentAmount buildingProvisionalRentAmount = new BuildingWithProvisionalRentAmount();
                    buildingProvisionalRentAmount.setIdBuilding(building.getId());
                    buildingProvisionalRentAmount.setProvisionalRentAmount(building.getProvisionalRentAmount());
                    return buildingProvisionalRentAmount;
                })
                .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }



    public List<BuildingResponse> getMatchedBuildingsByRentalRequestId(Long rentalRequestId) {
        List<Building> listBuilding= buildingRepository.findByRentalRequestIdAndStatus(rentalRequestId, RentalStatus.MATCHED);

        return listBuilding.stream()
            .map(BuildingResponse::fromEntity)
            .collect(Collectors.toList());
    }



    public BuildingForInspection getBuildingEstimatedById(Long id) {
        Optional<Building> building = buildingRepository.findById(id);
        //log.info("Nombre d'OfferAndCharacteristics chargés: {}", building.get().getListOfferAndCharacteristics().size());
        return building.map(BuildingForInspection::fromEntity).orElse(null);

    }


    @Transactional
    public void validateStateAfterCouncilMinisters(Long idBuilding, boolean isValid) {
        Building building = buildingRepository.findById(idBuilding)
            .orElseThrow(() -> new IllegalArgumentException("Building not found with id: " + idBuilding));
        
        if (building.getStatus()!= RentalStatus.MATCHED){
            throw new IllegalArgumentException("Building must status 'MATCHED' to be treat after Council Ministers. Not this: " + building.getStatus());
        }
        if (building.getProvisionalRentAmount() < 1000){
            throw new IllegalArgumentException("Building must status correct Provisional Rent Amount to be treat after Council Ministers. Not this: " + building.getProvisionalRentAmount());
        }

        //Retreive the rentalRequest
        RentalRequest rentalRequest = building.getRentalRequest();
        if (rentalRequest == null) {
            new IllegalArgumentException("Rental Request not found for Building with id: " + idBuilding);
        }
        if (rentalRequest.getStatus()!= RentalStatus.SATISFACTORY){
            throw new IllegalArgumentException("Building must status 'SATISFACTORY' to be treat after Council Ministers. Not this: " + rentalRequest.getStatus());
        }

        if (isValid){
            rentalRequest.setValidatedCouncilMinister(true);//Treat rental request
            rentalRequest.setExaminatedCouncilMinister(true);
            rentalRequestRepo.save(rentalRequest);

            building.setValidatedCouncilMinister(true);//Treat rental request
            buildingRepository.save(building);

            //Send mail to inform
            notifyValidateCouncilMinisterToDgaie(rentalRequest);
            notifyValidateCouncilMinisterToOrdonator(rentalRequest);
        }else{
            rentalRequest.setStatus(RentalStatus.REJECTED); //Treat rental request
            rentalRequest.setValidatedCouncilMinister(false);
            rentalRequest.setExaminatedCouncilMinister(true);
            rentalRequestRepo.save(rentalRequest);

            building.setValidatedCouncilMinister(false); //Treat Building
            building.setStatus(RentalStatus.AVAILABLE);// return to initial position, ready to be attached to another rental request
            buildingRepository.save(building);

            //Send mail to inform
            notifyRejectionCouncilMinisterToDgaie(rentalRequest);
            notifyRejectionCouncilMinisterToOrdonator(rentalRequest);
        }


    }


    public BuildingWithProvisionalRentAmount getBuildingsNotExaminedByCouncil(Long idRequest) {
        RentalRequest rentalRequest = rentalRequestRepo.findById(idRequest)
            .orElseThrow(() -> new RuntimeException("Rental request not found with id: " + idRequest));
    
        if (rentalRequest.isValidatedCouncilMinister()) {
            throw new RuntimeException("This Rental Request is already examined in council minister: " + idRequest);
        }
        
        if (rentalRequest.getStatus() != RentalStatus.SATISFACTORY) {
            throw new RuntimeException("Only Rental with 'SATISFACTORY' status can be use there! Not this: " + rentalRequest.getStatus());
        }

        Building building = buildingRepository.findByRentalRequestId(idRequest)
            .stream()
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No buildings found for the rental request id: " + idRequest));

        // Building attachedBuilding = buildingRepository.findBuildingsWithNonNullProvisionalRentAmount(idRequest)
        //     .orElseThrow(() -> new RuntimeException("Building not yet examined in council, not found with this rental request: " + idRequest));       

        
        if (building.getStatus()!=RentalStatus.MATCHED){
            throw new IllegalArgumentException("Building must have 'MATCHED'! Not this: " + building.getStatus());
        }
        
        BuildingWithProvisionalRentAmount buildingProvisionalRentAmount = new BuildingWithProvisionalRentAmount();
        buildingProvisionalRentAmount.setIdBuilding(building.getId());
        buildingProvisionalRentAmount.setProvisionalRentAmount(building.getProvisionalRentAmount());

        return buildingProvisionalRentAmount;

    }


    public BuildingWithProvisionalRentAmount getBuildingsRentalRequestValidated(Long idRentalRequest) {
        RentalRequest rentalRequest = rentalRequestRepo.findById(idRentalRequest)
                .orElseThrow(() -> new RuntimeException("Rental request not found with id: " + idRentalRequest));

        if (!rentalRequest.isValidatedCouncilMinister()) {
            throw new RuntimeException("This Rental Request is not examined in council minister: " + idRentalRequest);
        }
        
        Building attachedBuilding = buildingRepository.findBuildingsWithNonNullProvisionalRentAmount(idRentalRequest)
                .orElseThrow(() -> new RuntimeException("Building not found with this rental request: " + idRentalRequest));       

        BuildingWithProvisionalRentAmount buildingProvisionalRentAmount = new BuildingWithProvisionalRentAmount();
        buildingProvisionalRentAmount.setIdBuilding(attachedBuilding.getId());
        buildingProvisionalRentAmount.setProvisionalRentAmount(attachedBuilding.getProvisionalRentAmount());

        return buildingProvisionalRentAmount;
    }

  
    private void NotifySendRequestToCouncilMinisterToOrdonnator(RentalRequest rentalRequestToSend) {
        List<Users> listOrdonatorUsers = usersRepo.findByRoleAndCanReceiveNewProjectEmail(RoleType.ORDON, true);
        for (Users user : listOrdonatorUsers) {
            try {
                notificationService.sendRequestToCouncilMinisterToOrdonnatorUser(user, rentalRequestToSend);
            } catch (Exception e) {
                // Log the exception and continue with the next user
                log.error("Failed to send email to {}: {}", user.getEmail(), e.getMessage());
                throw new InvalidOperationException("Failed to send Approval Rental Request to Council Minsters email to Ordonnator !");
            }
        }
    }
      
    private void NotifySendRequestToCouncilMinisterToDgaie(RentalRequest rentalRequestToSend) {
        List<Users> listDgaieUsers = usersRepo.findByRoleAndCanReceiveNewProjectEmail(RoleType.DAIE, true);
        for (Users user : listDgaieUsers) {
            try {
                notificationService.sendRequestToCouncilMinisterToDgaieUser(user, rentalRequestToSend);
            } catch (Exception e) {
                // Log the exception and continue with the next user
                log.error("Failed to send email to {}: {}", user.getEmail(), e.getMessage());
                throw new InvalidOperationException("Failed to send Approval Rental Request to Council Minsters email to Ordonnator !");
            }
        }
    }
 
    private void notifyValidateCouncilMinisterToDgaie(RentalRequest rentalRequestToSend) {
        List<Users> listDgaieUsers = usersRepo.findByRoleAndCanReceiveNewProjectEmail(RoleType.DAIE, true);
        for (Users user : listDgaieUsers) {
            try {
                notificationService.sendValidationCouncilMinisterToDgaieUser(user, rentalRequestToSend);
            } catch (Exception e) {
                // Log the exception and continue with the next user
                log.error("Failed to send email to {}: {}", user.getEmail(), e.getMessage());
                throw new InvalidOperationException("Failed to send Validation Rental Request from Council Minsters email to Ordonnator !");
            }
        }
    }
      
    private void notifyValidateCouncilMinisterToOrdonator(RentalRequest rentalRequestToSend) {
        List<Users> listOrdonatorUsers = usersRepo.findByRoleAndCanReceiveNewProjectEmail(RoleType.ORDON, true);
        for (Users user : listOrdonatorUsers) {
            try {
                notificationService.sendValidationCouncilMinisterToOrdonnatorUser(user, rentalRequestToSend);
            } catch (Exception e) {
                // Log the exception and continue with the next user
                log.error("Failed to send email to {}: {}", user.getEmail(), e.getMessage());
                throw new InvalidOperationException("Failed to send Validation Rental Request from Council Minsters email to Ordonnator !");
            }
        }
    }
    
    private void notifyRejectionCouncilMinisterToOrdonator(RentalRequest rentalRequestToSend) {
        List<Users> listOrdonatorUsers = usersRepo.findByRoleAndCanReceiveNewProjectEmail(RoleType.ORDON, true);
        for (Users user : listOrdonatorUsers) {
            try {
                notificationService.sendRejectionCouncilMinisterToOrdonnatorUser(user, rentalRequestToSend);
            } catch (Exception e) {
                // Log the exception and continue with the next user
                log.error("Failed to send email to {}: {}", user.getEmail(), e.getMessage());
                throw new InvalidOperationException("Failed to send Rejection Rental Request from Council Minsters email to Ordonnator !");
            }
        }
    }

    private void notifyRejectionCouncilMinisterToDgaie(RentalRequest rentalRequestToSend) {
        List<Users> listDgaieUsers = usersRepo.findByRoleAndCanReceiveNewProjectEmail(RoleType.DAIE, true);
        for (Users user : listDgaieUsers) {
            try {
                notificationService.sendRejectionCouncilMinisterToDgaieUser(user, rentalRequestToSend);
            } catch (Exception e) {
                // Log the exception and continue with the next user
                log.error("Failed to send email to {}: {}", user.getEmail(), e.getMessage());
                throw new InvalidOperationException("Failed to send Rejection Rental Request from Council Minsters email to Ordonnator !");
            }
        }
    }
    
}