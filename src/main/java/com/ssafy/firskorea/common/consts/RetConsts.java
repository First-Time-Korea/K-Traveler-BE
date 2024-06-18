package com.ssafy.firskorea.common.consts;

import lombok.Getter;

@Getter
public enum RetConsts {

    NRM000(RetStatus.SUCCESS, 200, "성공"),
    NRM001(RetStatus.SUCCESS, 201, "데이터 생성 성공"),

    ERR400(RetStatus.AUTH, 400, "잘못된 요청"), //Bad Request
    ERR401(RetStatus.AUTH, 401, "권한 없음"), //Unauthorized

    ERR410(RetStatus.VALIDATION, 410, "잘못된 포맷으로 요청"),

    ERR500(RetStatus.INFRA, 500, "서버 에러"),
    ERR501(RetStatus.INFRA, 501, "파일 업로드 에러"),
    ERR502(RetStatus.INFRA, 502, "파일 경로 조회 실패"),

    ERR600(RetStatus.BUSINESS, 600, "비즈니스 로직 처리 중 문제 발생"),
    ERR601(RetStatus.BUSINESS, 601, "존재하지 않는 회원"),
    ERR602(RetStatus.BUSINESS, 602, "회원 정보 불일치"),
    ERR603(RetStatus.BUSINESS, 603, "중복된 회원 아이디"),
    ERR604(RetStatus.BUSINESS, 604, "GPT가 잘못된 구조로 번역"),
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
