package com.ssafy.firskorea.common.consts;

public enum RetStatus {

    SUCCESS(200),
    AUTH(400),
    VALIDATION(410),
    INFRA(500),
    BUSINESS(600);

    private int retCode;

    RetStatus(int retCode) {
        this.retCode = retCode;
    }
}
