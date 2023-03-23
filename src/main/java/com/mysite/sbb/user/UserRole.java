package com.mysite.sbb.user;

import lombok.Getter;


//class가 아닌 열거자료형 enum, 상수자료형 이므로 Setter 없이 Getter 만 사용 가능하도록 함.
@Getter
public enum UserRole {

    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    UserRole(String value)
    {
        this.value = value;
    }

    private String value;

}
