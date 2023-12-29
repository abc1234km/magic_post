package com.university.post.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.university.post.payload.response.data.ErrorResponse;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ExceptionFilter {
    public static void setErrorResponse(HttpServletResponse response, HttpStatus httpStatus, String message) {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(httpStatus.value());
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        ErrorResponse errorResponse = new ErrorResponse(httpStatus, message);
        try {
            response.getWriter().write(ow.writeValueAsString(errorResponse));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
