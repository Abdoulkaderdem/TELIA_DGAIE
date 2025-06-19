package com.telia.Lease_management.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.telia.Lease_management.dto.requests.MinistryDto;
import com.telia.Lease_management.entity.Ministry;
import com.telia.Lease_management.repository.MinistryRepository;
import com.telia.Lease_management.utils.ValidateForms;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class MinistryService {

    private MinistryRepository ministryRepo;


    public Boolean createMinistry (MinistryDto ministryDto) throws Exception{
        
        //Check the forms
        List<String> errors= ValidateForms.validateMinistry(ministryDto);
        if (!errors.isEmpty()){
            log.error("Offer is not valid {}:", ministryDto);
            log.error("Liste des erreurs {}: ", errors);
            throw new Exception("Ministry is not valid !");
        }

        //Check if a Ministry with same name already exist
        Optional<Ministry> ministryToAdd = ministryRepo.findByName(ministryDto.getName());
        
        if (!ministryToAdd.isPresent()){
            //save the new minstry
            ministryRepo.save(MinistryDto.toEntityMinistry(ministryDto));
            return true;
        }

        return false;
    }


    public List<MinistryDto> findAll() {
        // Retrieving the list of all activated Ministry from BDD
        List<Ministry> ministryList = ministryRepo.findByActivateTrue();
        
        // Transforming the user list into a MinistryDto list
        List<MinistryDto> ministryListDtoList = ministryList.stream()
                .map(ministry -> MinistryDto.fromEntityMinistry(ministry))
                .collect(Collectors.toList());

        return ministryListDtoList;
    }


    public MinistryDto findById(Long id) {
        if (id == null) {
            log.error("User ID is null");
            return null;
        }
 
        Optional<Ministry> optionalMinistry = ministryRepo.findByIdAndActivateTrue(id);

        if (optionalMinistry.isPresent()) {
            return MinistryDto.fromEntityMinistry(optionalMinistry.get());
        } else {
            throw new EntityNotFoundException(
                "No Ministry with the ID = " + id + " is in the DB"
            );
        }
    }


    public MinistryDto updateMinistry(MinistryDto dto) {
        // Retrieve the existing ministry from the database
        Optional<Ministry> optionalMinistry = ministryRepo.findById(dto.getId());

        if (optionalMinistry.isPresent()) {
            Ministry existingMinistry = optionalMinistry.get();
            
            // Update fields based on DTO
            existingMinistry.setName(dto.getName());
            existingMinistry.setDescription(dto.getDescription());
            existingMinistry.setPhone(dto.getPhone());
            existingMinistry.setManager(dto.getManager());
            existingMinistry.setAddress(dto.getAddress());
            existingMinistry.setCode(dto.getCode());

            // Save the updated ministry
            Ministry updatedMinistry = ministryRepo.save(existingMinistry);

            // Convert and return updated DTO
            return MinistryDto.fromEntityMinistry(updatedMinistry);
        } else {
            throw new IllegalArgumentException("Ministry with id " + dto.getId() + " not found");
        }
    }


    public MinistryDto setActivationStatus(Long id, boolean b) {
        Ministry ministry = ministryRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ministry not found with id " + id));

        ministry.setActivate(b);
        return MinistryDto.fromEntityMinistry(ministryRepo.save(ministry));
    }
}
    

