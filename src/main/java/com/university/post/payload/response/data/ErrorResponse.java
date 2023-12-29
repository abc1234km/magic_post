package com.university.post.payload.response.data;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class ErrorResponse extends Response {
    public ErrorResponse(HttpStatus status, String message) {
        super(status, message);
    }
}
