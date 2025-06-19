package com.telia.Lease_management.services.rental_request;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.telia.Lease_management.dto.requests.TypeUsageDto;
import com.telia.Lease_management.entity.rental_request.TypeUsage;
import com.telia.Lease_management.repository.rental_request.TypeUsageRepository;
import com.telia.Lease_management.utils.ValidateForms;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class TypeUsageService {
    
     private TypeUsageRepository typeUsageRepository;

      public TypeUsageDto createTypeUsage(TypeUsageDto typeUsageDto) throws Exception {
        
        //Check the forms
        List<String> errors= ValidateForms.validateTypeUsages(typeUsageDto);
        if (!errors.isEmpty()){
            log.error("Offer is not valid {}:", typeUsageDto);
            log.error("Liste des erreurs {}: ", errors);
            throw new Exception("Type Usage is not valid !");
        }
        
        TypeUsage typeUsage = TypeUsageDto.toEntity(typeUsageDto);
        typeUsage = typeUsageRepository.save(typeUsage);
        return TypeUsageDto.fromEntity(typeUsage);
    }

    public List<TypeUsageDto> getAllTypeUsages() {
        List<TypeUsage> typeUsages = typeUsageRepository.findAll();
        return typeUsages.stream()
                .map(TypeUsageDto::fromEntity)
                .collect(Collectors.toList());
    }

    public TypeUsageDto getTypeUsageById(Long id) {
        Optional<TypeUsage> typeUsage = typeUsageRepository.findById(id);
        return typeUsage.map(TypeUsageDto::fromEntity).orElse(null);
    }

    public TypeUsageDto updateTypeUsage(Long id, TypeUsageDto typeUsageDto) {
        Optional<TypeUsage> optionalTypeUsage = typeUsageRepository.findById(id);
        if (!optionalTypeUsage.isPresent()) {
            return null;
        }

        TypeUsage typeUsage = optionalTypeUsage.get();
        typeUsage.setLibCourt(typeUsageDto.getLibCourt());
        typeUsage.setLibLong(typeUsageDto.getLibLong());
        
        typeUsage = typeUsageRepository.save(typeUsage);
        return TypeUsageDto.fromEntity(typeUsage);
    }

    public void deleteTypeUsage(Long id) {
        typeUsageRepository.deleteById(id);
    }
    
}
