package com.javaproject.Backend.events;

import org.springframework.context.ApplicationEvent;

/**
 * Đại diện cho một Sự kiện (Event) được kích hoạt (published) khi một Người dùng (User) mới
 * được tạo thành công trong hệ thống.
 * * Event này là một tín hiệu để các Event Listener (ví dụ: DefaultBudgetInitializer)
 * có thể thực hiện các hành động cần thiết sau khi user được tạo.
 * * Kế thừa từ ApplicationEvent 
 */
public class UserCreatedEvent extends ApplicationEvent {
    private final Long userId;

    public UserCreatedEvent(Object source, Long userId) {
        super(source);
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}