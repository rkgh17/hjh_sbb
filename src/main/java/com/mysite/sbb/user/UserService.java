// 경로 : sbb/src/main/java/com/mysite/sbb/user/UserService.java

package com.mysite.sbb.user;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원 데이터 생성 메서드
    public SiteUser create(String username, String email, String password) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setEmail(email);
        
        // 암호화
        user.setPassword(passwordEncoder.encode(password));
        this.userRepository.save(user);
        return user;
    }
    
    // 사용자 조회 메서드
    public SiteUser getUser(String username) {
    	
    	// UserRepository - findByusername
        Optional<SiteUser> siteUser = this.userRepository.findByusername(username);
        if (siteUser.isPresent()) {
            return siteUser.get(); 
        }
        
        // 조회 실패
        else {
            throw new DataNotFoundException("siteuser not found");
        }
    }
    
}