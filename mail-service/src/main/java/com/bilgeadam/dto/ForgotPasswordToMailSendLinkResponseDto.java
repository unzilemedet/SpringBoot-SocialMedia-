package com.bilgeadam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ForgotPasswordToMailSendLinkResponseDto {
    
    String token;
    String email;
}
