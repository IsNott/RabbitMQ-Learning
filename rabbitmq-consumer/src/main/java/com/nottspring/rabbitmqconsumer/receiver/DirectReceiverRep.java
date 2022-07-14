package com.nottspring.rabbitmqconsumer.receiver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.nottspring.rabbitmqconsumer.config.DirectRabbitConfig.DIRECTQUEUE;

/**
 * @author Nott
 * @Date 2022/7/13
 */

//@Component
//@Slf4j
//@RabbitListener(queues = DIRECTQUEUE) //Rabbit监听，监听队列TestDirectQueue
//public class DirectReceiverRep {
//
//    @RabbitHandler
//    public void process(Map msg) {
//        log.info("Consumer's listener02 Received message: " + msg.toString());
//    }
//}
