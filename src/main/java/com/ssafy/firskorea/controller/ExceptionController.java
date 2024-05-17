package com.ssafy.firskorea.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> exception(Exception e, Model model) {
		e.printStackTrace();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		resultMap.put("status", "error");
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
}