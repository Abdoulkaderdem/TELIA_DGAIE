package com.telia.Lease_management.dto.requests;

import com.telia.Lease_management.entity.Role;
import com.telia.Lease_management.entity.Users;
import com.telia.Lease_management.entity.common.RoleType;
import com.telia.Lease_management.entity.common.TypeUser;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String matricule;
    private TypeUser typeUser; 
    private boolean canReceiveNewProjectEmail;
    private RoleType role;
    private Long idStructure;

    public static UserDto fromEntityUsers (Users users){
        if (users == null) {
            return null;
        }

        return UserDto.builder()
                .id(users.getId())
                .firstName(users.getFirstName())
                .lastName(users.getLastName())
                .email(users.getEmail())
                .password(users.getPassword())
                .matricule(users.getMatricule())
                .typeUser(users.getTypeUser())
                .role(users.getRole())
                .canReceiveNewProjectEmail(users.isCanReceiveNewProjectEmail())
                .build();
    }

    public static Users toEntityUsers (UserDto dto){
        if (dto == null) {
            return null;
        }

        Users users= new Users();
        users.setId(dto.id);
        users.setFirstName(dto.getFirstName());
        users.setLastName(dto.getLastName());
        users.setEmail(dto.getEmail());
        users.setMatricule(dto.getMatricule());
        users.setTypeUser(dto.getTypeUser());
        users.setPassword(dto.getPassword());
        users.setRole(dto.getRole());
        users.setCanReceiveNewProjectEmail(dto.isCanReceiveNewProjectEmail());

        return users;
    }
    
}
