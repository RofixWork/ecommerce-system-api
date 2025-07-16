package com.rofix.ecommerce_system.exception;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.rofix.ecommerce_system.exception.base.*;
import com.rofix.ecommerce_system.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<Map<String, String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<Map<String, String>> errors = ex.getBindingResult().getAllErrors().stream().map((error) -> {
            Map<String, String> map =  new HashMap<>();
            String fieldName = ((FieldError)error).getField(),
                    fieldValue = error.getDefaultMessage();
            map.put("field", fieldName);
            map.put("message", fieldValue);
            return map;
        }).toList();

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse> handleNotFoundException(NotFoundException e){
        ApiResponse apiResponse = new ApiResponse(e.getMessage(), false);

        return ResponseEntity.status(e.getStatusCode()).body(apiResponse);
    }
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse> handleUnauthorizedException(UnauthorizedException e){
        ApiResponse apiResponse = new ApiResponse(e.getMessage(), false);

        return ResponseEntity.status(e.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse> handleConflictException(ConflictException e){
        ApiResponse apiResponse = new ApiResponse(e.getMessage(), false);

        return ResponseEntity.status(e.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse> handleBadRequestException(BadRequestException e){
        ApiResponse apiResponse = new ApiResponse(e.getMessage(), false);

        return ResponseEntity.status(e.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<ApiResponse> handleAPIException(APIException e){
        ApiResponse apiResponse = new ApiResponse(e.getMessage(), false);

        return ResponseEntity.status(e.getStatusCode()).body(apiResponse);
    }

//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    public ResponseEntity<ApiResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex)
//    {
//        String msg = "Invalid request format or data type error.";
//
//        Throwable cause = ex.getCause();
//
//        if (cause instanceof InvalidFormatException formatException) {
//            String fieldName = formatException.getPath().get(0).getFieldName();
//            if ("appointmentDate".equals(fieldName)) {
//                msg = "Invalid date format for appointmentDate. Expected format: yyyy-MM-dd HH:mm.";
//            } else if ("dob".equals(fieldName)) {
//                msg = "Invalid date format, expected yyyy-MM-dd.";
//            }
//        }
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(msg, false));
//    }
}
