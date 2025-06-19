package com.telia.Lease_management.services.contract;

import java.time.LocalDateTime;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.telia.Lease_management.dto.responses.ContractResponse;
import com.telia.Lease_management.dto.responses.ContractRevisionResponse;
import com.telia.Lease_management.entity.common.RentalStatus;
import com.telia.Lease_management.entity.contract.Contract;
import com.telia.Lease_management.entity.contract.ContractRevision;
import com.telia.Lease_management.entity.rental_offer.Building;
import com.telia.Lease_management.entity.rental_request.RentalRequest;
import com.telia.Lease_management.repository.contract.ContractRepository;
import com.telia.Lease_management.repository.contract.ContractRevisionRepository;
import com.telia.Lease_management.repository.rental_offer.BuildingRepository;
import com.telia.Lease_management.repository.rental_request.RentalRequestRepository;
import com.telia.Lease_management.utils.DateUtils;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class ContractRevisionService {

    private ContractRevisionRepository contractRevisionRepository;
    private ContractRepository contractRepository;
    private BuildingRepository buildingRepository;
    private RentalRequestRepository rentalRequestRepo;


    public ContractRevisionResponse addContractRevision(Long contractId, String revisionDetails) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new EntityNotFoundException("Contract not found!"));

        if (revisionDetails==null || revisionDetails==""){
             throw new IllegalArgumentException("revisionDetails is not valid !");
        }

        //Checking status
        if (contract.getStatus() == RentalStatus.DISABLED || contract.isTerminated()){
            throw new IllegalArgumentException("Cannot revise a terminated or disabled contract !");
        }
        //Update Contract
        contract.setRevised(true);
        contract.setNberRevision(contract.getNberRevision()+1);
        contractRepository.save(contract);

        //Set Contract Revision
        ContractRevision contractRevisionToAdd = new ContractRevision();
        contractRevisionToAdd.setContract(contract);
        contractRevisionToAdd.setRevisionDetails(revisionDetails);
        contractRevisionToAdd.setRevisionDate(LocalDateTime.now());
        contractRevisionToAdd.setStatus(RentalStatus.ENABLE);

        contractRevisionRepository.save(contractRevisionToAdd);

         return ContractRevisionResponse.fromEntity(contractRevisionToAdd);
    }


    public List<ContractRevisionResponse> getContractRevisions(Long contractId) {
         List<ContractRevision> listContractRevisions= contractRevisionRepository.findByContractId(contractId);

         return listContractRevisions.stream()
                    .map(ContractRevisionResponse:: fromEntity)
                    .collect(Collectors.toList());
    }

    public ContractRevisionResponse deactivateContractRevision(Long revisionId) {
        ContractRevision contractRevision = contractRevisionRepository.findById(revisionId)
                .orElseThrow(() -> new EntityNotFoundException("Contract revision not found"));

        contractRevision.setStatus(RentalStatus.DISABLED);
        ContractRevision savedRevision = contractRevisionRepository.save(contractRevision);

        return ContractRevisionResponse.fromEntity(savedRevision);
    }

        
    public List<ContractRevisionResponse> getEnabledAmendmentsByContractId(Long contractId) {
        List<ContractRevision> listContractRevisions= contractRevisionRepository.findByContractIdAndStatus(contractId, RentalStatus.ENABLE);

        return listContractRevisions.stream()
                    .map(ContractRevisionResponse:: fromEntity)
                    .collect(Collectors.toList());
    }


    public ContractResponse revisonContractPeriodicity(Long idContract, int periodicityInMonths,
            String revisionDetails) {

        Contract existingContract = contractRepository.findById(idContract)
                .orElseThrow(() -> new RuntimeException("Contract not found"));

        if (periodicityInMonths < 1 ){
            new RuntimeException("The periodicity value is not correct! ");
        }
        
        if (revisionDetails==null || revisionDetails==""){
            throw new IllegalArgumentException("revisionDetails is not valid !");
       }

        //Retreive the  building
        Building existingBuilding = buildingRepository.findById(existingContract.getBuilding().getId())
                .orElseThrow(() -> new RuntimeException("Building not found"));

        //Retreive the rental request for building
        RentalRequest rentalRequest = existingBuilding.getRentalRequest();

         //Update Rental Request Status and save it
         rentalRequest.setStatus(RentalStatus.FINALIZED);
         rentalRequestRepo.save(rentalRequest);
 
         //Update Building Status and save it 
         existingBuilding.setStatus(RentalStatus.RENT);
         buildingRepository.save(existingBuilding);

        int previewPeriodicityInMonths = existingContract.getContractPeriodicity();   //get preview periodicity to updating

         // Calculate the new 'end date' from the preview 'endDate' and Update it
        Instant startDate = existingContract.getEndDate();
        Instant endDate = DateUtils.calculateEndDate(startDate, periodicityInMonths);
        int newPeriodicityInMonths = previewPeriodicityInMonths + periodicityInMonths;

        //Updating the existing contract
        existingContract.setContractPeriodicity(newPeriodicityInMonths);
        existingContract.setStatus(RentalStatus.DISABLED);  
        existingContract.setRevised(true);
        existingContract.setNberRevision(existingContract.getNberRevision()+1);
        existingContract.setEndDate(endDate);
        existingContract.setTerminated(false);
        contractRepository.save(existingContract); //Save the updating of contract

        //Create the amandement or revision of this contract
        
        //Set Contract Revision
        ContractRevision contractRevisionToAdd = new ContractRevision();
        contractRevisionToAdd.setContract(existingContract);
        contractRevisionToAdd.setRevisionDetails(revisionDetails);
        contractRevisionToAdd.setRevisionDate(LocalDateTime.now());
        contractRevisionToAdd.setStatus(RentalStatus.ENABLE);

        contractRevisionRepository.save(contractRevisionToAdd);

        return ContractResponse.fromEntity(existingContract);

    }


    public Boolean renewContractRentalAmount(Long idContract, String revisionDetails) {
       
        Contract existingContract = contractRepository.findById(idContract)
                .orElseThrow(() -> new RuntimeException("Contract not found"));
            
        if (revisionDetails==null || StringUtils.isEmpty(revisionDetails)){
            throw new IllegalArgumentException("revisionDetails is not valid !");
        }

        //Retreive the  building
         Building existingBuilding = buildingRepository.findById(existingContract.getBuilding().getId())
            .orElseThrow(() -> new RuntimeException("Building not found"));

        //Retreive the rental request for building
        RentalRequest rentalRequest = existingBuilding.getRentalRequest();

        //Set RentalRequest
        rentalRequest.setStatus(RentalStatus.BATIMENT_FOUND);
        rentalRequest.setExaminatedCouncilMinister(false);
        rentalRequest.setValidatedCouncilMinister(false);
        rentalRequest.setValidate(true);
        rentalRequestRepo.save(rentalRequest);
        //Set Buulding
        existingBuilding.setStatus(RentalStatus.RETAINED);
        existingBuilding.setValidatedCouncilMinister(false);
        buildingRepository.save(existingBuilding);
        //Set contract
        existingContract.setStatus(RentalStatus.DISABLED);  
        existingContract.setRevised(true);
        existingContract.setNberRevision(existingContract.getNberRevision()+1);
        existingContract.setTerminated(false);
        contractRepository.save(existingContract); //Save the updating of contract
        
        //Set Contract Revision
        ContractRevision contractRevisionToAdd = new ContractRevision();
        contractRevisionToAdd.setContract(existingContract);
        contractRevisionToAdd.setRevisionDetails(revisionDetails);
        contractRevisionToAdd.setRevisionDate(LocalDateTime.now());
        contractRevisionToAdd.setStatus(RentalStatus.ENABLE);
        contractRevisionRepository.save(contractRevisionToAdd); //Save

        return true;

    }

    
}
