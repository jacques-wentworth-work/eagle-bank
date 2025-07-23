package com.eaglebank.entity;

import java.util.Arrays;
import java.util.List;

public enum Role {

    CUSTOMER(List.of(Permission.USER, Permission.USER_ACCOUNT,  Permission.USER_ACCOUNT_TRANSACTION)),

    ADMIN(List.of(Permission.ADMIN));

    private List<Permission> permissions;

    Role(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}
