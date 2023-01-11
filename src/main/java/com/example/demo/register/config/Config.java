package com.example.demo.register.config;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public Declarables amqp() {
        var exchange = new FanoutExchange("demo.register", true, false);
        var userQueue = new Queue("demo.register.user", true);
        return new Declarables(exchange, userQueue, BindingBuilder.bind(userQueue).to(exchange));
    }

}
