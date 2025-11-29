package com.javaproject.Backend.service;

import java.util.List;
import java.util.Optional;

import com.javaproject.Backend.domain.User;
import com.javaproject.Backend.dto.request.update.UserUpdateRequest;
import com.javaproject.Backend.dto.response.UserResponse;

/**
 * Interface định nghĩa các dịch vụ (Service) liên quan đến Quản lý Người dùng (User Management).
 * * Chịu trách nhiệm thực hiện logic nghiệp vụ như truy xuất thông tin, cập nhật hồ sơ, 
 * và xử lý các thao tác liên quan đến người dùng.
 */
public interface UserService {
    
    /**
     * Truy xuất danh sách ID của tất cả người dùng hiện có trong hệ thống.
     * * Thường dùng cho mục đích quản trị hoặc các logic nội bộ.
     * @return Danh sách ID của người dùng (List<Long>).
     */
    List<Long> getAllUserIds();

    /**
     * Lấy ID của người dùng hiện tại đang gửi request.
     * * ID này được trích xuất từ JWT Token trong Security Context (dựa trên luồng xác thực).
     * @return ID của người dùng đang đăng nhập.
     */
    Long getCurrentUserId();

    /**
     * Truy xuất thông tin chi tiết của một người dùng dựa trên ID.
     * * Phương thức này chuyển đổi Entity sang DTO (UserResponse) trước khi trả về Controller.
     * @param userId ID của người dùng cần truy xuất.
     * @return UserResponse chứa thông tin không nhạy cảm của người dùng.
     */
    UserResponse getUserById(Long userId);

    /**
     * Xóa tài khoản người dùng khỏi hệ thống.
     * * Khi xóa, các dữ liệu liên quan (Expense, Budget) cũng sẽ bị xóa do cấu hình Cascade/Orphan Removal.
     * @param userId ID của người dùng cần xóa.
     */
    void deleteUser(Long userId);

    /**
     * Cập nhật thông tin hồ sơ của người dùng (ví dụ: fullname).
     * * Phương thức này sử dụng DTO UserResponse (có thể không chính xác nếu UserResponse không phải là DTO request).
     * * LƯU Ý: Có vẻ đây là phương thức trùng lặp/thừa và nên dùng phương thức bên dưới.
     * @param userId ID của người dùng cần cập nhật.
     * @param request Dữ liệu cập nhật.
     * @return UserResponse sau khi cập nhật thành công.
     */
    UserResponse updateUser(Long userId, UserResponse request);

    /**
     * Tìm kiếm một User (Entity) trong cơ sở dữ liệu dựa trên địa chỉ Email.
     * * Quan trọng cho việc xác thực và kiểm tra sự tồn tại của email.
     * @param email Địa chỉ email cần tìm kiếm.
     * @return Optional chứa đối tượng User nếu tìm thấy, ngược lại là Optional.empty().
     */
    Optional<User> findByEmail(String email);

    /**
     * Cập nhật thông tin hồ sơ của người dùng (chính xác hơn, sử dụng DTO request cụ thể).
     * * Phương thức này xử lý logic cập nhật dữ liệu (ví dụ: thay đổi mật khẩu, cập nhật fullname).
     * @param userId ID của người dùng cần cập nhật.
     * @param request Dữ liệu cập nhật từ client.
     * @return UserResponse sau khi cập nhật thành công.
     */
    UserResponse updateUser(Long userId, UserUpdateRequest request);
}