package com.telia.Lease_management.services.CNOI;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.telia.Lease_management.dto.requests.CommitteeMemberDto;
import com.telia.Lease_management.entity.CNOI.CommitteeMember;
import com.telia.Lease_management.repository.CNOI.CommitteeMemberRepository;
import com.telia.Lease_management.utils.ValidateForms;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class CommitteeMemberService {
    
    private CommitteeMemberRepository committeeMemberRepository;

    public CommitteeMemberDto saveCommitteeMember(CommitteeMemberDto committeeMemberDto) throws Exception{
        List<String> errors= ValidateForms.validateCommitteeMember(committeeMemberDto);
        if (!errors.isEmpty()){
            log.error("Liste des erreurs {}: ", errors);
            throw new Exception("Committee member is not valid !");
        }

        CommitteeMember committeeMemberSaved= committeeMemberRepository.save(CommitteeMemberDto.toEntity(committeeMemberDto));

        return CommitteeMemberDto.fromEntity(committeeMemberSaved);
    }

    public List<CommitteeMember> getAllCommitteeMembers() {
        return committeeMemberRepository.findAll();
    }

    public Optional<CommitteeMember> getCommitteeMemberById(Long id) {
        return committeeMemberRepository.findById(id);
    }

    public CommitteeMember updateCommitteeMember(Long id, CommitteeMember updatedCommitteeMember) {
        return committeeMemberRepository.findById(id)
                .map(committeeMember -> {
                    committeeMember.setFirstName(updatedCommitteeMember.getFirstName());
                    committeeMember.setLastName(updatedCommitteeMember.getLastName());
                    // committeeMember.setMatricule(updatedCommitteeMember.getMatricule());
                    committeeMember.setPhoneNumber(updatedCommitteeMember.getPhoneNumber());
                    committeeMember.setEmail(updatedCommitteeMember.getEmail());
                    committeeMember.setFunction(updatedCommitteeMember.getFunction());
                    // committeeMember.setCommittee(updatedCommitteeMember.getCommittee());
                    return committeeMemberRepository.save(committeeMember);
                })
                .orElseGet(() -> {
                    updatedCommitteeMember.setId(id);
                    return committeeMemberRepository.save(updatedCommitteeMember);
                });
    }

    public void deleteCommitteeMember(Long id) {
        committeeMemberRepository.deleteById(id);
    }
}

