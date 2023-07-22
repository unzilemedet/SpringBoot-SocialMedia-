package com.bilgeadam.rabbitmq.producer;

import com.bilgeadam.rabbitmq.model.RegisterModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterProducer {
    @Value("${rabbitmq.exchange-auth}")
    private String exchange;
    @Value("${rabbitmq.registerKey}")
    private String registerBindingKey;
    private final RabbitTemplate rabbitTemplate;

    public void sendNewUser(RegisterModel registerModel){
        rabbitTemplate.convertAndSend(exchange,registerBindingKey,registerModel);
    }
}
