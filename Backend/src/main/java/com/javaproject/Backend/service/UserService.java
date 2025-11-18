package com.javaproject.Backend.service;

import java.util.List;

import com.javaproject.Backend.dto.response.RegisterResponse;

/**
 * Interface định nghĩa các nghiệp vụ liên quan đến User
 */
public interface UserService {

    /**
     * Lấy thông tin người dùng theo ID.
     * 
     * @param userId ID của người dùng cần tìm.
     * @return Đối tượng RegisterResponse chứa thông tin người dùng.
     */
    RegisterResponse getUserById(Long userId);

    /**
     * Lấy thông tin tất cả người dùng.
     * (Phương thức này không có @Override trong UesrServiceImpl, nhưng nó là một
     * nghiệp vụ liên quan đến User.)
     * 
     * @return Danh sách các đối tượng RegisterResponse.
     */
    List<RegisterResponse> getAllUsers();

    /**
     * Cập nhật thông tin người dùng.
     * 
     * @param userId  ID của người dùng cần cập nhật.
     * @param request Dữ liệu cập nhật.
     * @return Đối tượng RegisterResponse sau khi cập nhật.
     */
    RegisterResponse updateUser(Long userId, RegisterResponse request);
}