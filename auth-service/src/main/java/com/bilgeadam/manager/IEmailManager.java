package com.bilgeadam.manager;

import com.bilgeadam.dto.request.ForgotPasswordToMailSendLinkRequestDto;
import com.bilgeadam.dto.response.ForgotPasswordMailResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import static com.bilgeadam.constant.ApiUrls.*;

@FeignClient(url = "http://localhost:8085/api/v1/mail", name = "auth-mail")
public interface IEmailManager {
    @PostMapping(FORGOT_PASSWORD_SEND_MAIL_TOKEN)
    public ResponseEntity<Boolean> forgotPasswordMail(@RequestBody ForgotPasswordToMailSendLinkRequestDto forgotPassword);
}
