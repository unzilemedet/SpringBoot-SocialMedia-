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
    //create post consumer
    //@Value("${rabbitmq.queueCreatePost}")
    String queueCreatePost = "queue-post";
    @Bean
    Queue queueCreatePost(){
        return new Queue(queueCreatePost);
    }

    //register consumer queue
    @Value("${rabbitmq.queueRegister}")
    String queueNameRegister;
    @Bean
    Queue registerQueue(){
        return new Queue(queueNameRegister);
    }

    //register elasticsearch producer
    @Value("${rabbitmq.exchange}")
    private String exchange;
    @Value("${rabbitmq.queueElasticRegister}")
    private String queueElasticRegister;
    @Value("${rabbitmq.elasticRegisterBindingKey}")
    private String elasticRegisterBindingKey;

    @Bean
    Queue queueElasticRegister(){
        return new Queue(queueElasticRegister);
    }
    @Bean
    DirectExchange exchange(){
        return new DirectExchange(exchange);
    }
    @Bean
    public Binding bindingElasticRegister(final Queue queueElasticRegister, final DirectExchange exchange) {
        return BindingBuilder.bind(queueElasticRegister).to(exchange).with(elasticRegisterBindingKey);
    }
}
