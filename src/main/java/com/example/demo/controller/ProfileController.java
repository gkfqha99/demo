package com.example.demo.controller;

import com.example.demo.domain.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ProfileController {

	private final UserService userService;

	@Value("${app.upload.dir}")
	private String uploadDir; // 예: C:/Users/user/upload/profile

	public ProfileController(UserService userService) {
		this.userService = userService;
	}

	@PostConstruct
	public void init() {
		File dir = new File(uploadDir);
		if (!dir.exists()) {
			boolean ok = dir.mkdirs();
			if (!ok) {
				System.err.println("Failed to create upload directory: " + uploadDir);
			}
		}
	}

	// ✅ JWT에서 userId를 읽어온다: (Long) auth.getPrincipal()
	@PostMapping("/me/profile-image")
	public ResponseEntity<Map<String, String>> uploadMyProfileImage(
			Authentication auth,
			@RequestParam("file") MultipartFile file
	) throws IOException {

		if (auth == null || auth.getPrincipal() == null) {
			return ResponseEntity.status(401).body(Map.of("error", "unauthorized"));
		}
		Long userId = (Long) auth.getPrincipal();

		if (file == null || file.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("error", "empty_file"));
		}

		String ext = Optional.ofNullable(file.getOriginalFilename())
				.filter(n -> n.contains("."))
				.map(n -> n.substring(n.lastIndexOf('.')))
				.orElse("");
		String lower = ext.toLowerCase();
		if (!(lower.equals(".jpg") || lower.equals(".jpeg") || lower.equals(".png") || lower.equals(".webp"))) {
			return ResponseEntity.badRequest().body(Map.of("error", "unsupported_type"));
		}

		String safeName = "u" + userId + "-" + System.currentTimeMillis() + ext;
		Path target = Path.of(uploadDir, safeName);
		file.transferTo(target.toFile());

		userService.updateProfileImage(userId, safeName);

		String url = "/upload/" + safeName; // WebConfig의 정적 매핑 이용
		return ResponseEntity.ok(Map.of("filename", safeName, "url", url));
	}

	// (옵션) 내 정보 조회에 profileImage까지 보고 싶으면 이 엔드포인트로 확인
	@GetMapping("/me/profile")
	public ResponseEntity<User> myProfile(Authentication auth) {
		if (auth == null || auth.getPrincipal() == null) {
			return ResponseEntity.status(401).build();
		}
		Long userId = (Long) auth.getPrincipal();
		User u = userService.getById(userId);
		if (u == null) return ResponseEntity.notFound().build();
		return ResponseEntity.ok(u);
	}
}
