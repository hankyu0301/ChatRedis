package com.example.chatredis.global.auth.guard;


import com.example.chatredis.domain.user.entity.UserRole;
import com.example.chatredis.domain.util.AuthUtil;

import java.util.List;

public abstract class Guard {
    public final boolean check(Long id) {
        return hasRole(getRoleTypes()) || isResourceOwner(id);
    }

    abstract protected List<UserRole> getRoleTypes();
    abstract protected boolean isResourceOwner(Long id);

    private boolean hasRole(List<UserRole> roleTypes) {
        return AuthUtil.extractUserRole().containsAll(roleTypes);
    }
}
