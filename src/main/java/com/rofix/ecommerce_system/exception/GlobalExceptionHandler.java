package com.rofix.ecommerce_system.exception;

import com.rofix.ecommerce_system.exception.base.*;
import com.rofix.ecommerce_system.response.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            Map<String, String> map = new HashMap<>();
            String fieldName = ((FieldError) error).getField(),
                    fieldValue = error.getDefaultMessage();
            map.put("field", fieldName);
            map.put("message", fieldValue);
            return map;
        }).toList();

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<APIResponse> handleNotFoundException(NotFoundException e) {
        APIResponse apiResponse = new APIResponse(e.getMessage(), false);

        return ResponseEntity.status(e.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<APIResponse> handleUnauthorizedException(UnauthorizedException e) {
        APIResponse apiResponse = new APIResponse(e.getMessage(), false);

        return ResponseEntity.status(e.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<APIResponse> handleConflictException(ConflictException e) {
        APIResponse apiResponse = new APIResponse(e.getMessage(), false);

        return ResponseEntity.status(e.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<APIResponse> handleBadRequestException(BadRequestException e) {
        APIResponse apiResponse = new APIResponse(e.getMessage(), false);

        return ResponseEntity.status(e.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse> handleAPIException(APIException e) {
        APIResponse apiResponse = new APIResponse(e.getMessage(), false);

        return ResponseEntity.status(e.getStatusCode()).body(apiResponse);
    }

//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    public ResponseEntity<APIResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex)
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
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new APIResponse(msg, false));
//    }
}
