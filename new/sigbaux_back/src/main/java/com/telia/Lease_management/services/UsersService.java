package com.telia.Lease_management.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.telia.Lease_management.dto.requests.MinistryDto;
import com.telia.Lease_management.dto.requests.UserDto;
import com.telia.Lease_management.entity.MinisterialStructure;
import com.telia.Lease_management.entity.Ministry;
import com.telia.Lease_management.entity.Role;
import com.telia.Lease_management.entity.common.RoleType;

import com.telia.Lease_management.entity.Users;
import com.telia.Lease_management.repository.MinisterialStrutureRepository;
import com.telia.Lease_management.repository.UsersRepository;
import com.telia.Lease_management.utils.ValidateForms;
import com.telia.Lease_management.utils.ValidationService;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class UsersService implements UserDetailsService{
    
    private UsersRepository usersRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private MinisterialStrutureRepository ministerialStrutureRepo;
    private ValidationService validationService;


    public Boolean createUser(UserDto user) throws Exception {   
        List<String> errors= ValidateForms.validateUser(user);
        if (!errors.isEmpty()){
            log.error("User is not valid {}:", user);
            log.error("Liste des erreurs {}: ", errors);
            throw new Exception("User is not valid !", (Throwable) errors);
        }

        if(!user.getEmail().contains("@")) {
            throw  new RuntimeException("Votre mail invalide");
        }
        if(!user.getEmail().contains(".")) {
            throw  new RuntimeException("Votre mail invalide");
        }

        // Check if role is valid
        if (!isRoleValid(user.getRole())) {
            throw new RuntimeException("Role not valid !");
        }

        //Check if User already exist
        Optional<Users> userOptional = this.usersRepository.findByEmail(user.getEmail());
        if(userOptional.isPresent()) {
            throw  new RuntimeException("Cet Utilisateur existe déjà ! ");
        }
        
        Users userToSave = UserDto.toEntityUsers(user);

        String mdpCrypte = this.passwordEncoder.encode(user.getPassword());
        userToSave.setPassword(mdpCrypte);

        if (user.getIdStructure() != null){
            MinisterialStructure existingStructure = ministerialStrutureRepo.findById(user.getIdStructure())
                     .orElseThrow(() -> new RuntimeException("MinisterialStructure not found !"));
            
            userToSave.setMinisterialStructure(existingStructure);
        }

        //save user
        UserDto userDtoSaved = UserDto.fromEntityUsers(usersRepository.save(userToSave));

        return userDtoSaved != null;
    }


    private boolean isRoleValid(RoleType role) {
        for (RoleType value : RoleType.values()) {
            if (value == role) {
                return true;
            }
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.usersRepository
                .findByEmail(username)
                .orElseThrow(() -> new  UsernameNotFoundException("Aucun utilisateur ne corespond à cet identifiant"));
    
    }


    public String getCurrentUser(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getName();
    }

    public void logout(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authentication.setAuthenticated(false);
        SecurityContextHolder.clearContext();
    }


    public UserDto updateUserProfile(UserDto dto) throws Exception {
        
        dto.setPassword("123"); //To avoid error mapping
        
        List<String> errors= ValidateForms.validateUser(dto);
        if (!errors.isEmpty()){
            log.error("User is not valid {}:", dto);
            log.error("Liste des erreurs {}: ", errors);
            throw new Exception("User is not valid !", (Throwable) errors);
        }

        if(!dto.getEmail().contains("@")) {
            throw  new RuntimeException("Votre mail invalide");
        }
        if(!dto.getEmail().contains(".")) {
            throw  new RuntimeException("Votre mail invalide");
        }

        //An user can only update his profil so Check if user is the same

        String usernameConnected = this.getCurrentUser();

        if(!usernameConnected.equals(dto.getEmail())) {
            throw new Exception("L'utilisateur connecté ne correspond pas à l'utilisateur à mettre à jour");
        }
      
        //Check if User already exist
        Optional<Users> userToUpdate= this.usersRepository.findByEmail(usernameConnected);
        if(userToUpdate.isPresent()) {
            Users existingUser = userToUpdate.get();

            // Update fields for user's profile
            existingUser.setFirstName(dto.getFirstName());
            existingUser.setLastName(dto.getLastName());
            existingUser.setRole(dto.getRole());
            
            UserDto userToUpdated = UserDto.fromEntityUsers(usersRepository.save(existingUser));

            userToUpdated.setPassword(""); // Donn't send Password
            return userToUpdated;
        }
     
        throw new Exception("Utilisateur non trouvé");
    }

    public List<UserDto> findAll() {
       // Retrieving the list of all Users from BDD
        List<Users> usersList = usersRepository.findAll();
        
       // Transforming the user list into a MinistryDto list
       List<UserDto> userListDto = usersList.stream()
       .map(user -> {
           // Clear the password before converting to DTO
           user.setPassword("");
           return UserDto.fromEntityUsers(user);
       })
       .collect(Collectors.toList());

        return userListDto;
    }


    public UserDto findById(Long id) {
        if (id == null) {
            log.error("User ID is null");
            return null;
        }
 
        Optional<Users> optionalUser= usersRepository.findById(id);

        if (optionalUser.isPresent()) {
            optionalUser.get().setPassword("");
            return UserDto.fromEntityUsers(optionalUser.get());
        } else {
            throw new EntityNotFoundException(
                "No User with the ID = " + id + " is in the DB"
            );
        }
    }

    public Users getMe() throws Exception {

        String usernameConnected = this.getCurrentUser();

        Optional<Users> userToUpdate= this.usersRepository.findByEmail(usernameConnected);
        if(userToUpdate.isPresent()) {
            userToUpdate.get().setPassword(""); //send data without password
            return userToUpdate.get();//UserDto.fromEntityUsers(userToUpdate.get());
        }
        
        throw new Exception("Utilisateur non trouvé");
    }

    
}
