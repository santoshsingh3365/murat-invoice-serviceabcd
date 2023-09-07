package com.murat.invoice.generation.exception;

import com.murat.invoice.generation.model.ApiResponse;
import com.murat.invoice.generation.utils.ApplicationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFound(ResourceNotFound ex) {
        ApiResponse<Object> message = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), ApplicationUtils.getStringFromLocalDateTime(ZonedDateTime.now()), ex.getMessage(), null);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceAlreadyExists.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceAlreadyExists(ResourceAlreadyExists ex) {
        ApiResponse<Object> message = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), ApplicationUtils.getStringFromLocalDateTime(ZonedDateTime.now()), ex.getMessage(), null);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidRequest.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidRequest(InvalidRequest ex) {
        ApiResponse<Object> message = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), ApplicationUtils.getStringFromLocalDateTime(ZonedDateTime.now()), ex.getMessage(), null);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}
