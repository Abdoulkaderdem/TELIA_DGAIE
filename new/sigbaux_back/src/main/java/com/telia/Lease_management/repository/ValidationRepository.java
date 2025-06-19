package com.telia.Lease_management.repository;

import org.springframework.data.repository.CrudRepository;

import com.telia.Lease_management.entity.Validation;

import java.util.Optional;

public interface ValidationRepository extends CrudRepository<Validation, Integer> {

    Optional<Validation> findByCode(String code);
}
