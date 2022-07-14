package com.nott.spring.rabbitprovider.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Nott
 * @Date 2022/7/14
 */

//设置生产者推送消息回调
@Configuration
@Slf4j
public class RabbitConfig {

    /**
     * 生产者的消息确认回调函数，存在ConfirmCallback和ReturnsCallback
     * 消息推送存在四种情况：
     * 消息推送到server，但是在server里找不到交换机，触发ConfirmCallback.situation=false
     * 消息推送到server，找到交换机了，但是没找到队列，触发ConfirmCallback.situation=true和ReturnCallback
     * 消息推送到sever，交换机和队列都没找到，触发ConfirmCallback.situation=false
     * 消息推送成功,触发ConfirmCallback.situation=true
     *
     * @param factory
     * @return
     */

    @Bean
    public RabbitTemplate templateConfig(ConnectionFactory factory) {
        RabbitTemplate template = new RabbitTemplate();
        template.setConnectionFactory(factory);
        //设置开启Mandatory,消息推送都会强制触发回调函数
        template.setMandatory(true);
        template.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            //confirm参数：回调数据，确认情况，原因
            public void confirm(CorrelationData correlationData, boolean b, String s) {
                log.info("ConfirmCallback data: " + correlationData);
                log.info("ConfirmCallback situation: " + b);
                log.info("ConfirmCallback cause: " + s);
            }
        });

        template.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returnedMessage) {
                log.info("ReturnCallback msg: " + returnedMessage);
            }
        });
        return template;
    }
}
