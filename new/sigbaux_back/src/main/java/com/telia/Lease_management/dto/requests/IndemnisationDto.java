package com.telia.Lease_management.dto.requests;

import java.time.Instant;
import java.time.LocalDate;

import lombok.Data;

@Data
public class IndemnisationDto {
    
    private Long contratId; 
    private Instant date;
    private String comment;
}
