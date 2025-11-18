package com.javaproject.Backend.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.Valid;

@RestControllerAdvice
// Là component đặc biệt trong Spring Boot, lắng nghe tất cả controller để xử lý exception.
// = @ControllerAdvice + @ResponseBody, nên trả về JSON trực tiếp cho client.
public class GlobalExceptionHandler {
    // bắt lỗi 404 không tồn tại
    @ExceptionHandler(ResourceNotFoundException.class) // Định nghĩa loại lỗi sẽ xử lý
    public ResponseEntity<?> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }
    
    // Bắt các lỗi 400 validation khi @Valid trong DTO request thất bại.
    // getFieldErrors() trả về danh sách lỗi từng field.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    // Lỗi khác trả về 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", ex.getMessage()));
    }
}