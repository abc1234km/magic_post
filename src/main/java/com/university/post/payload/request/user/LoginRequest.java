package com.university.post.payload.request.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class LoginRequest {
    @NotNull
    private String userId;

    @NotBlank
    private String password;
}
