package com.example.securitypractice.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.example.securitypractice.security.UserPermission.*;

public enum UserRole {
    ADMIN(Sets.newHashSet(PRODUCT_READ, PRODUCT_WRITE, TRANSACTION_READ, TRANSACTION_WRITE)),
    SELLER(Sets.newHashSet(PRODUCT_READ, PRODUCT_WRITE, TRANSACTION_READ)),
    CUSTOMER(Sets.newHashSet(PRODUCT_READ, TRANSACTION_READ, TRANSACTION_WRITE));

    private final Set<UserPermission> permissions;

    UserRole(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<UserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
