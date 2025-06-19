package com.telia.Lease_management.repository.rental_offer;

import org.springframework.data.jpa.repository.JpaRepository;

import com.telia.Lease_management.entity.rental_offer.LandLord;
import java.util.List;
import java.util.Optional;


public interface LandLordRepository extends JpaRepository<LandLord, Long> {

    Optional<LandLord> findByIfu(String ifu);

    Optional<LandLord> findByEmailAdress(String emailAdress);
}
