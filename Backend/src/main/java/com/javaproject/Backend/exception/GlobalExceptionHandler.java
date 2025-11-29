package com.javaproject.Backend.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
/**
 * Lớp Xử lý Ngoại lệ Toàn cục (Global Exception Handler).
 * * @RestControllerAdvice: Là một component đặc biệt của Spring, hoạt động như một bộ
 * điều phối để lắng nghe và xử lý các ngoại lệ (exception) phát sinh từ tất cả
 * các Controller (@Controller và @RestController) trong ứng dụng.
 * * Kết hợp chức năng của @ControllerAdvice và @ResponseBody, cho phép phương thức
 * xử lý trả về dữ liệu JSON trực tiếp cho client.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    // Lỗi 404 không tồn tại
    @ExceptionHandler(ResourceNotFoundException.class) 
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