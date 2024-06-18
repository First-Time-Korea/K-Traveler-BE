package com.ssafy.firskorea.config;

public enum ValidationConfig {
    MEMBER_ID_MIN_LENGTH(1),
    MEMBER_ID_MAX_LENGTH(20),
    PASSWORD_MIN_LENGTH(4),
    PASSWORD_MAX_LENGTH(20),
    //최소 하나의 숫자, 영문자, 특수 문자, 공백 없음, 최소 8자 이상
    //PASSWORD_REGEX("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*])(?=\\S+$).{8,}$"),
    //정수
    CONTENT_ID_FORMAT("^\\d+$"), // 정수
    SIDO_CODE_FORMAT("^[0-9]+$"), // 정수
    GUGUN_CODE_FORMAT("^[0-9]+$"), // 정수
    THEME_CODE_FORMAT("^[a-zA-Z]$"), // 알파벳 한 글자
    CATEGORY_CODE_FORMAT("^[a-zA-Z]\\d{2}$") // 알파벳 한 글자 다음에 정수 2글자
    ;

    private final int value;
    private final String stringValue;

    ValidationConfig(int value) {
        this.value = value;
        this.stringValue = null;
    }

    ValidationConfig(String stringValue) {
        this.value = -1;
        this.stringValue = stringValue;
    }

    public int getIntValue() {
        return value;
    }

    public String getStringValue() {
        return stringValue;
    }
}
