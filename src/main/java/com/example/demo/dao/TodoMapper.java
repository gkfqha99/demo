package com.example.demo.dao;

import com.example.demo.domain.Todo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface TodoMapper {
	@Select("SELECT id, title, done FROM todo")
	List<Todo> findAll();
}
