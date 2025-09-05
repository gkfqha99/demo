package com.example.demo.dao;

import com.example.demo.domain.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
	@Select("SELECT id, email, password, nickname FROM users WHERE email = #{email}")
	User findByEmail(String email);

	@Select("SELECT id, email, password, nickname FROM users WHERE id = #{id}")
	User findById(Long id);

	@Insert("INSERT INTO users(email, password, nickname) VALUES(#{email}, #{password}, #{nickname})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	void insert(User user);
}
