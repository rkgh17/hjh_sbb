// 경로 : sbb/src/main/java/com/mysite/sbb/user/UserDetailsService.java

package com.mysite.sbb.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService { 
// UserDetailsService 인터페이스 상속
// UserDetailsService 인터페이스 : loadUserByUsername 메서드를 구현하도록 강제하는 인터페이스

    private final UserRepository userRepository;

    @Override // loadUserByUsername 구현부 : 사용자명으로 비밀번호를 조회하여 리턴하는 메서드
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	
    	// 사용자명으로 SiteUser 객체를 조회
        Optional<SiteUser> _siteUser = this.userRepository.findByusername(username);
        
        // 사용자명에 해당하는 데이터가 없을 경우
        if (_siteUser.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
        }
        
        // 사용자 권한 검사
        SiteUser siteUser = _siteUser.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        // admin 유저
        if ("admin".equals(username)) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        } 
        
        // 일반 유저
        else {
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }
        
        // 스프링 시큐리티의 User객체 리턴
        return new User(siteUser.getUsername(), siteUser.getPassword(), authorities);
    }
}