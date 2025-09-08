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

	// 유저id랑 새닉네임 받아서 해당 유저 닉네임 수정. 서비스단 updateNickname이랑 연결
	@Update("UPDATE users SET nickname = #{nickname} WHERE id = #{id}")
	void updateNickname(@Param("id") Long id, @Param("nickname") String nickname);

	// 비밀번호 변경
	@Update("UPDATE users SET password = #{password} WHERE id = #{id}")
	void updatePassword(@Param("id") Long id, @Param("password") String encodedPassword);


}
