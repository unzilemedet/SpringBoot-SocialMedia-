package com.bilgeadam.rabbitmq.producer;

import com.bilgeadam.rabbitmq.model.CreatePostModel;
import com.bilgeadam.rabbitmq.model.UserProfileResponseModel;
import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatePostProducer {
    @Value("${rabbitmq.exchange}")
    private String exchange;
    @Value("${rabbitmq.bindingKeyCreatePost}")
    private String createPostBindingKey;

    private final RabbitTemplate rabbitTemplate;
    public Object createPost(CreatePostModel model){
        return rabbitTemplate.convertSendAndReceive(exchange, createPostBindingKey, model);
    }
}
