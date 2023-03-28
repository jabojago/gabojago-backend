package com.example.gabojago_server.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Getter
@Entity
@Table(name="member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder @Builder
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long useridx;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String birth;

    @Column(unique = true, nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    public void updatePassword(String password){
        this.password = password;
    }

    public void updateNickName(String nickname){
        this.nickname = nickname;
    }

    public void updatePhone(String phone){
        this.phone = phone;
    }

}
