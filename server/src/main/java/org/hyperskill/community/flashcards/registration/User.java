package org.hyperskill.community.flashcards.registration;

import java.util.Collection;
import java.util.Collections;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * entity class for registered users, that implements UserDetails and whose instances thus serve the
 * DaoAuthenticationProvider (AuthenticationManager).
 */
@Getter
@Setter
@RequiredArgsConstructor
@Accessors(chain = true)
@Document
public class User implements UserDetails {
    @Id
    private String username;
    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
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
