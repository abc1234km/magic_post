package com.university.post.exception.role;

import com.university.post.exception.entity.EntityNotFound;

public class RoleNotFound extends EntityNotFound {
    public RoleNotFound(String message) {
        super(message);
    }
}
