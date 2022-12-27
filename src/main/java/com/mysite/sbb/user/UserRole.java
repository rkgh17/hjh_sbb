// 경로 : sbb/src/main/java/com/mysite/sbb/user/UserRole.java
package com.mysite.sbb.user;

import lombok.Getter;


@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER"); // 권한설정 - 관리자, 이용자

    UserRole(String value) {
        this.value = value;
    }

    private String value;
}