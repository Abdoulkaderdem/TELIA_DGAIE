package com.telia.Lease_management.entity.contract;

import java.time.Instant;

import com.telia.Lease_management.entity.common.AbstractEntity;
import com.telia.Lease_management.entity.common.RentalStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Invoice extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    @Column(nullable = true)
    private int invoicesOrder;
    
    private String quaterPayment;
    private Double amount;
    private Instant dueDate;
    private String description;
    private String invoiceReference;
    private String startInterval;
    private String endInterval;
    private String filePath; // Le chemin du fichier associé à la facture

    @Column(name = "rental_status", columnDefinition = "smallint")
    @Enumerated(EnumType.ORDINAL)
    private RentalStatus status = RentalStatus.DISABLED;

}