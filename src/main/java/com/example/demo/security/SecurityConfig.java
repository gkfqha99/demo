package com.example.demo.security;

import com.example.demo.dao.UserMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;

import java.util.List;

/*Security 설정 (허용/보호 경로 + CORS)
* Spring Security 전체 동작 규칙을 정의하는 클래스*/

@Configuration
public class SecurityConfig {

	private final JwtUtil jwtUtil;
	private final UserMapper userMapper;

	public SecurityConfig(JwtUtil jwtUtil, UserMapper userMapper) {
		this.jwtUtil = jwtUtil;
		this.userMapper = userMapper;
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable()) // API는 비활성화
				.cors(Customizer.withDefaults())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(
								"/api/auth/login",
								"/api/auth/register",
								"/health", "/db-ping",
								"/upload/**"
						).permitAll()
						.anyRequest().authenticated()/*requestMatchers 안에 든거 빼고는 다 인증필요란뜻*/
				)
				.addFilterBefore(new JwtAuthFilter(jwtUtil, userMapper), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	// 개발용 CORS (Vite dev 서버 허용)
	/*cors 는 뭐야 크로스 오리진 리소스 쉐어링 = 원래 브라우저는 각기 다른 포트의 요청
	못하게 막아두는데 cors 하면 크로스 되서 다른 포트의 요청도 받을수 있게됨.
	근데 이미 리액트에서 만든 ui에서 요청하면 백가서 디비에 있는거 끄집어 오면 cors 설정된거 아니냐
	할수있는데 이건 vite에서 살짝 컴터를 속여서 proxy를 바꿔줬기때문이다. 실제배포 할때는 이게
	안먹혀서 반드시 이렇게 cors 설정해주는 파일을 만들어 둬야한다.
	*/
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		var c = new CorsConfiguration();
		c.setAllowedOrigins(List.of("http://localhost:5173"));
		c.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
		c.setAllowedHeaders(List.of("*"));
		c.setAllowCredentials(true);
		var s = new UrlBasedCorsConfigurationSource();
		s.registerCorsConfiguration("/**", c);
		return s;
	}
}
