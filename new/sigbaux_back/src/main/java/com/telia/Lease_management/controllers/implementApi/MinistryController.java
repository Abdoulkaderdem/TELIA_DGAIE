package com.telia.Lease_management.controllers.implementApi;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.telia.Lease_management.controllers.interfaceApi.MinistryApi;
import com.telia.Lease_management.dto.requests.MinistryDto;
import com.telia.Lease_management.dto.responses.AuthResponse;
import com.telia.Lease_management.entity.Ministry;
import com.telia.Lease_management.services.MinistryService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
public class MinistryController implements MinistryApi{
    
    private MinistryService ministryService;
    
    @Override
    public ResponseEntity<AuthResponse> createMinistry(MinistryDto dto) {
        try {
            
            if (this.ministryService.createMinistry(dto)){
                return ResponseEntity.ok(new AuthResponse(HttpStatus.OK.value(), "Ministère crée avec succès!"));
            } else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(HttpStatus.BAD_REQUEST.value(), "Formulaire invalide"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(HttpStatus.UNAUTHORIZED.value(), "Une erreur est survenue !"));
        }
    }

    @Override
    public List<MinistryDto> findAllMinistry() {
        return ministryService.findAll();
    }

    @Override
    public MinistryDto findMinistryById(Long id) {
        return ministryService.findById(id);
    }
    

    @Override
    public ResponseEntity<MinistryDto> updateMinistry(Long id, MinistryDto ministryDto) {
        ministryDto.setId(id); // Assure que l'ID dans le DTO correspond à celui dans l'URL

        MinistryDto updatedMinistry = ministryService.updateMinistry(ministryDto);
        return ResponseEntity.ok(updatedMinistry);
    }

    
    @Override
    @PutMapping("/{id}/activate")
    public ResponseEntity<MinistryDto> activateMinistry(@PathVariable Long id) {
        MinistryDto updatedMinistry = ministryService.setActivationStatus(id, true);
        return ResponseEntity.ok(updatedMinistry);
    }
    
    @Override
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<MinistryDto> deactivateMinistry(@PathVariable Long id) {
        MinistryDto updatedMinistry = ministryService.setActivationStatus(id, false);
        return ResponseEntity.ok(updatedMinistry);
    }

}
