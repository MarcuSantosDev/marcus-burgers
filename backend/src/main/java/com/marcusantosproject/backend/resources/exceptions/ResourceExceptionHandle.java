package com.marcusantosproject.backend.resources.exceptions;

import com.marcusantosproject.backend.services.exceptions.CategoryHasProducts;
import com.marcusantosproject.backend.services.exceptions.CategoryNotFound;
import com.marcusantosproject.backend.services.exceptions.InsufficientStock;
import com.marcusantosproject.backend.services.exceptions.ResourceNotFound;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ResourceExceptionHandle {

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<StandardError> ResourceNotFound(ResourceNotFound e, HttpServletRequest request){
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(System.currentTimeMillis() ,status.value(), "Not Found", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(CategoryNotFound.class)
    public ResponseEntity<StandardError> CategoryNotFound (CategoryNotFound e, HttpServletRequest request){
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(System.currentTimeMillis() ,status.value(), "Not found.", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(InsufficientStock.class)
    public ResponseEntity<StandardError> InsufficientStock(InsufficientStock e, HttpServletRequest request){
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError err = new StandardError(System.currentTimeMillis() ,status.value(), "Conflict", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(CategoryHasProducts.class)
    public ResponseEntity<StandardError> categoryHasProducts(CategoryHasProducts e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError err = new StandardError(System.currentTimeMillis(), status.value(), "Conflict", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }
}

