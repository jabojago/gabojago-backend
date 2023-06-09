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

    @NotBlank(message = "핸드폰번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^[0-9]{3}+-[0-9]{4}+-[0-9]{4}$", message = "번호 형식이 올바르지 않습니다.")
    private String phone;
}
