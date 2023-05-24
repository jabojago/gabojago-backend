package com.example.gabojago_server.repository.member;

import com.example.gabojago_server.model.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);    //중복 가입 방지 위함

    boolean existsByNickname(String nickname);    //중복 가입 방지 위함
}
