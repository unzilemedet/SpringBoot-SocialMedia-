package com.bilgeadam.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    //create post producer işlemi için gerekli değişkenler
    @Value("${rabbitmq.exchange}")
    private String exchange;
    @Value("${rabbitmq.queueCreatePost}")
    private String queuePost;
    @Value("${rabbitmq.bindingKeyCreatePost}")
    private String createPostBindingKey;

    @Bean
    Queue queuePost(){
        return new Queue(queuePost);
    }
    @Bean
    DirectExchange exchange(){
        return new DirectExchange(exchange);
    }
    @Bean
    public Binding postBindingKey(final Queue queuePost, final DirectExchange exchange){
        return BindingBuilder.bind(queuePost).to(exchange).with(createPostBindingKey);
    }
}
