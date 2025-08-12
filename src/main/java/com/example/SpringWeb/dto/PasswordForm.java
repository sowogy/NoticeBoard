package com.example.SpringWeb.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordForm {
    @NotBlank(message = "기존 패스워드를 입력하세요")
    private String old;
    @Size(min=8, message = "8글자 이상 입력하세요")
    @NotBlank(message = "새로운 패스워드를 입력하세요")
    private String new_password;
    @Size(min=8, message="8글자 이상 입력하세요")
    @NotBlank(message = "새로 입력한 패스워드를 확인하세요")
    private String new_passwordConfirm;
}
