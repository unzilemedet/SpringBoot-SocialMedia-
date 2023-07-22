package com.bilgeadam.service;

import com.bilgeadam.dto.ForgotPasswordToMailSendLinkResponseDto;
import com.bilgeadam.rabbitmq.model.RegisterMailModel;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSenderService {
    private final JavaMailSender javaMailSender;

    public void sendMail(RegisterMailModel registerMailModel){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("${spring.mail.username}");
        mailMessage.setTo(registerMailModel.getEmail());
        mailMessage.setSubject("AKTIVASYON KODU");
        mailMessage.setText(
                registerMailModel.getName() + " " + registerMailModel.getSurname() + " başarıyla kayıt oldunuz.\n" +
                "Aktivasyon Link: " + "http://localhost:8090/api/v1/auth/activate-status?token="+registerMailModel.getToken()

        );
        javaMailSender.send(mailMessage);
    }

    public Boolean sendMailForgotPassword(ForgotPasswordToMailSendLinkResponseDto dto){
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("${spring.mail.username}");
            mailMessage.setTo(dto.getEmail());
            mailMessage.setSubject("SIFRE SIFIRLAMA LINKI");
            mailMessage.setText("Aşağıdaki bağlantıya tıklayarak şifrenizi sıfırlayabilirsiniz.\n" +
                    "http://localhost:8090/api/v1/auth/forgot-password");
            //TODO forgot password url'i eski ve yeni şifrenin bulunduğu bir sayfaya yönlendirecek.
            //http://localhost:8090/api/v1/auth/reset-password
            javaMailSender.send(mailMessage);
        }catch (Exception e){
            e.getMessage();
        }
        return true;
    }
}
