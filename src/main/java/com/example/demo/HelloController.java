package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class HelloController {
	@GetMapping("/api/hello-list")
	public List<String> sayHelloList(){
		List<String> result = new ArrayList<>();
		result.add("안녕 , 안녕! 진경");
		result.add("안녕 , 안녕! 철수");
		return result;
	}

}
