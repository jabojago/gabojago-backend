package com.example.gabojago_server.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Entity
@Table(name="member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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


    @Builder
    public Member(String email, String password, String name, String nickname, String birth, String phone,
                  Authority authority){
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.birth = birth;
        this.phone = phone;
        this.authority = authority;
    }

    public void updatePassword(String password){
        this.password = password;
    }

    public void updateNickName(String nickname){
        this.nickname = nickname;
    }

    public void updatePhone(String phone){
        this.phone = phone;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        else if (!(obj instanceof Member)) return false;
        Member member = (Member) obj;
        return Objects.equals(useridx, member.getUseridx());
    }

    @Override
    public int hashCode(){
        return useridx != null ? useridx.hashCode() : 0;
    }

}
