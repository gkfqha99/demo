package com.example.demo.controller;

import com.example.demo.dao.TodoMapper;
import com.example.demo.domain.Todo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class TodoController {
	private final TodoMapper mapper;
	public TodoController(TodoMapper mapper){ this.mapper = mapper; }

	@GetMapping("/api/todos")
	public List<Todo> list(){ return mapper.findAll(); }
}
