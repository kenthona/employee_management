package no.group.employeemanagement.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface GetAuthorities {
    Collection<? extends GrantedAuthority> getAuthorities();
}
