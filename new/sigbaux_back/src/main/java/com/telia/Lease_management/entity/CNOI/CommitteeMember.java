package com.telia.Lease_management.entity.CNOI;

import java.util.List;

import com.telia.Lease_management.entity.common.AbstractEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CommitteeMember  extends AbstractEntity{
     
    private String firstName;
    private String lastName;
    private String matricule;
    private String phoneNumber;
    private String email;
    private String function;

    @ManyToOne
    @JoinColumn(name = "committee_id")
    private ValidationCommittee committee;

}
