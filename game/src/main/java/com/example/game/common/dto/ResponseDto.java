package com.example.game.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ResponseDto<T> {
    private boolean success;
    private T data;
    private Error error;

    public ResponseDto(boolean success, T data, Error error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }

    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<>(true, data, null);
    }

    public static <T> ResponseDto<T> fail(String message, String detail) {
        return new ResponseDto<>(false, null, new Error(message, detail));
    }

    @Getter
    @AllArgsConstructor
    static class Error {
        private String message;
        private String detail;
    }

}
