package com.telia.Lease_management.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BuildingWithProvisionalRentAmount {
    private Long idBuilding;
    private Long provisionalRentAmount;
}
