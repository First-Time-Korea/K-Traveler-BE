package com.ssafy.firskorea.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomController {
	@GetMapping("/")
	public String home() {
		return "index";
	}
}
