package com.telia.Lease_management.controllers.implementApi;

import org.springframework.web.bind.annotation.RestController;

import com.telia.Lease_management.controllers.interfaceApi.UserApi;
import com.telia.Lease_management.dto.requests.MinistryDto;
import com.telia.Lease_management.dto.requests.UserDto;
import com.telia.Lease_management.dto.responses.AuthResponse;
import com.telia.Lease_management.dto.responses.EntityResponse;
import com.telia.Lease_management.services.UsersService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@RestController
public class UsersControleur implements UserApi{

    private UsersService usersService;


    public ResponseEntity<AuthResponse> saveUser( UserDto dto) {
        log.info("Inscription");
        try {
            if (this.usersService.createUser(dto)){
                return ResponseEntity.ok(new AuthResponse(HttpStatus.OK.value(), "Utilisateur crée avec succès!"));
            } else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(HttpStatus.BAD_REQUEST.value(), "Formulaire invalide"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(HttpStatus.UNAUTHORIZED.value(), "Une erreur est survenue !"));
        }
    }


    @Override 
    public ResponseEntity<EntityResponse<UserDto>> updateUserProfile(UserDto dto) {
        EntityResponse<UserDto> response = new EntityResponse<>();

        try {
            UserDto userUpdated = usersService.updateUserProfile(dto);
            response.setStatus(HttpStatus.CREATED.value());
            response.setMessage("Profil mis à jour avec succès");
            response.setData(userUpdated);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setMessage("Une erreur est survenue : " + e.getMessage());
            response.setData(null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        

    }


    @Override
    public List<UserDto> findAllUsers() {
        return usersService.findAll();
    }


    @Override
    public UserDto findUserById(Long id) {
        return usersService.findById(id);
    }
    
    
    
}
