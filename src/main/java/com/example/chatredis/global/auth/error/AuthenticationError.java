package com.example.chatredis.global.auth.error;

import com.example.chatredis.domain.util.FailResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AuthenticationError {

    public static void setErrorResponse(HttpStatus status, int code, String msg, HttpServletResponse response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(status.value());
        response.getWriter().write(objectMapper.writeValueAsString(
                FailResponse.of(code, msg)
        ));
    }

}
