package com.example.gabojago_server.repository;

import com.example.gabojago_server.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);    //중복 가입 방지 위함
}
