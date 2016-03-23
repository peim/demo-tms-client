package ru.taximaxim.demo.security;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private static final long serialVersionUID = 2827853102924988639L;

    private String context;

    public CustomAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
        this.context = null;
    }

    public CustomAuthenticationToken(Object principal, Object credentials,
            Collection<? extends GrantedAuthority> authorities, String context) {
        super(principal, credentials, authorities);
        this.context = context;
    }

    public String getContext() {
        return context;
    }
}
