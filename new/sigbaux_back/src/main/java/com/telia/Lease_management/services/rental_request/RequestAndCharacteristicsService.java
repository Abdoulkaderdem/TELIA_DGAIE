package com.telia.Lease_management.services.rental_request;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.telia.Lease_management.dto.requests.RequestAndCharacteristicsDto;
import com.telia.Lease_management.entity.rental_request.RequestAndCharacteristics;
import com.telia.Lease_management.repository.rental_offer.RentalCharacteristicsRepository;
import com.telia.Lease_management.repository.rental_request.RequestAndCharacteristicsRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class RequestAndCharacteristicsService {

    private RentalCharacteristicsRepository rentalCharacteristicsRepository;
    private RequestAndCharacteristicsRepository rAndCharacteristicsRepo;


    public List<RequestAndCharacteristicsDto> findAll() {
        return rAndCharacteristicsRepo.findAll().stream()
                .map(RequestAndCharacteristicsDto::fromEntity)
                .collect(Collectors.toList());
    }


    public Optional<RequestAndCharacteristicsDto> findById(Long id) {
        return rAndCharacteristicsRepo.findById(id).map(RequestAndCharacteristicsDto::fromEntity);
    }

    public RequestAndCharacteristicsDto save(RequestAndCharacteristicsDto requestAndCharacteristicsDto) {
        RequestAndCharacteristics requestAndCharacteristics = RequestAndCharacteristicsDto.toEntity(requestAndCharacteristicsDto);
        return RequestAndCharacteristicsDto.fromEntity(rAndCharacteristicsRepo.save(requestAndCharacteristics));
    }

    public void deleteById(Long id) {
        rAndCharacteristicsRepo.deleteById(id);
    }
    
}
