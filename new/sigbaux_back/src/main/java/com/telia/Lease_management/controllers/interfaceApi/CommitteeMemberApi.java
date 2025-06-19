package com.telia.Lease_management.controllers.interfaceApi;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.telia.Lease_management.dto.requests.CommitteeMemberDto;
import com.telia.Lease_management.entity.CNOI.CommitteeMember;

@RequestMapping("/committee-member")
@CrossOrigin
public interface CommitteeMemberApi {

    @PreAuthorize("hasAnyAuthority('ROLE_DSI', 'ROLE_ADMIN', 'ROLE_SUPER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommitteeMemberDto> createCommitteeMember(@RequestBody CommitteeMemberDto committeeMemberDto) throws Exception ;
       
    @PreAuthorize("hasAnyAuthority('ROLE_DSI', 'ROLE_ADMIN', 'ROLE_SUPER')")
    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommitteeMember>> getAllCommitteeMembers();

    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommitteeMember> getCommitteeMemberById(@PathVariable Long id);

    @PreAuthorize("hasAnyAuthority('ROLE_DSI', 'ROLE_ADMIN', 'ROLE_SUPER')")
    @PutMapping(value="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommitteeMember> updateCommitteeMember(@PathVariable Long id, @RequestBody CommitteeMember updatedCommitteeMember);

    
}
