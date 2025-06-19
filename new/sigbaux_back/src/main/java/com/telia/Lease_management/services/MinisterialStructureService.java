package com.telia.Lease_management.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.telia.Lease_management.dto.requests.MinisterialStructureDto;
import com.telia.Lease_management.dto.requests.MinistryDto;
import com.telia.Lease_management.dto.requests.RentalRequestDto;
import com.telia.Lease_management.dto.responses.StructureWithRequestRentalResponse;
import com.telia.Lease_management.entity.MinisterialStructure;
import com.telia.Lease_management.entity.Ministry;
import com.telia.Lease_management.entity.common.RentalStatus;
import com.telia.Lease_management.entity.rental_request.RentalRequest;
import com.telia.Lease_management.repository.MinisterialStrutureRepository;
import com.telia.Lease_management.repository.MinistryRepository;
import com.telia.Lease_management.repository.rental_request.RentalRequestRepository;
import com.telia.Lease_management.utils.ValidateForms;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Service
@Slf4j
public class MinisterialStructureService {

    private MinisterialStrutureRepository strutureRepo;
    private MinistryRepository ministryRepository;
    private RentalRequestRepository rentalRequestRepository;


    public Boolean  createStructure(MinisterialStructureDto structureDto) throws Exception{

        //Check the forms
        List<String> errors= ValidateForms.validateMinisterialStructure(structureDto);
        if (!errors.isEmpty()){
            log.error("Offer is not valid {}:", structureDto);
            log.error("Liste des erreurs {}: ", errors);
            throw new Exception("Ministerial Structure is not valid !");
        }

        //Check if a struture with same name already exit
        Optional<MinisterialStructure> existingStructure = strutureRepo.findByName(structureDto.getName());
        if (existingStructure.isPresent()) {
            throw new IllegalArgumentException("A structure with the same name already exists!");
        }

        MinisterialStructure structureToAdd = MinisterialStructureDto.toEntityMinistryStrucure(structureDto);

        Optional<Ministry> ministry = ministryRepository.findById(structureDto.getIdMinistry());
        if (!ministry.isPresent()) {
            throw new IllegalArgumentException("Ministry not found!");
        }
        structureToAdd.setMinistry(ministry.get());
        
        structureToAdd = strutureRepo.save(structureToAdd);

        //return MinisterialStructureDto.fromEntityMinistryStrucure(structureToAdd);

        return true;


    }


    public List<MinisterialStructureDto> findAll() {
        
        // Retrieving the list of all activated Ministry from BDD
        //List<MinisterialStructure> structureList = strutureRepo.findAll();
        List<MinisterialStructure> structureList = strutureRepo.findByActivateTrue();

        return structureList.stream()
                .map(MinisterialStructureDto::fromEntityMinistryStrucure)
                .collect(Collectors.toList());
    }


    public MinisterialStructureDto findById(Long id) {
         if (id == null) {
            log.error("User ID is null");
            return null;
        }
 
        //Optional<MinisterialStructure> optionalStructure= strutureRepo.findById(id);
        Optional<MinisterialStructure> optionalStructure = strutureRepo.findByIdAndActivateTrue(id);

        if (optionalStructure.isPresent()) {
            return MinisterialStructureDto.fromEntityMinistryStrucure(optionalStructure.get());
        } else {
            throw new EntityNotFoundException(
                "No Struture with the ID = " + id + " is in the DB"
            );
        }
    }

    public MinisterialStructureDto updateMinisterialStructure(Long id, MinisterialStructureDto structureDto) {

        Optional<MinisterialStructure> existingStructure = strutureRepo.findById(id);
        if (!existingStructure.isPresent()) {
            throw new IllegalArgumentException("Structure not found!");
        }

        MinisterialStructure structure = existingStructure.get();
        if (!structure.getName().equals(structureDto.getName())) {
            Optional<MinisterialStructure> structureWithName = strutureRepo.findByName(structureDto.getName());
            if (structureWithName.isPresent()) {
                throw new IllegalArgumentException("A structure with the same name already exists!");
            }
        }

        structure.setName(structureDto.getName());
        structure.setDomain(structureDto.getDomain());
        structure.setPhone(structureDto.getPhone());
        structure.setEmail(structureDto.getEmail());
        structure.setManager(structureDto.getManager());
        structure.setCode(structureDto.getCode());
        structure.setLegalStatus(structureDto.getLegalStatus());

        Optional<Ministry> ministry = ministryRepository.findById(structureDto.getIdMinistry());
        if (!ministry.isPresent()) {
            throw new IllegalArgumentException("Ministry not found!");
        }
        structure.setMinistry(ministry.get());

        structure = strutureRepo.save(structure);
        return MinisterialStructureDto.fromEntityMinistryStrucure(structure);


    }


    public MinisterialStructureDto setActivationStatus(Long id, boolean b) {
        MinisterialStructure ministerialStructure = strutureRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ministerial Structure is found with id " + id));

        ministerialStructure.setActivate(b);
        return MinisterialStructureDto.fromEntityMinistryStrucure(strutureRepo.save(ministerialStructure));
    }


    public List<RentalRequestDto> getValidatedRentalRequests(Long structureId) {       
        Optional<MinisterialStructure> structureOptional = strutureRepo.findById(structureId);
        if (structureOptional.isEmpty()) {
            throw new RuntimeException("Structure not found with id: " + structureId);
        }       
        MinisterialStructure structure = structureOptional.get();
        List<RentalRequest> listRentalRequests= rentalRequestRepository.findByStructureAndStatus(structure, RentalStatus.VALIDATE);

        return listRentalRequests.stream()
            .map(RentalRequestDto::fromEntity)
            .collect(Collectors.toList());
    
    }


    public List<RentalRequestDto> getNewRentalRequests(Long structureId) {       
        Optional<MinisterialStructure> structureOptional = strutureRepo.findById(structureId);
        if (structureOptional.isEmpty()) {
            throw new RuntimeException("Structure not found with id: " + structureId);
        }       
        MinisterialStructure structure = structureOptional.get();
        List<RentalRequest> listRentalRequests= rentalRequestRepository.findByStructureAndStatus(structure, RentalStatus.NEW);

        return listRentalRequests.stream()
            .map(RentalRequestDto::fromEntity)
            .collect(Collectors.toList());
    }


    public List<RentalRequestDto> getSendedRentalRequests(Long structureId) {
        Optional<MinisterialStructure> structureOptional = strutureRepo.findById(structureId);
        if (structureOptional.isEmpty()) {
            throw new RuntimeException("Structure not found with id: " + structureId);
        }       
        MinisterialStructure structure = structureOptional.get();
        List<RentalRequest> listRentalRequests= rentalRequestRepository.findByStructureAndStatus(structure, RentalStatus.SEND);

        return listRentalRequests.stream()
            .map(RentalRequestDto::fromEntity)
            .collect(Collectors.toList());
    }


    public List<RentalRequestDto> getComplementRentalRequests(Long structureId) {
        Optional<MinisterialStructure> structureOptional = strutureRepo.findById(structureId);
        if (structureOptional.isEmpty()) {
            throw new RuntimeException("Structure not found with id: " + structureId);
        }       
        MinisterialStructure structure = structureOptional.get();
        List<RentalRequest> listRentalRequests= rentalRequestRepository.findByStructureAndStatus(structure, RentalStatus.COMPLEMENT);

        return listRentalRequests.stream()
            .map(RentalRequestDto::fromEntity)
            .collect(Collectors.toList());
    }


    public List<RentalRequestDto> getApprovalRentalRequests(Long structureId) {
        Optional<MinisterialStructure> structureOptional = strutureRepo.findById(structureId);
        if (structureOptional.isEmpty()) {
            throw new RuntimeException("Structure not found with id: " + structureId);
        }       
        MinisterialStructure structure = structureOptional.get();
        List<RentalRequest> listRentalRequests= rentalRequestRepository.findByStructureAndStatus(structure, RentalStatus.APPROVAL);

        return listRentalRequests.stream()
            .map(RentalRequestDto::fromEntity)
            .collect(Collectors.toList());
    }


    public List<RentalRequestDto> getRejectedRentalRequests(Long structureId) {
        Optional<MinisterialStructure> structureOptional = strutureRepo.findById(structureId);
        if (structureOptional.isEmpty()) {
            throw new RuntimeException("Structure not found with id: " + structureId);
        }       
        MinisterialStructure structure = structureOptional.get();
        List<RentalRequest> listRentalRequests= rentalRequestRepository.findByStructureAndStatus(structure, RentalStatus.REJECTED);

        return listRentalRequests.stream()
            .map(RentalRequestDto::fromEntity)
            .collect(Collectors.toList());
    }


    public List<RentalRequestDto> getHeldRentalRequests(Long structureId) {
        Optional<MinisterialStructure> structureOptional = strutureRepo.findById(structureId);
        if (structureOptional.isEmpty()) {
            throw new RuntimeException("Structure not found with id: " + structureId);
        }       
        MinisterialStructure structure = structureOptional.get();
        List<RentalRequest> listRentalRequests= rentalRequestRepository.findByStructureAndStatus(structure, RentalStatus.HELD);

        return listRentalRequests.stream()
            .map(RentalRequestDto::fromEntity)
            .collect(Collectors.toList());
    }
    
}
