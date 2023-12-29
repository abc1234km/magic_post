package com.university.post.payload.response.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
public class DataRepsonse<T> extends Response {

    private T data;

    public DataRepsonse(HttpStatus status, T data, String message) {
        super(status, message);
        this.data = data;
    }
}
