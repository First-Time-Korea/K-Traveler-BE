package com.ssafy.firskorea.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssafy.firskorea.common.consts.RetConsts;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class ApiResponse<T> {
    @JsonIgnore
    private final RetConsts retConsts;
    private final String message;
    private final int code;
    private final T data;

    public static <T> ApiResponse<T> ok() {
        return new ApiResponse<>(
                RetConsts.NRM000,
                RetConsts.NRM000.getDescription(),
                RetConsts.NRM000.getCode(),
                null
        );
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(
                RetConsts.NRM000,
                RetConsts.NRM000.getDescription(),
                RetConsts.NRM000.getCode(),
                data
        );
    }

    public static <T> ApiResponse<T> failure(RetConsts status, String message) {
        return new ApiResponse<>(
                status,
                message,
                status.getCode(),
                null
        );
    }

}
