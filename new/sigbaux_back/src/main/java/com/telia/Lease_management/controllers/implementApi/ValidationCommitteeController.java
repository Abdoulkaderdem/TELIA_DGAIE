package com.telia.Lease_management.controllers.implementApi;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.telia.Lease_management.controllers.interfaceApi.ValidationCommitteeApi;
import com.telia.Lease_management.dto.requests.ValidationCommitteeDto;
import com.telia.Lease_management.dto.responses.AuthResponse;
import com.telia.Lease_management.services.CNOI.ValidationCommitteeService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
public class ValidationCommitteeController implements ValidationCommitteeApi{
    
     private ValidationCommitteeService validationCommitteeService;

    @Override
    public List<ValidationCommitteeDto> getAllCommittees() {
        return validationCommitteeService.getAllCommittees();
    }

    @Override
    public ResponseEntity<ValidationCommitteeDto> getCommitteeById(Long id) {
        ValidationCommitteeDto dto = validationCommitteeService.getCommitteeById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<ValidationCommitteeDto>  createCommittee(ValidationCommitteeDto dto) {
        try {
            ValidationCommitteeDto committeeSaved= validationCommitteeService.createCommittee(dto);

            if (committeeSaved != null){
                return ResponseEntity.ok(committeeSaved);
            } else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(dto);
        }
    }

    @Override
    public ResponseEntity<ValidationCommitteeDto> updateCommittee(ValidationCommitteeDto dto) {
        ValidationCommitteeDto updatedDto = validationCommitteeService.updateCommittee(dto);
        return updatedDto != null ? ResponseEntity.ok(updatedDto) : ResponseEntity.notFound().build();
    }

}
