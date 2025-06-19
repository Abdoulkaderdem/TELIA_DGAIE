package com.telia.Lease_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.telia.Lease_management.entity.Users;
import com.telia.Lease_management.entity.common.RoleType;
import com.telia.Lease_management.entity.common.TypeUser;


public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);
    
    List<Users> findByTypeUser(TypeUser typeUser);

    List<Users> findByRoleAndCanReceiveNewProjectEmail(RoleType role, boolean canReceiveNewProjectEmail);

    
}


