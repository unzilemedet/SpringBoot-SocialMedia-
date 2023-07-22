package com.bilgeadam.rabbitmq.consumer;

import com.bilgeadam.rabbitmq.model.RegisterModel;
import com.bilgeadam.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j //konsolda loglama işlemleri için kullanılır(Simple Logging Facade for Java)
public class RegisterConsumer {
    private final UserProfileService userProfileService;

    @RabbitListener(queues = ("${rabbitmq.queueRegister}"))
    public void createUser(RegisterModel model){
        log.info("User {}", model.toString());
        userProfileService.createUser(model);
    }
}
