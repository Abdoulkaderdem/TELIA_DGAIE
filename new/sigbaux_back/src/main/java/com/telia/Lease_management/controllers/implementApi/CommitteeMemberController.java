package com.telia.Lease_management.controllers.implementApi;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.telia.Lease_management.controllers.interfaceApi.CommitteeMemberApi;
import com.telia.Lease_management.dto.requests.CommitteeMemberDto;
import com.telia.Lease_management.entity.CNOI.CommitteeMember;
import com.telia.Lease_management.services.CNOI.CommitteeMemberService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
public class CommitteeMemberController implements CommitteeMemberApi{

     private CommitteeMemberService committeeMemberService;

    @Override
    public ResponseEntity<CommitteeMemberDto> createCommitteeMember(CommitteeMemberDto committeeMemberDto) throws Exception {
        CommitteeMemberDto savedCommitteeMember = committeeMemberService.saveCommitteeMember(committeeMemberDto);
        return ResponseEntity.ok(savedCommitteeMember);
    }

    @Override
    public ResponseEntity<List<CommitteeMember>> getAllCommitteeMembers() {
        List<CommitteeMember> committeeMembers = committeeMemberService.getAllCommitteeMembers();
        return ResponseEntity.ok(committeeMembers);
    }

    @Override
    public ResponseEntity<CommitteeMember> getCommitteeMemberById(Long id) {
        return committeeMemberService.getCommitteeMemberById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<CommitteeMember> updateCommitteeMember(Long id, CommitteeMember updatedCommitteeMember) {
        CommitteeMember savedCommitteeMember = committeeMemberService.updateCommitteeMember(id, updatedCommitteeMember);
        return ResponseEntity.ok(savedCommitteeMember);
    }
    
}
