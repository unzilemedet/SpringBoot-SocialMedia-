package com.bilgeadam.controller;

import com.bilgeadam.dto.ForgotPasswordToMailSendLinkResponseDto;
import com.bilgeadam.service.MailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/v1/mail")
@RestController
@RequiredArgsConstructor
public class MailSenderController {
    private final MailSenderService mailSenderService;

    @PostMapping("/forgot-password-send-mail-token")
    public ResponseEntity<Boolean> forgotPasswordMail(@RequestBody ForgotPasswordToMailSendLinkResponseDto dto){
        return ResponseEntity.ok(mailSenderService.sendMailForgotPassword(dto));
    }
}
