package com.nottspring.rabbitmqconsumer.receiver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Nott
 * @Date 2022/7/14
 */

@Component
@RabbitListener(queues = "fanout.C")
@Slf4j
public class FanoutCReceiver {

    @RabbitHandler
    public void process(Map msg) {
        log.info("Consumer's FanoutCReceiver Received message: " + msg.toString());
    }
}
