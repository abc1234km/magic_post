package com.university.post.exception.user;

import com.university.post.exception.entity.EntityNotFound;

public class UserNotFound extends EntityNotFound {
   public UserNotFound(String message) {
       super(message);
   }
}
