package com.healthforu.authservice.user.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

@Document(collection = "users")
@Getter
@NoArgsConstructor
public class User {

    @Id
    private ObjectId mongoId;

    @Field("id")
    @Indexed(unique = true)
    private String loginId;

    private String userName;

    @Indexed(unique = true)
    private String email;

    @Indexed(unique = true)
    private String mobile;

    private String password; // 암호화된 비밀번호 ($2b$10... 형식이니 그대로 String)

    @Builder
    public User(String loginId, String userName, String email, String mobile, String password){
        this.loginId=loginId;
        this.userName=userName;
        this.email=email;
        this.mobile=mobile;
        this.password=password;
    }
}
