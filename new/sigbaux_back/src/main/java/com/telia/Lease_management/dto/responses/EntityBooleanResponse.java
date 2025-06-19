package com.telia.Lease_management.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EntityBooleanResponse <T> {
    private boolean isConform;
    private T data;
}