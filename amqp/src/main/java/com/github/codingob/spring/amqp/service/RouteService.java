package com.github.codingob.spring.amqp.service;

import com.github.codingob.spring.amqp.entity.Entity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author codingob
 * @version 1.0.0
 * @since JDK1.8
 */

@Service
@Slf4j
public class RouteService {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Transactional(rollbackFor = Exception.class)
    public void send(Object message, int route) {
        rabbitTemplate.convertAndSend("demo.direct", route+"", message);
        log.info("发送成功: " + message);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue,
            exchange = @Exchange(
                    value = "demo.direct"),
            key = {"1", "4", "7"}))
    @RabbitHandler
    public void receive(Entity entity) {
        log.info("消费者1: " + entity);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue,
            exchange = @Exchange(
                    value = "demo.direct"), key = {"2", "8", "9"}))
    @RabbitHandler
    public void receive2(Entity entity) {
        log.info("消费者2: " + entity);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue,
            exchange = @Exchange(
                    value = "demo.direct"), key = {"0", "3", "5","6"}))
    @RabbitHandler
    public void receive3(Entity entity) {
        log.info("消费者3: " + entity);
    }
}