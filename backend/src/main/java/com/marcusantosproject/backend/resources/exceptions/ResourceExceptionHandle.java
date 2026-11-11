package com.marcusantosproject.backend.resources.exceptions;

import com.marcusantosproject.backend.services.exceptions.ObjectNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ResourceExceptionHandle {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request){
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(System.currentTimeMillis() ,status.value(), "Not Found", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }
}

