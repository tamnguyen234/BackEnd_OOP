package com.javaproject.Backend.util;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

// đánh dấu lớp này là một Spring Bean để Spring có thể quản lý và Dependency Inject nó vào các lớp khác
@Component
public class JwtUtils {
    // Khóa Bí Mật (Secret Key): Dùng để ký (sign) token, đảm bảo tính toàn vẹn
    // (integrity).
    private final Key key = Keys.hmacShaKeyFor("replace_this_with_very_long_secret_key_for_prod_please".getBytes());
    // Thời gian Hết hạn (Expiration Time) token
    private final long expirationMs = 1000L * 60 * 60 * 24; // 24h

    // ==== Thiết lập token ====
    public String generateToken(String email, Long userId) {
        return Jwts.builder()
                .setSubject(email) // Thiết lập "Subject" (chủ thể) của token, thường là email/username
                .claim("userId", userId) // Thiết lập "Claim" (thông tin tùy chỉnh) cho userId
                .setIssuedAt(new Date()) // Thiết lập thời gian token được tạo (iat - Issued At)
                .setExpiration( // Thiết lập thời gian token hết hạn (exp - Expiration)
                        new Date(System.currentTimeMillis() // trả về thời gian hiện tại của hệ thống (current time)
                                                            // dưới dạng một số nguyên lớn
                                + expirationMs)) // thời gian hiện tại + thời gian tồn tại -> ngày hết hạn
                .signWith(key, SignatureAlgorithm.HS256) // Ký token bằng Khóa Bí Mật và thuật toán HS256
                .compact(); // Xây dựng và nén token thành chuỗi JWS (JSON Web Signature)
        // Định dạng header.payload.signature mà client có thể sử dụng.
    }

    // ==== Kiểm tra token =====
    public boolean validateJwtToken(String token) {
        try {
            // Đầu vào: Chuỗi token
            // Nhiệm vụ của Parser:
            // Tách chuỗi token thành ba phần (header, payload, signature).
            // Xác thực chữ ký (Signature).
            // Giải mã phần Payload để trích xuất các Claims (như userId, exp, sub).
            Jwts.parserBuilder() // Tạo Parser: Jwts.parserBuilder()
                    .setSigningKey(key).build() // thiết lập khóa ký và build
                    .parseClaimsJws(token); // nếu không hợp lệ ném ra JwtException
            return true; // token hợp lệ
        } catch (JwtException e) {
            // Bắt mọi ngoại lệ liên quan đến JWT (ExpiredJwtException, SignatureException,
            // v.v.)
            return false; // Token không hợp lệ
        }
    }

    // ==== Trích xuất giá trị từ trường Subject đã lưu trong token ====
    public String getEmailFromToken(String token) {
        // Phân tích token và lấy ra các Claims (thông tin chứa trong payload)
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.getSubject(); //// Trả về giá trị đã đặt trong .setSubject()
    }

    // ==== Trích xuất giá trị từ trường userId tùy chỉnh ====
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        Object v = claims.get("userId"); // // Lấy giá trị của claim "userId"

        // Xử lý kiểu dữ liệu:
        // Thư viện JWT có thể đọc giá trị số nguyên (ví dụ: Long) từ JSON dưới dạng
        // Integer,
        // đặc biệt nếu giá trị này đủ nhỏ. Cần phải kiểm tra và chuyển đổi thành Long.
        if (v instanceof Integer) {
            return ((Integer) v).longValue();
        } else if (v instanceof Long) {
            return (Long) v;
        } else {
            return null;
        }
    }
}
