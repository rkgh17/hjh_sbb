// 경로 sbb/src/main/java/com/mysite/sbb/SeurityConfig.java
package com.mysite.sbb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.mysite.sbb.user.UserSecurityService;

import lombok.RequiredArgsConstructor;

@EnableGlobalMethodSecurity(prePostEnabled = true) // @PreAuthorize 애너테이션을 사용하기 위한 에너테이션
@RequiredArgsConstructor // final이 붙거나 @NotNull이 붙은 필드의 생성자 자동 생성하는 에너테이션
@Configuration // 스프링의 환경설정 파일임을 의미하는 에너테이션
@EnableWebSecurity // 모든 요청URL이 스프링 시큐리티의 제어를 받도록 만드는 에너테이션
public class SecurityConfig {
	
	// 스프링 시큐리티에 로그인 검증 등록
	private final UserSecurityService userSecurityService;
	
	// 스프링 시큐리티 세부설정하기
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		
		// 로그인을 하지 않더라도 모든 페이지에 접근할 수 있음
		http.authorizeHttpRequests().antMatchers("/**").permitAll()
		
			// H2 콘솔은 예외처리하기
			.and()
				.csrf().ignoringAntMatchers("/h2-console/**")
			
			// X-Frame-Options 설정
			.and()
				.headers()
				.addHeaderWriter(new XFrameOptionsHeaderWriter(
						XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
			
			// 로그인URL 매핑
			.and()
				.formLogin()
				.loginPage("/user/login")
				.defaultSuccessUrl("/")
				
			// 로그아웃URL 매핑
			.and()
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
				.logoutSuccessUrl("/")
				.invalidateHttpSession(true) // 로그아웃시 이전에 생성된 사용자 세션 삭제
		;
		return http.build();
	}
	
	// 암호화 방식 설정
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	// 스프링 시큐리티 인증 담당
	@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
	}

}
