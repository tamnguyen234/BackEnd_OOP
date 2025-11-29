package com.javaproject.Backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaproject.Backend.domain.User;

/**
 * Interface Repository chịu trách nhiệm tương tác với cơ sở dữ liệu cho thực thể Người dùng (User).
 * * Kế thừa (extends) từ JpaRepository để thừa hưởng các phương thức CRUD cơ bản
 * và các tính năng truy vấn mạnh mẽ của Spring Data JPA.
 * * Kiểu dữ liệu của Entity là User, kiểu dữ liệu của ID là Long.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Tìm và trả về một User dựa trên địa chỉ Email của họ.
     * * Phương thức truy vấn được tạo tự động (Derived Query Method) dựa trên tên trường.
     * * Rất quan trọng cho chức năng Đăng nhập (Login) và kiểm tra sự tồn tại của Email.
     * @param email Địa chỉ email cần tìm kiếm.
     * @return Optional chứa đối tượng User nếu tìm thấy, ngược lại là Optional.empty().
     */
    Optional<User> findByEmail(String email);
}
