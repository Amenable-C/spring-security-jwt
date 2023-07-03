package com.amenable.jwttutorial.service;

import com.amenable.jwttutorial.dto.UserDto;
import com.amenable.jwttutorial.entity.Authority;
import com.amenable.jwttutorial.entity.User;
import com.amenable.jwttutorial.exception.DuplicateMemberException;
import com.amenable.jwttutorial.exception.NotFoundMemberException;
import com.amenable.jwttutorial.repository.UserRepository;
import com.amenable.jwttutorial.util.SecurityUtil;
import java.util.Collections;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원 가입
    @Transactional
    public UserDto signup(UserDto userDto) {
        if (userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        // 권한 정보 생성
        Authority authority = Authority.builder()
            .authorityName("ROLE_USER")
            .build();

        // 유저 생성
        User user = User.builder()
            .username(userDto.getUsername())
            .password(passwordEncoder.encode(userDto.getPassword()))
            .nickname(userDto.getNickname())
            .authorities(Collections.singleton(authority))
            .activated(true)
            .build();

        return UserDto.from(userRepository.save(user));
    }

    // username에 해당하는 user 정보를 가져오는것
    // ROLE_ADMIN 용
    @Transactional(readOnly = true)
    public UserDto getUserWithAuthorities(String username) {
        return UserDto.from(userRepository.findOneWithAuthoritiesByUsername(username).orElse(null));
    }

    // 현재 SecurityContext에 저장되어 있는 user 정보만 가져옴
    // ROLE_USER 용
    @Transactional(readOnly = true)
    public UserDto getMyUserWithAuthorities() {
        return UserDto.from(
            SecurityUtil.getCurrentUsername()
                .flatMap(userRepository::findOneWithAuthoritiesByUsername)
                .orElseThrow(() -> new NotFoundMemberException("Member not found"))
        );
    }
}
