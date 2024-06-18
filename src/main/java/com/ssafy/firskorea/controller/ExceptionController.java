package com.ssafy.firskorea.controller;

import com.ssafy.firskorea.common.consts.RetConsts;
import com.ssafy.firskorea.common.dto.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public CommonResponse<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.info("Request 유효성 검증 실패");
        StringBuilder errors = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errors.append(errorMessage).append(" ");
        });
        return CommonResponse.failure(RetConsts.ERR410, errors.toString().trim());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public CommonResponse<?> exception(Exception e) {
        e.printStackTrace();
        return CommonResponse.failure(RetConsts.ERR600, "로직 처리 중 문제 발생");
    }


}