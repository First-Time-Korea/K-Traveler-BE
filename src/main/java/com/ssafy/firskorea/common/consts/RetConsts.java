package com.ssafy.firskorea.common.consts;

import lombok.Getter;

@Getter
public enum RetConsts {

    NRM000(RetStatus.SUCCESS, 200, "성공"),
    ERR401(RetStatus.AUTH, 401, "유효하지 않은 토큰"), //Unauthorized
    ERR403(RetStatus.AUTH, 403, "토큰 누락"), //Forbidden
    ERR500(RetStatus.INFRA, 500, "파일 업로드 에러"),
    ERR501(RetStatus.INFRA, 501, "파일 경로 문제"),
    ERR600(RetStatus.BUSINESS, 600, "해당 유저가 없어요"),
    ERR601(RetStatus.BUSINESS, 601, "회원 정보가 일치하지 않습니다."),
    ;

    private final RetStatus retStatus;
    private final int code;
    private final String description;

    RetConsts(RetStatus retStatus, int code, String description) {
        this.retStatus = retStatus;
        this.code = code;
        this.description = description;
    }
}
