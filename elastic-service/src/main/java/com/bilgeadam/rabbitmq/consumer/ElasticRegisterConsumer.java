package com.bilgeadam.rabbitmq.consumer;

import com.bilgeadam.rabbitmq.model.RegisterElasticModel;
import com.bilgeadam.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ElasticRegisterConsumer {
    private final UserProfileService userProfileService;

    @RabbitListener(queues = ("${rabbitmq.queueElasticRegister}"))
    public void createNewUser(RegisterElasticModel registerElasticModel){
        userProfileService.createUserWithRabbitMq(registerElasticModel);
    }
}
