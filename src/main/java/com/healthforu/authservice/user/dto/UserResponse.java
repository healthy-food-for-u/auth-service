package com.healthforu.authservice.user.dto;


import com.healthforu.authservice.user.domain.User;

public record UserResponse(
        String id,
        String loginId,
        String userName
) {
    /** 엔티티를 응답 DTO로 변환 */
    public static UserResponse from(User user){
        return new UserResponse(
                user.getMongoId().toHexString(),
                user.getLoginId(),
                user.getUserName()
        );
    }

}
