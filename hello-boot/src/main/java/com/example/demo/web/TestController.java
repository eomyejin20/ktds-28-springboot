package com.example.demo.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class TestController {
	
	@GetMapping("/hello")
	public ResponseEntity<String> hello() {
		return new ResponseEntity<>("Hello Test Controller", HttpStatus.OK);
	}
	
	@GetMapping("/me")
	public String introduceMe(Model model) {
		model.addAttribute("name", "엄예진");
		model.addAttribute("job", "developer");
		model.addAttribute("age", 24);
		return "introduceme";
	}
}
