package com.telia.Lease_management.services.CNOI;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.telia.Lease_management.dto.requests.BuildingDto;
import com.telia.Lease_management.dto.requests.CommitteeMemberDto;
import com.telia.Lease_management.dto.requests.RentalRequestDto;
import com.telia.Lease_management.dto.requests.ValidationCommitteeDto;
import com.telia.Lease_management.entity.CNOI.CommitteeMember;
import com.telia.Lease_management.entity.CNOI.ValidationCommittee;
import com.telia.Lease_management.entity.rental_offer.Building;
import com.telia.Lease_management.entity.rental_offer.LandLord;
import com.telia.Lease_management.entity.rental_offer.RentalOffer;
import com.telia.Lease_management.entity.rental_request.RentalRequest;
import com.telia.Lease_management.repository.CNOI.CommitteeMemberRepository;
import com.telia.Lease_management.repository.CNOI.ValidationCommitteeRepository;
import com.telia.Lease_management.repository.rental_request.RentalRequestRepository;
import com.telia.Lease_management.utils.ValidateForms;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class ValidationCommitteeService {
    
    private ValidationCommitteeRepository validationCommitteeRepo;
    private RentalRequestRepository rentalRequestRepo;
    private CommitteeMemberRepository memberRepo;

     public List<ValidationCommitteeDto> getAllCommittees() {
        return validationCommitteeRepo.findAll().stream()
                .map(ValidationCommitteeDto::fromEntity)
                .collect(Collectors.toList());
    }


    public ValidationCommitteeDto getCommitteeById(Long id) {
        return validationCommitteeRepo.findById(id)
                .map(ValidationCommitteeDto::fromEntity)
                .orElse(null);
    }


    public ValidationCommitteeDto createCommittee(ValidationCommitteeDto dto) throws Exception {
        //Conversion
        ValidationCommittee committee = ValidationCommitteeDto.toEntity(dto);

        //******save first all Committee memebers an responsible
            //Check first the list of committee member
        for(CommitteeMemberDto member: dto.getMembers()){           
            List<String> errors= ValidateForms.validateCommitteeMember(member);
            if (!errors.isEmpty()){
                log.error("Error occur on committee member List. It's not valid {}:", dto.getMembers());
                log.error("Liste des erreurs {}: ", errors);
                throw new Exception("Committee member List is not valid !", (Throwable) errors);
            }

        }

            //Then Check responsible committee member
        List<String> errors= ValidateForms.validateCommitteeMember(dto.getResponsible());
        if (!errors.isEmpty()){
            log.error("Committee responsible's not valid {}:", dto.getMembers());
            log.error("Liste des erreurs {}: ", errors);
            throw new Exception("Committee responsible is not valid !", (Throwable) errors);
        }

        //Now save all Committee members
        List<CommitteeMember> listMemeberCommitteeSaved = new ArrayList<>(); 
        List<CommitteeMember> listMemeberCommittee = CommitteeMemberDto.toEntities(dto.getMembers());
        for(CommitteeMember member: listMemeberCommittee){      
            if (member.getId() == null) {
                // If the member's ID is null, it is a new member To Save.
                member = memberRepo.save(member);
            } else {
                // If not, look for the existing member
                Optional<CommitteeMember> existingMemberOptional = memberRepo.findById(member.getId());
                if (existingMemberOptional.isPresent()) {
                    member = existingMemberOptional.get();
                } else {
                    throw new IllegalArgumentException("LandLord with id " + member.getId() + " not found");
                }
            }     
            //member.setCommittee(committee);
            member= memberRepo.save(member);
            listMemeberCommitteeSaved.add(member);
        }

        //Set committee members
        CommitteeMember committeeResponsible= CommitteeMemberDto.toEntity(dto.getResponsible());
        if (committeeResponsible.getId() == null) {
            // If the LandLord's ID is null, it is a new landLord To Save.
            committeeResponsible = memberRepo.save(committeeResponsible);
        } else {
            // If not, look for the existing LandLord
            Optional<CommitteeMember> existingResponsiveOptional = memberRepo.findById(committeeResponsible.getId());
            if (existingResponsiveOptional.isPresent()) {
                committeeResponsible = existingResponsiveOptional.get();
            } else {
                throw new IllegalArgumentException("LandLord with id " + committeeResponsible.getId() + " not found");
            }
        }

        //set Committee
        committee.setMembers(listMemeberCommitteeSaved);
        committee.setResponsible(committeeResponsible);
                
        //Save the complete validatetion Committter
        ValidationCommittee committeeSaved = validationCommitteeRepo.save(committee); 

        // Check if the rentalRequest exist in the database or not
        List<RentalRequest> requestsToAdd = new ArrayList<>();

        for (RentalRequestDto requestDto : dto.getRentalRequests()) {
            RentalRequest requestToCheck= RentalRequestDto.toEntity(requestDto);
            if (requestToCheck != null) {
                // If request exist, Insert the new committee
                requestToCheck.setValidationCommittee(committeeSaved);
                requestToCheck.setValidate(true);
                rentalRequestRepo.save(requestToCheck);
                requestsToAdd.add(requestToCheck);
                log.info("******************Demande de validation sauvegardé!");
            } else {
                // If Validation committee doen't have rental request
                throw new IllegalArgumentException("Impossible to create a committee without rental request  " + dto);
            }
        }

        //Set all rental requests
        committeeSaved.setRentalRequests(requestsToAdd);

        return ValidationCommitteeDto.fromEntity(committeeSaved);
    }


    @Transactional
    public ValidationCommitteeDto updateCommittee(ValidationCommitteeDto dto) {

        //verify if committee exist in the DB
        ValidationCommittee existingCommittee = validationCommitteeRepo.findById(dto.getId())
            .orElseThrow(() -> new IllegalArgumentException("Validation Committee with id " + dto.getId() + " not found"));
        
        // Update fields
        existingCommittee.setName(dto.getName());

        //Check Responsible Committee if necessary
        if (dto.getResponsible() != null) {
            CommitteeMember reponsibleMember = memberRepo.findById(dto.getResponsible().getId())
                    .orElseThrow(() -> new IllegalArgumentException("LandLord with id " + dto.getResponsible().getId() + " not found"));
            existingCommittee.setResponsible(reponsibleMember);
        }else {
            throw new IllegalArgumentException("This Validation committee must Have Responsible:  " + dto.getResponsible());
        }

        //Check Committee member if necessary
        List<CommitteeMember> listMembers= new ArrayList<>();
        if (!dto.getMembers().isEmpty()) {
            for(CommitteeMemberDto memberDto: dto.getMembers()){         
                CommitteeMember member = memberRepo.findById(memberDto.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Member with id " + dto.getResponsible().getId() + " not found"));
                
                listMembers.add(member);
            }
        }else {
            throw new IllegalArgumentException("This Validation committee must Have members:  " + dto.getMembers());
        }

        existingCommittee.setMembers(listMembers);

        //Check Committee member if necessary
        List<RentalRequest> requestsToAdd = new ArrayList<>();

        for (RentalRequestDto requestDto : dto.getRentalRequests()) {
            RentalRequest requestToCheck= RentalRequestDto.toEntity(requestDto);

            if (requestToCheck != null) {
                requestsToAdd.add(requestToCheck);
            } else {
                throw new IllegalArgumentException("Impossible to create a committee without rental request  " + dto);
            }
        }
        
        //Clear all previous rental requests and make updating
        existingCommittee.setRentalRequests(requestsToAdd);

        //Save the committee
        existingCommittee = validationCommitteeRepo.save(existingCommittee);

        return ValidationCommitteeDto.fromEntity(existingCommittee);

    }



}
