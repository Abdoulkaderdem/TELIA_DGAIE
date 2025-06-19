package com.telia.Lease_management.dto.requests;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttachBuildingsToRequestDto {
    private Long rentalRequestId;
    private List<Long> buildingIds;
}
