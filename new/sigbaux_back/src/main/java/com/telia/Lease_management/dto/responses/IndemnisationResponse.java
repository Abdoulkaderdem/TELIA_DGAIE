package com.telia.Lease_management.dto.responses;

import java.time.Instant;
import java.time.LocalDate;

import lombok.Data;

@Data
public class IndemnisationResponse {
    private Long id;
    private Instant date;
    private String justificationFilePath;
    private String comment;
    private boolean isJustificationUploaded;
}
