package com.telia.Lease_management.entity.contract;

import java.time.Instant;

import com.telia.Lease_management.entity.common.AbstractEntity;
import com.telia.Lease_management.entity.common.RentalStatus;
import com.telia.Lease_management.entity.rental_offer.Building;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.ArrayList;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Contract extends AbstractEntity {

    @OneToOne
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    private Long rentAmount;
    @Column(nullable = true)
    private Long initialRent = 0L;
    private Instant startDate;
    private Instant endDate;

    //The signatories 
    private String president;
    private String reporter;
    private String contractingAuthority ;

    @Column(name = "terms", length = 6000) 
    private String terms;
    private int contractPeriodicity;
    private String leaseContractNumber;
    private String bankAccountNumber;
    private boolean isRevised = false;
    private boolean isTerminated = false;
    private Double  ireAmount= 0.0; 

    @Column(name = "document_url",  length = 1024)
    private String documentUrl; // Save URL contract document

    @Column(name = "notice_url",  length = 1024)
    private String noticeUrl; // Save URL contract document terminated
    
    @Column(name = "report_key_url",  length = 1024)
    private String reportKeyUrl; // Save URL of report key handover

    @Column(nullable = true)
    private boolean isKeyHandoverReport = false;

    @Column(nullable = true)
    private boolean isIndemnisation = false;

    @Column(nullable = true)
    private int nberRevision=0; //Count number of revision contrat

    @Column(nullable = true)
    private int invoicesPrinted=0;
                
    @Column(name = "rental_status", columnDefinition = "smallint")
    @Enumerated(EnumType.ORDINAL)
    private RentalStatus status = RentalStatus.DISABLED;

    @Column(name = "ampliation")
    private List<String> ampliations = new ArrayList<>();  // Save the list of ampliation structures in text format

}