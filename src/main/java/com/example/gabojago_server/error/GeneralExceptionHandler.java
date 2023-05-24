package com.example.gabojago_server.error;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler({
            GabojagoException.class
    })
    public ResponseEntity<?> handleGabojagoException(GabojagoException e) {
        return ResponseEntity.status(e.getStatus())
                .body(ErrorResponse.from(e));
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.from(e));
    }

    @Getter
    static class ErrorResponse {
        private int statusCode;
        private String message;
        private String solution;

        public static ErrorResponse from(GabojagoException exception) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.statusCode = exception.getStatus();
            errorResponse.message = exception.getMessage();
            errorResponse.solution = exception.getSolution();
            return errorResponse;
        }

        public static ErrorResponse from(MethodArgumentNotValidException exception) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.statusCode = HttpStatus.BAD_REQUEST.value();
            errorResponse.message = convert(exception.getAllErrors());
            errorResponse.solution = "입력 데이터를 수정해주세요";

            return errorResponse;
        }

        private static String convert(List<ObjectError> objectErrors){
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.writeValueAsString(objectErrors);
            }catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }


    }

}
