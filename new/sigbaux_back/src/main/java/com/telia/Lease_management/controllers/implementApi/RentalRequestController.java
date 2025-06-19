package com.telia.Lease_management.controllers.implementApi;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.telia.Lease_management.controllers.interfaceApi.RentalRequestApi;
import com.telia.Lease_management.dto.requests.AttachBuildingsToRequestDto;
import com.telia.Lease_management.dto.requests.RentalRequestDto;
import com.telia.Lease_management.dto.requests.UserDto;
import com.telia.Lease_management.dto.responses.AuthResponse;
import com.telia.Lease_management.dto.responses.EntityResponse;
import com.telia.Lease_management.dto.responses.RentalRequestResponse;
import com.telia.Lease_management.entity.common.RentalStatus;
import com.telia.Lease_management.entity.rental_request.RentalRequest;
import com.telia.Lease_management.services.UsersService;
import com.telia.Lease_management.services.rental_request.RentalRequestService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@RestController
@Slf4j
public class RentalRequestController implements RentalRequestApi{
    
    private RentalRequestService rentalRequestService;
    private UsersService usersService;

    @Override
    public ResponseEntity<RentalRequestResponse> create(RentalRequestDto dto) {
        try {
            //log.info("*********************** Received request: {}", dto); // Log de la requête reçue
            return ResponseEntity.status(HttpStatus.CREATED).body(rentalRequestService.createRentalRequest(dto));
        } catch (Exception e) {
            log.error("Error while processing request", e); // Log de l'erreur
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Override
    public ResponseEntity<RentalRequestResponse> getById(Long id) {
        String usernameConnected = usersService.getCurrentUser();
        RentalRequestResponse rentalRequestResponse = rentalRequestService.findById(id, usernameConnected);
        return ResponseEntity.ok(rentalRequestResponse);
    }

    @Override
    public ResponseEntity<List<RentalRequestResponse>> getAll() {
        String usernameConnected = usersService.getCurrentUser();
        List<RentalRequestResponse> listRentalRequest = rentalRequestService.findAll(usernameConnected);
        return ResponseEntity.ok(listRentalRequest);
    }

    @Override
    public ResponseEntity<RentalRequestResponse> update(Long id, RentalRequestDto dto) {
        // dto.setId(id);
        // try {
        //     return ResponseEntity.ok(rentalRequestService.createRentalRequest(dto));
        // } catch (Exception e) {
        //     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        // }
        RentalRequestResponse updatedRentalRequest = rentalRequestService.updateRentalRequest(id, dto);
        if (updatedRentalRequest != null) {
            return ResponseEntity.ok(updatedRentalRequest);
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<EntityResponse<List<RentalRequestDto>>> findApprovalRentalRequestsForStructure(Long structureId) {
        EntityResponse<List<RentalRequestDto>> responseDemandStructure = new EntityResponse<>();
        try {
            List<RentalRequestDto> listRentalRequestDtos = rentalRequestService.getApprovalRentalRequestsForStructure(structureId);
            responseDemandStructure.setStatus(HttpStatus.ACCEPTED.value());
            responseDemandStructure.setMessage("Demande(s) approuvée(s) trouvée(s) avec succès!");
            responseDemandStructure.setData(listRentalRequestDtos);
            return ResponseEntity.ok(responseDemandStructure);
        }catch (RuntimeException e) {
            responseDemandStructure.setStatus(HttpStatus.UNAUTHORIZED.value());
            responseDemandStructure.setMessage("Demande introuvable : " + e.getMessage());
            responseDemandStructure.setData(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDemandStructure);
        } catch (Exception e) {
            responseDemandStructure.setStatus(HttpStatus.UNAUTHORIZED.value());
            responseDemandStructure.setMessage("Une erreur est survenue : " + e.getMessage());
            responseDemandStructure.setData(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDemandStructure);
        }

    }


    @Override
    public ResponseEntity<AuthResponse> validateDemandByOfficer(Long idRentalRequest) {

        AuthResponse response = new AuthResponse();
        try {
            rentalRequestService.validateRequest(idRentalRequest);
            response.setStatus(HttpStatus.ACCEPTED.value());
            response.setAnswer("Validation effectuée avec succès!");
            return ResponseEntity.ok(response);
        }catch (RuntimeException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setAnswer("Demande introuvable : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setAnswer("Une erreur est survenue : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


    @Override
    public ResponseEntity<AuthResponse> sendValidateRentalRequest(Long idRentalRequest) {

        AuthResponse response = new AuthResponse();
        try {
            rentalRequestService.sendRentalRequest(idRentalRequest);
            response.setStatus(HttpStatus.ACCEPTED.value());
            response.setAnswer("Demande envoyée avec succès!");
            return ResponseEntity.ok(response);
        }catch (RuntimeException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setAnswer("Demande introuvable : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setAnswer("Une erreur est survenue : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @Override
    public ResponseEntity<AuthResponse> needComplementRentalRequest(Long idRentalRequest) {
        
        AuthResponse response = new AuthResponse();
        try {
            rentalRequestService.needComplementRentalRequest(idRentalRequest);
            response.setStatus(HttpStatus.ACCEPTED.value());
            response.setAnswer("Demande renvoyée pour complément, effectuée avec succès!");
            return ResponseEntity.ok(response);
        }catch (RuntimeException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setAnswer("Demande introuvable : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setAnswer("Une erreur est survenue : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // @Override
    // public ResponseEntity<AuthResponse> rejectRentalRequest(Long idRentalRequest) {
        
    //     AuthResponse response = new AuthResponse();
    //     try {
    //         rentalRequestService.rejectRentalRequest(idRentalRequest);
    //         response.setStatus(HttpStatus.ACCEPTED.value());
    //         response.setAnswer("Demande à rejeter, effectuée avec succès!");
    //         return ResponseEntity.ok(response);
    //     }catch (RuntimeException e) {
    //         response.setStatus(HttpStatus.UNAUTHORIZED.value());
    //         response.setAnswer("Demande introuvable : " + e.getMessage());
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    //     } catch (Exception e) {
    //         response.setStatus(HttpStatus.UNAUTHORIZED.value());
    //         response.setAnswer("Une erreur est survenue : " + e.getMessage());
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    //     }
    // }

    @Override
    public ResponseEntity<AuthResponse> approveDemandByDGAIE(Long idRentalRequest) {
        AuthResponse response = new AuthResponse();
        try {
            rentalRequestService.approvalDemandByDGAIE(idRentalRequest);
            response.setStatus(HttpStatus.ACCEPTED.value());
            response.setAnswer("Demande approuvée avec succès!");
            return ResponseEntity.ok(response);
        }catch (RuntimeException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setAnswer("Demande introuvable : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setAnswer("Une erreur est survenue : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @Override
    public ResponseEntity<AuthResponse> rejectDemandByCNOI(Long idRentalRequest, String rejectionReason) {
        AuthResponse response = new AuthResponse();
        try {
            rentalRequestService.rejectDemandByCNOI(idRentalRequest, rejectionReason);
            response.setStatus(HttpStatus.ACCEPTED.value());
            response.setAnswer("Demande rejettée avec succès!");
            return ResponseEntity.ok(response);
        }catch (RuntimeException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setAnswer("Demande introuvable : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setAnswer("Une erreur est survenue : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    

    @Override
    public ResponseEntity<AuthResponse> heldDemandByCNOI(Long idRentalRequest) {
        AuthResponse response = new AuthResponse();
        try {
            rentalRequestService.heldDemandByCNOI(idRentalRequest);
            response.setStatus(HttpStatus.ACCEPTED.value());
            response.setAnswer("Demande retenue avec succès!");
            return ResponseEntity.ok(response);
        }catch (RuntimeException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setAnswer("Demande introuvable : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setAnswer("Une erreur est survenue : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @Override
    public ResponseEntity<AuthResponse> attachBuildingsToRentalRequest(
            AttachBuildingsToRequestDto attachBuildingsDto) {

        AuthResponse response = new AuthResponse();
        try {
            rentalRequestService.attachBuildingsToRentalRequest(attachBuildingsDto.getRentalRequestId(), attachBuildingsDto.getBuildingIds());
            response.setStatus(HttpStatus.ACCEPTED.value());
            response.setAnswer("Bâtiments ajoutés avec succès à la demande de location !");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Erreur : {}", e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setAnswer("Erreur : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            log.error("Une erreur est survenue : {}", e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setAnswer("Une erreur est survenue : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Override
    public ResponseEntity<List<RentalRequestResponse>> getRentalRequestsByStatus(RentalStatus status) {
        List<RentalRequestResponse> rentalRequests = rentalRequestService.getRentalRequestsByStatus(status);
        return new ResponseEntity<>(rentalRequests, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Long> countAllRentalRequests() {
        Long count = rentalRequestService.countAllRentalRequests();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @Override
    public List<RentalRequestResponse> getSatisfactoryNotValidatedRequests() {
        return rentalRequestService.getRequestsByStatusAndValidation(RentalStatus.SATISFACTORY, false);
    }

    @Override
    public List<RentalRequestResponse> getSatisfactoryValidatedRequests() {
        return rentalRequestService.getRequestsByStatusAndValidation(RentalStatus.SATISFACTORY, true);
    }


    
    
}
