package com.example.gabojago_server.error;

import lombok.Getter;

@Getter
public class GabojagoException extends RuntimeException {
    private int status;
    private String message;
    private String solution;

    public GabojagoException(ErrorCode errorCode) {
        this.message = errorCode.getMessage();
        this.status = errorCode.getHttpStatus().value();
        this.solution = errorCode.getSolution();
    }

    public GabojagoException(ErrorCode errorCode, String message) {
        this.message = message;
        this.solution = errorCode.getSolution();
        this.status = errorCode.getHttpStatus().value();
    }

    public GabojagoException(ErrorCode errorCode, String message, String solution) {
        this.message = message;
        this.status = errorCode.getHttpStatus().value();
        this.solution = solution;
    }
}
