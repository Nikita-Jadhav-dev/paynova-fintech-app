package com.paynova.app.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.paynova.app.entity.enums.Role;
import com.paynova.app.entity.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String upiId;
    private String profileImageUrl;
    private Role role;
    private UserStatus status;
    private LocalDateTime createdAt;
}
