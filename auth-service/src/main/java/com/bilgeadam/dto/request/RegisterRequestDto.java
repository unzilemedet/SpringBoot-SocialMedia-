package com.bilgeadam.dto.request;

import com.bilgeadam.model.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequestDto {
    @NotEmpty(message = "Kullanıcı adını girmek zorunludur.")
    @Size(min = 3, max = 20, message = "Kullanıcı adı en az 3 en fazla 20 karakter olabilir.")
    private String username;
    @NotEmpty(message = "Ad girmek zorunludur.")
    private String name;
    @NotEmpty(message = "Soyad girmek zorunludur.")
    private String surname;
    @NotEmpty(message = "Email girmek zorunludur.")
    @Email(message = "Lütfen geçerli bir email giriniz.")
    private String email;
    @NotEmpty(message = "Şifre girmek zorunludur.")
    @Size(min = 8, max = 32, message = "Şifre en az 8 en çok 32 karakter olabilir.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[@#$%^&+=_!.])(?!.*\s).*$")
    private String password;
    private String repassword;
    private List<Role> roles;
}
