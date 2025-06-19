package com.telia.Lease_management.controllers.implementApi;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.telia.Lease_management.controllers.interfaceApi.DashboardApi;
import com.telia.Lease_management.dto.responses.DashboardCountsResponse;
import com.telia.Lease_management.services.CNOI.RentPriceService;
import com.telia.Lease_management.services.contract.ContractService;
import com.telia.Lease_management.services.rental_offer.BuildingService;
import com.telia.Lease_management.services.rental_offer.RentalOfferService;
import com.telia.Lease_management.services.rental_request.RentalRequestService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
public class DashboardController implements DashboardApi{
    
    private BuildingService buildingService;
    private RentalRequestService rentalRequestService;
    private RentalOfferService rentalOfferService;
    private ContractService contractService;

    @Override
    public ResponseEntity<DashboardCountsResponse> getDashboardCounts() {
        DashboardCountsResponse dashboardCounts = new DashboardCountsResponse();

        dashboardCounts.setBuildingCount(buildingService.countAllBuildings());
        dashboardCounts.setRentalRequestCount(rentalRequestService.countAllRentalRequests());
        dashboardCounts.setRequestOfferCount(rentalOfferService.countAllRentalOffer());
        // dashboardCounts.setContractCount(contractService.countContracts());
        dashboardCounts.setContractCount(3L);
        dashboardCounts.setInvoiceCount(13L);
        
        return ResponseEntity.ok(dashboardCounts);
    }

    
    
}
