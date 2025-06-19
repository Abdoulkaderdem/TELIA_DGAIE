package com.telia.Lease_management.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Slf4j
public class DashboardCountsResponse {
    private Long buildingCount;
    private Long rentalRequestCount;
    private Long requestOfferCount;
    private Long contractCount;   
    private Long invoiceCount;
}
