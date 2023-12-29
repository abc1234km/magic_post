package com.university.post.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageResponse {
    SUCCESS_REGISTER("Đăng ký thành công"),

    ERROR_REGISTER("Đâng ký không thành công"),

    SUCCESS_LOGIN("Đăng nhập thành công");

    private final String message;
}
