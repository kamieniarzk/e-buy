package com.example.onlinestore.security;

public enum UserPermission {
    PRODUCT_READ("product:read"),
    PRODUCT_WRITE("product:write"),
    TRANSACTION_READ("transaction:read"),
    TRANSACTION_WRITE("transaction:write");

    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
