package com.javaproject.Backend.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id // đánh dấu primarykey
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Tự động sinh giá trị primary key (ID) cho entity khi lưu vào database.
    private Long userId;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 100)
    private String passwordHash;

    @Column(length = 100)
    private String fullName;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    // Đánh dấu createdAT sẽ được gán tự động
    public void prePersist() {
        if (createdAt == null)
            createdAt = LocalDateTime.now();
    }
}
