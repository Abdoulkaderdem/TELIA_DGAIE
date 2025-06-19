package com.telia.Lease_management.dto.responses;

import com.telia.Lease_management.dto.requests.RentalCharacteristicsDto;
import com.telia.Lease_management.dto.requests.RentalRequestDto;
import com.telia.Lease_management.entity.rental_offer.OfferAndCharacteristics;
import com.telia.Lease_management.entity.rental_request.RequestAndCharacteristics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class RequestAndCharacteristicsResponse {
       
    private Long id;
    private Long values; 
    private RentalCharacteristicsDto characteristicsDto;
    private Long idRentalRequestDto;

    // Static method to convert OfferAndCharacteristics to OfferAndCharacteristicsResponse
      public static RequestAndCharacteristicsResponse fromEntity(RequestAndCharacteristics requestAndCharacteristics) {
        return new RequestAndCharacteristicsResponse(
            requestAndCharacteristics.getId(),
            requestAndCharacteristics.getValues(),
            RentalCharacteristicsDto.fromEntityRentalCharacteristics(requestAndCharacteristics.getCharacteristics()),
            requestAndCharacteristics.getRentalRequest() != null ? requestAndCharacteristics.getRentalRequest().getId() : null
        );
    }
}
