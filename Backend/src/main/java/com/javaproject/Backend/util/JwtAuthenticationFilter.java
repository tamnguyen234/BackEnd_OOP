package com.javaproject.Backend.util;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Lớp triển khai interface jakarta.servlet.Filter. Điều này cho phép nó chặn và xử lý request trước khi nó đến được servlet (Controller)
public class JwtAuthenticationFilter implements Filter {
    // sử dụng lớp tiện ích JwtUtils để thực hiện xác thực và trích xuất thông tin
    private final JwtUtils jwtUtils;

    public JwtAuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
public void doFilter(jakarta.servlet.ServletRequest servletRequest, jakarta.servlet.ServletResponse servletResponse,
            FilterChain filterChain) throws IOException, ServletException {
    
        // 1. Ép kiểu (Casting): Chuyển đổi đối tượng chung sang HttpServletRequest/Response để truy cập HTTP header
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        
        // 2. Trích xuất Header Authorization
        String header = request.getHeader("Authorization"); // Lấy giá trị của header "Authorization"
        String token = null;

        // 3. Kiểm tra định dạng Header: Xác định JWT
        if (header != null && header.startsWith("Bearer ")) {
            // Token được gửi theo định dạng tiêu chuẩn: "Bearer <token>"
            token = header.substring(7); // Cắt bỏ "Bearer " (7 ký tự) để lấy chuỗi token thô
        }

        // 4. Xử lý Token (Chỉ khi token tồn tại)
        if (token != null && jwtUtils.validateJwtToken(token)) {
            
            // 4a. Trích xuất thông tin người dùng từ token
            String email = jwtUtils.getEmailFromToken(token);
            Long userId = jwtUtils.getUserIdFromToken(token);
            
            // 4b. Tạo đối tượng Xác thực (Authentication)
            // Đây là đối tượng chuẩn mà Spring Security sử dụng để đại diện cho người dùng đã xác thực.
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    email, // Principal (Chủ thể): Đặt email làm định danh người dùng
                    null, // Credentials: Mật khẩu (đặt là null vì đã xác thực bằng token)
                    Collections.emptyList() // Authorities (Quyền hạn): Đặt là danh sách rỗng (có thể thay bằng roles/permissions thực tế)
            );
            
            // 4c. Thiết lập chi tiết xác thực (WebAuthenticationDetailsSource)
            // Gán các chi tiết bổ sung (như IP, session ID) vào đối tượng Authentication
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            
            // 4d. Thiết lập Context Bảo mật
            // Lưu trữ đối tượng Authentication vừa tạo vào SecurityContextHolder.
            // Đây là bước quan trọng nhất: cho Spring Security biết rằng người dùng hiện tại đã được xác thực thành công.
            SecurityContextHolder.getContext().setAuthentication(auth);
            
            // 4e. (Tùy chọn) Gắn userId vào Request Attribute
            // Giúp các Controller/Service tiếp theo có thể dễ dàng lấy userId mà không cần đọc lại token.
            request.setAttribute("userId", userId);
        }

        // 5. Chuyển request tới Filter tiếp theo trong chuỗi
        filterChain.doFilter(request, response);
    }
}
