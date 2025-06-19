package com.telia.Lease_management.controllers.implementApi;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.telia.Lease_management.controllers.interfaceApi.BuildingApi;
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
import com.telia.Lease_management.services.rental_offer.BuildingService;
import com.telia.Lease_management.services.rental_offer.RentalOfferService;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
public class BuildingController implements BuildingApi{

    private BuildingService buildingService;
    private RentalOfferService rentalOfferService;

    @Override
    public ResponseEntity<BuildingResponse> createBuilding(BuildingDto buildingDto) {
        try {
            BuildingResponse createdBuilding = buildingService.createBuilding(buildingDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBuilding);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Override
    public ResponseEntity<BuildingResponse> getBuildingById(Long id) {
        BuildingResponse building = buildingService.getBuildingById(id);
        if (building != null) {
            return new ResponseEntity<>(building, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<BuildingResponse> updateBuilding(Long id, BuildingDto buildingDto) {
        try {
            BuildingResponse updatedBuilding = buildingService.updateBuilding(id, buildingDto);
            return ResponseEntity.status(HttpStatus.OK).body(updatedBuilding);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

    @Override
    public ResponseEntity<List<BuildingResponse>> getAllBuildings() {
        List<BuildingResponse> buildings = buildingService.getAllBuildings();
        return new ResponseEntity<>(buildings, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> addOfferAndCharacteristicsToBuilding(Long buildingId,
    List<OfferAndCharacteristicsDto> listOfferAndCharacteristicsDto) {
                buildingService.addOfferAndCharacteristicsToBuilding(buildingId, listOfferAndCharacteristicsDto);
                return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> removeOfferAndCharacteristicsFromBuilding(Long buildingId,
            Long offerAndCharacteristicsId) {
        buildingService.removeOfferAndCharacteristicsFromBuilding(buildingId, offerAndCharacteristicsId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<BuildingForInspection>> getAllBuildingsForInspection() {
        List<BuildingForInspection> listBuildingsForInspection = buildingService.getAllBuildingsInspection();
        return new ResponseEntity<>(listBuildingsForInspection, HttpStatus.OK);
    }
    

    @Override
    public ResponseEntity<EntityResponse<List<BuildingResponse>>> filterRentalOffersByBuilding(Long rentalRequestId) {

        EntityResponse<List<BuildingResponse>> responseMatchedWithRentalRequest = new EntityResponse<>();
        try {
            // Filter rental offers by rental request
            List<BuildingResponse> filteredOffers = rentalOfferService.filterOffersByRentalRequest(rentalRequestId);
            responseMatchedWithRentalRequest.setStatus(HttpStatus.ACCEPTED.value());
            responseMatchedWithRentalRequest.setMessage("Demande(s) approuvée(s) trouvée(s) avec succès!");
            responseMatchedWithRentalRequest.setData(filteredOffers);
            return ResponseEntity.ok(responseMatchedWithRentalRequest);
        }catch (RuntimeException e) {
            responseMatchedWithRentalRequest.setStatus(HttpStatus.UNAUTHORIZED.value());
            responseMatchedWithRentalRequest.setMessage("Demande introuvable : " + e.getMessage());
            responseMatchedWithRentalRequest.setData(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMatchedWithRentalRequest);
        } catch (Exception e) {
            responseMatchedWithRentalRequest.setStatus(HttpStatus.UNAUTHORIZED.value());
            responseMatchedWithRentalRequest.setMessage("Une erreur est survenue : " + e.getMessage());
            responseMatchedWithRentalRequest.setData(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMatchedWithRentalRequest);
        }
    }

    @Override
    public ResponseEntity<AuthResponse> updateBuildingAfterInspection(
            List<BuildingForInspection> listBuildingUpdatedAfterInspection) {

        // Log the content of listBuildingUpdatedAfterInspection
        // for (BuildingForInspection building : listBuildingUpdatedAfterInspection) {
        //     log.info("Building for Inspection: {}", building.toString());
        // }

        AuthResponse response = new AuthResponse();
        try {
            buildingService.updateBuildingsAfterInspection(listBuildingUpdatedAfterInspection);
            response.setStatus(HttpStatus.ACCEPTED.value());
            response.setAnswer("Rapport Inspection ajoutés avec succès !");
            return ResponseEntity.ok(response);
        }  catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setAnswer("Une erreur est survenue : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @Override
    public ResponseEntity<List<BuildingResponse>> getBuildingsByStatus(RentalStatus status) {
        List<BuildingResponse> buildings = buildingService.getBuildingsByStatus(status);
        return ResponseEntity.ok(buildings);
    }

    @Override
    public ResponseEntity<Long> countAllBuildings() {
        long count = buildingService.countAllBuildings();
        return ResponseEntity.ok(count);
    }

    @Override
    public ResponseEntity<List<BuildingResponse>> getMatchedBuildings(Long rentalRequestId) {
        List<BuildingResponse> matchedBuildings =buildingService.getMatchedBuildingsByRentalRequestId(rentalRequestId);

        if (matchedBuildings.isEmpty()) {
            return ResponseEntity.ok(new ArrayList<>());
        }

        return ResponseEntity.ok(matchedBuildings);
    }

    @Override
    public ResponseEntity<BuildingWithProvisionalRentAmount> getBuildingWithProvisionalRentalAmount(
            Long rentalRequestId) {
        
        try {
            BuildingWithProvisionalRentAmount building = buildingService.getBuildingsRentalRequestValidated(rentalRequestId);
            return ResponseEntity.ok(building);
        } catch (RuntimeException e) {
            log.error("Runtime exception occurred: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            log.error("An exception occurred: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Override
    public ResponseEntity<BuildingForInspection> getBuildingAlreadyAmountEstimatedById(Long id) {
        BuildingForInspection building = buildingService.getBuildingEstimatedById(id);
        if (building != null) {
            return new ResponseEntity<>(building, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<AuthResponse> validateProvisionalRentalAmount(Long idBuilding) {
        AuthResponse response = new AuthResponse();
        try {
            buildingService.validateStateAfterCouncilMinisters(idBuilding, true);
            response.setStatus(HttpStatus.ACCEPTED.value());
            response.setAnswer("Validation effectuée avec succès !");
            return ResponseEntity.ok(response);
        }  catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setAnswer("Une erreur est survenue : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @Override
    public ResponseEntity<AuthResponse> rejecteProvisionalRentalAmount(Long idBuilding) {
        AuthResponse response = new AuthResponse();

        try {
            buildingService.validateStateAfterCouncilMinisters(idBuilding, false);
            response.setStatus(HttpStatus.ACCEPTED.value());
            response.setAnswer("Rejet effectué avec succès !");
            return ResponseEntity.ok(response);
        }  catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setAnswer("Une erreur est survenue : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @Override
    public ResponseEntity<BuildingWithProvisionalRentAmount> getBuildingAlreadyEstimatedForUpdateReportCouncilMinister(
            Long idRequest) {
        
        try {
            BuildingWithProvisionalRentAmount building = buildingService.getBuildingsNotExaminedByCouncil(idRequest);
            return ResponseEntity.ok(building);
        } catch (RuntimeException e) {
            log.error("Runtime exception occurred: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            log.error("An exception occurred: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



    

    
}
