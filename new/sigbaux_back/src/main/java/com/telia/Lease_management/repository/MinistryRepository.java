package com.telia.Lease_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.telia.Lease_management.entity.Ministry;
import java.util.List;
import java.util.Optional;


public interface MinistryRepository extends JpaRepository<Ministry, Long>{
    
   Optional<Ministry> findByName(String name);

   List<Ministry> findByActivateTrue();
   Optional<Ministry> findByIdAndActivateTrue(Long id);
}
