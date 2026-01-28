package com.healthforu.authservice.auth.dto;

public record TokenDto(
        String accessToken,
        String refreshToken,
        String id,
        String loginId,
        String userName
) {
   public static TokenDto from(String accessToken, String refreshToken, String id, String loginId, String userName){
       return new TokenDto(
               accessToken,
               refreshToken,
               id,
               loginId,
               userName
       );
   }
}
