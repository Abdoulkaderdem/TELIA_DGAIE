package com.telia.Lease_management.dto.sinafoloDto;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceNafoloDto {
    private String referenceFacture;
    private Date dateFacture;
    private double montantFacture;
    private String referenceContrat;
    private String codeTiers;
}