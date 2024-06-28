package com.ssafy.firskorea.controller;

import com.ssafy.firskorea.common.consts.RetConsts;
import com.ssafy.firskorea.common.dto.CommonResponse;
import com.ssafy.firskorea.common.exception.DuplicationMemberIdException;
import com.ssafy.firskorea.common.exception.IncorrectMemberException;
import com.ssafy.firskorea.common.exception.InvalidRefreshTokenException;
import com.ssafy.firskorea.common.exception.MemberAlreadyWithdrawnException;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<CommonResponse<?>> handleParameterValidationExceptions(ConstraintViolationException e) {
        e.printStackTrace();

        StringBuilder errors = new StringBuilder();
        e.getConstraintViolations().forEach(violation -> {
            String message = violation.getMessage();
            errors.append(message).append(" ");
        });
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.failure(RetConsts.ERR410, errors.toString().trim()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<CommonResponse<?>> handleDtoValidationExceptions(MethodArgumentNotValidException e) {
        e.printStackTrace();

        StringBuilder errors = new StringBuilder();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errors.append(errorMessage).append(" ");
        });
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.failure(RetConsts.ERR410, errors.toString().trim()));
    }

    @ExceptionHandler({NoResourceFoundException.class, HttpRequestMethodNotSupportedException.class})
    @ResponseBody
    public ResponseEntity<CommonResponse<?>> handleNoResourceFoundExceptions(NoResourceFoundException e) {
        e.printStackTrace();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(CommonResponse.failure(RetConsts.ERR404, "잘못된 경로로 접근"));
    }
    
   @ExceptionHandler(DuplicationMemberIdException.class)
   @ResponseBody
   public ResponseEntity<CommonResponse<?>> handleDuplicationMemberIdExceptions(DuplicationMemberIdException e) {
	   return ResponseEntity
			   .status(HttpStatus.CONFLICT)
			   .body(CommonResponse.failure(RetConsts.ERR603, e.getMessage()));
   }
   
   @ExceptionHandler({IncorrectMemberException.class, InvalidRefreshTokenException.class})
   @ResponseBody
   public ResponseEntity<CommonResponse<?>> handleIncorrectMemberExceptions(RuntimeException e) {
	   return ResponseEntity
			   .status(HttpStatus.UNAUTHORIZED)
			   .body(CommonResponse.failure(RetConsts.ERR602, e.getMessage()));
   }
   
   @ExceptionHandler(MemberAlreadyWithdrawnException.class)
   @ResponseBody
   public ResponseEntity<CommonResponse<?>> handleMemberAlreadyWithdrawnExceptions(MemberAlreadyWithdrawnException e) {
	   return ResponseEntity
			   .status(HttpStatus.FORBIDDEN)
			   .body(CommonResponse.failure(RetConsts.ERR601, e.getMessage()));
   }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<CommonResponse<?>> handleBusinessException(Exception e) {
        e.printStackTrace();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.failure(RetConsts.ERR600, "로직 처리 중 문제 발생"));
    }

}