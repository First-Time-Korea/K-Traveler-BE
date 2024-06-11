package com.ssafy.firskorea.controller;

import com.ssafy.firskorea.common.consts.RetConsts;
import com.ssafy.firskorea.common.dto.CommonResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(Exception.class)
    public CommonResponse<?> exception(Exception e) {
        e.printStackTrace();
        return CommonResponse.failure(RetConsts.ERR600, "로직 처리 중 문제 발생");
    }
}