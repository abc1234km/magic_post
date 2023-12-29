package com.university.post.controller;

import com.university.post.exception.entity.EntityException;
import com.university.post.exception.entity.EntityNotFound;
import com.university.post.exception.role.RoleNotFound;
import com.university.post.exception.user.UserException;
import com.university.post.exception.user.UserNotFound;
import com.university.post.payload.response.data.ErrorResponse;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler({UserException.class, EntityException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUserException(UserException userException) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, userException.getMessage());
    }

    @ExceptionHandler({EntityNotFound.class, RoleNotFound.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEntityNotFound(EntityNotFound entityNotFound) {
        return new ErrorResponse(HttpStatus.NOT_FOUND, entityNotFound.getMessage());
    }

//    @ExceptionHandler(RoleNotFound.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ErrorResponse handleRoleNotFound(RoleNotFound roleNotFound) {
//        return new ErrorResponse(HttpStatus.NOT_FOUND, roleNotFound.getMessage());
//    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleBadCredentialsException() {
        return new ErrorResponse(HttpStatus.UNAUTHORIZED, "Xác thực không thành công");
    }
}
