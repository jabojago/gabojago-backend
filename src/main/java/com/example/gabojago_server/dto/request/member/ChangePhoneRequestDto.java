package com.example.gabojago_server.dto.request.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePhoneRequestDto {

    @NotBlank(message = "이메일을 입력하세요.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "핸드폰번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^[0-9]+-[0-9]+-[0-9]$", message = "번호 형식이 올바르지 않습니다.")
    private String phone;
}
