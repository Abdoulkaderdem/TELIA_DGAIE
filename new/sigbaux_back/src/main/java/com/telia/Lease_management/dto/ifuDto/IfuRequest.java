package com.telia.Lease_management.dto.ifuDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IfuRequest {
    private String NUMIFU;
    private String TOKEN;
}
