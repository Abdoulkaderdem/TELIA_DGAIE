package com.telia.Lease_management.dto.requests;

import java.time.Instant;

import com.telia.Lease_management.entity.common.RentalStatus;
import com.telia.Lease_management.entity.contract.Contract;
import com.telia.Lease_management.entity.rental_offer.Building;
import com.telia.Lease_management.entity.rental_offer.LandLord;
import com.telia.Lease_management.entity.rental_request.RentalRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class ContractDto {
    private Long id;
    private Long buildingId;
    private Long rentAmount;
    private Instant startDate;
    private Instant endDate;
    private String terms;
    private int contractPeriodicity;
    private String bankAccountNumber;
    private String president;
    private String reporter;
    private String contractingAuthority ;
    // private RentalStatus status;
    private boolean isTerminated;


    public static ContractDto fromEntity(Contract contract) {
        if (contract == null) {
            return null;
        }

        return ContractDto.builder()
                .id(contract.getId())
                .buildingId(contract.getBuilding().getId())
                .rentAmount(contract.getRentAmount())
                .startDate(contract.getStartDate())
                .endDate(contract.getEndDate())
                .terms(contract.getTerms())
                .contractPeriodicity(contract.getContractPeriodicity())
                .bankAccountNumber(contract.getBankAccountNumber())
                .president(contract.getPresident())
                .reporter(contract.getReporter())
                .contractingAuthority(contract.getContractingAuthority())
                // .status(contract.getStatus())
                .isTerminated(contract.isTerminated())
                .build();
    }


    public static Contract toEntity(ContractDto dto) {
        if (dto == null) {
            return null;
        }

        Contract contract = new Contract();
        contract.setId(dto.getId());
        // contract.setBuilding(BuildingDto.toEntityBuilding(dto.getBuildingDto()));
        contract.setRentAmount(dto.getRentAmount());
        contract.setStartDate(dto.getStartDate());
        // contract.setEndDate(dto.getEndDate());
        contract.setTerms(dto.getTerms());
        contract.setBankAccountNumber(dto.getBankAccountNumber());
        // contract.setStatus(dto.getStatus());
        contract.setTerminated(dto.isTerminated);

        return contract;
    }
}
