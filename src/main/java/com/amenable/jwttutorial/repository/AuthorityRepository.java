package com.amenable.jwttutorial.repository;

import com.amenable.jwttutorial.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
