package com.telia.Lease_management.entity;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.telia.Lease_management.entity.common.AbstractEntity;
import com.telia.Lease_management.entity.common.DocumentType;
import com.telia.Lease_management.entity.common.RoleType;
import com.telia.Lease_management.entity.common.TypeUser;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "utilisateur")
public class Users extends AbstractEntity implements UserDetails{   

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String matricule;
    private String ifuLandLord;
    
    @Column(nullable = true)
    private boolean canReceiveNewProjectEmail = false;

    private boolean actif = true;

    @Enumerated(EnumType.STRING)
    private TypeUser typeUser;    

    
    @Enumerated(EnumType.STRING)
    private RoleType role;
        
    // @ManyToOne
    // @JoinColumn(name = "ministry_id")
    // private Ministry ministry;

    @ManyToOne
    @JoinColumn(name = "structure_id")
    private MinisterialStructure ministerialStructure; 
        
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+this.role.getLibelle()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.actif;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.actif;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.actif;
    }

    @Override
    public boolean isEnabled() {
        return this.actif;
    }
}
