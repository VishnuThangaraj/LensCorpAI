package com.vishnuthangaraj.LensCorpAI.Model;

import com.vishnuthangaraj.LensCorpAI.Enums.Gender;
import com.vishnuthangaraj.LensCorpAI.Enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUser implements UserDetails {

    /*
        CLASS NAME  : AppUser
        DESCRIPTION : This class represents a model entity that encapsulates essential information
                      about a user within the application. This model integrates with Spring Security's
                      `UserDetails` interface, enabling seamless integration with authentication and
                      authorization mechanisms. Instances of this class store user details,
                      including their unique identifier, name, gender, role, email, password
                      associated with the user.
        ATTRIBUTES : - `id` (UUID): Unique identifier generated for each user.
                     - `name` (String): Represents the full name of the user.
                     - `gender` (Gender): Enumerated type capturing the user's gender.
                     - `role` (Role): Enumerated type representing the user's role in the application (e.g., USER or ADMIN).
                     - `email` (String): Unique identifier for the user's account, used for authentication and communication.
                     - `password` (String): Securely stores the user's hashed password for authentication.
    */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
