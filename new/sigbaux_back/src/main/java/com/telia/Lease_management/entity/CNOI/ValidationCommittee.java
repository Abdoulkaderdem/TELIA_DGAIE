package com.telia.Lease_management.entity.CNOI;

import java.util.List;

import com.telia.Lease_management.entity.common.AbstractEntity;
import com.telia.Lease_management.entity.rental_request.RentalRequest;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ValidationCommittee extends AbstractEntity{
   
    private String name;

    @OneToMany(mappedBy = "committee",  fetch = FetchType.LAZY)
    private List<CommitteeMember> members;

    @OneToOne
    @JoinColumn(name = "responsible_id")
    private CommitteeMember responsible;
    
    @OneToOne
    @JoinColumn(name = "reporter_id")
    private CommitteeMember reporter;

    @OneToMany(mappedBy = "validationCommittee", fetch = FetchType.LAZY)
    private List<RentalRequest> rentalRequests;

}
