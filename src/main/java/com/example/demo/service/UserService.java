package com.example.demo.service;

import com.example.demo.dao.UserMapper;
import com.example.demo.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

	private final UserMapper userMapper;

	// Lombok @RequiredArgsConstructor 없이, 명시적 생성자 사용
	public UserService(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	/** 사용자 단건 조회 */
	@Transactional(readOnly = true)
	public User getById(Long userId) {
		return userMapper.findById(userId);
	}

	/** 프로필 이미지 파일명 업데이트 */
	@Transactional
	public void updateProfileImage(Long userId, String filename) {
		userMapper.updateProfileImage(userId, filename);
	}
}
