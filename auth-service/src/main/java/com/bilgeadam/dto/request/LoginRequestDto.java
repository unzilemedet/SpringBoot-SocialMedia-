package com.bilgeadam.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequestDto {
    @NotEmpty(message = "Lütfen email giriniz")
    String email;
    @NotEmpty(message = "Şifrenizi giriniz")
    String password;
}
