package com.ssafy.firskorea.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssafy.firskorea.common.consts.RetConsts;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@Schema(title = "CommonResponse : 응답 포맷")
public class CommonResponse<T> {
    @JsonIgnore
    private final RetConsts retConsts;
    @Schema(description = "회원 아이디")
    private final String message;
    @Schema(description = "상태 코드")
    private final int code;
    @Schema(description = "데이터")
    private final T data;

    public static <T> CommonResponse<T> ok() {
        return new CommonResponse<>(
                RetConsts.NRM000,
                RetConsts.NRM000.getDescription(),
                RetConsts.NRM000.getCode(),
                null
        );
    }

    public static <T> CommonResponse<T> ok(T data) {
        return new CommonResponse<>(
                RetConsts.NRM000,
                RetConsts.NRM000.getDescription(),
                RetConsts.NRM000.getCode(),
                data
        );
    }

    public static <T> CommonResponse<T> failure(RetConsts status, String message) {
        return new CommonResponse<>(
                status,
                message,
                status.getCode(),
                null
        );
    }
}
