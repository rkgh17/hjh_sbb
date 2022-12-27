// 경로 : sbb/src/main/java/com/mysite/sbb/user/UserRepository.java

package com.mysite.sbb.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
	Optional<SiteUser> findByusername(String username); // 사용자 조회
}
