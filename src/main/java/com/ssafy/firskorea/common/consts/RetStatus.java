package com.ssafy.firskorea.common.consts;

public enum RetStatus {

    SUCCESS(200),
    AUTH(400),
    VALIDATION(410),
    INFRA(420),
    BUSINESS(430);

    private int retCode;

    RetStatus(int retCode) {
        this.retCode = retCode;
    }
}
