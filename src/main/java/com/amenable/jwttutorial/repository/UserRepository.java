package com.amenable.jwttutorial.repository;

import com.amenable.jwttutorial.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = "authorities") // Lazy 조회가 아니라 Eager 조회로 authorities 정보를 같이 가져오는 것
    Optional<User> findOneWithAuthoritiesByUsername(String username);
}
