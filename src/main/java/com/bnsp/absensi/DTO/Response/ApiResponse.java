package com.bnsp.absensi.DTO.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;

    private int code;

    private String message;

    private T data;

    @Schema(description = "Error details (jika ada)")
    private Object errors;

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> created(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(HttpStatus.CREATED.value())
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> updated(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> deleted(String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> error(int code, String message, Object errors) {
        return ApiResponse.<T>builder()
                .success(false)
                .code(code)
                .message(message)
                .errors(errors)
                .build();
    }
}