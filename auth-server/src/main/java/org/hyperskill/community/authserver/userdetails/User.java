package org.hyperskill.community.authserver.userdetails;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

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
}
