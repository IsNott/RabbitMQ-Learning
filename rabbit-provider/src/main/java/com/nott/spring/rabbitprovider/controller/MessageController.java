package com.nott.spring.rabbitprovider.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.UUID;

import static com.nott.spring.rabbitprovider.config.DirectRabbitConfig.DIRECTROUTING;
import static com.nott.spring.rabbitprovider.config.DirectRabbitConfig.DIREXCHANGE;
import static com.nott.spring.rabbitprovider.config.FanoutRabbitConfig.FANOUT;
import static com.nott.spring.rabbitprovider.config.TopicRabbitConfig.*;

/**
 * @author Nott
 * @Date 2022/7/13
 */

@RestController
public class MessageController {

    @Resource
    private RabbitTemplate rabbitTemplate; //使用rabbitTemplate，提供发送、接收方法

    /**
     * 直连交换机
     *
     * @return
     */
    @RequestMapping("dirMsg")
    public String sendDirtectMessage() {
        String messageId = String.valueOf(UUID.randomUUID());
        String message = "hello,RabbitMQ!";
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        HashMap<String, String> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("message", message);
        map.put("time", time);
        //将消息绑定路由键发送到对应交换机
        rabbitTemplate.convertAndSend(DIREXCHANGE, DIRECTROUTING, map);
        return "ok";
    }

    /**
     * 主题交换机
     *
     * @return
     */
    @RequestMapping("/man")
    public String sendTopicMsg() {
        String messageId = String.valueOf(UUID.randomUUID());
        String message = "MSG: MAN!";
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        HashMap<String, String> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("message", message);
        map.put("time", time);
        //将消息绑定路由键发送到对应交换机
        rabbitTemplate.convertAndSend(TOPICEXCHANGE, MAN, map);
        return "ok";
    }


    @RequestMapping("/woman")
    public String sendTopicMsg01() {
        String messageId = String.valueOf(UUID.randomUUID());
        String message = "MSG: WOMAN!!!!";
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        HashMap<String, String> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("message", message);
        map.put("time", time);
        //将消息绑定路由键发送到对应交换机
        rabbitTemplate.convertAndSend(TOPICEXCHANGE, WOMAN, map);
        return "ok";
    }

    /**
     * 扇形交换机
     *
     * @return
     */
    @RequestMapping("/fan")
    public String sendFanoutMsg01() {
        String messageId = String.valueOf(UUID.randomUUID());
        String message = "MSG: Hello,Fanout!";
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        HashMap<String, String> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("message", message);
        map.put("time", time);
        //扇形交换机不需要路由键
        rabbitTemplate.convertAndSend(FANOUT, null, map);
        return "ok";
    }

    /**
     * 测试消息推送：消息推送到server，但是在server里找不到交换机
     *
     * @return
     */
    @RequestMapping("/noExist")
    public String sendNotExistMsg01() {
        String messageId = String.valueOf(UUID.randomUUID());
        String message = "MSG: Hello,noExist!";
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        HashMap<String, String> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("message", message);
        map.put("time", time);
        rabbitTemplate.convertAndSend("Exchange-not-exist", DIRECTROUTING, map);
        return "ok";
    }

    /**
     * 测试消息推送到server，找到交换机了，但是没找到队列
     *
     * @return
     */
    @RequestMapping("/noExist02")
    public String sendNotExistMsg02() {
        String messageId = String.valueOf(UUID.randomUUID());
        String message = "MSG: Hello,noExist!";
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        HashMap<String, String> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("message", message);
        map.put("time", time);
        rabbitTemplate.convertAndSend(TOPICEXCHANGE, "RoutingKey-not-exist", map);
        return "ok";
    }

    /**
     * 测试消息推送到sever，交换机和队列都没找到
     *
     * @return
     */
    @RequestMapping("/noExist03")
    public String sendNotExistMsg03() {
        String messageId = String.valueOf(UUID.randomUUID());
        String message = "MSG: Hello,noExist!";
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        HashMap<String, String> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("message", message);
        map.put("time", time);
        rabbitTemplate.convertAndSend("Exchange-not-exist", "RoutingKey-not-exist", map);
        return "ok";
    }

    /**
     * 测试消息推送成功
     *
     * @return
     */
    @RequestMapping("/success")
    public String sendsuccessMsg() {
        String messageId = String.valueOf(UUID.randomUUID());
        String message = "MSG: Hello,RabbitMQ!";
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        HashMap<String, String> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("message", message);
        map.put("time", time);
        rabbitTemplate.convertAndSend(DIREXCHANGE, DIRECTROUTING, map);
        return "ok";
    }

    /**
     * 使用注解的形式配置消费者
     * @return
     */
    @RequestMapping("/anno")
    public String testAnnoMsg() {
        String messageId = String.valueOf(UUID.randomUUID());
        String message = "MSG: Hello,Annotation-RabbitMQ!";
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        HashMap<String, String> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("message", message);
        map.put("time", time);
        rabbitTemplate.convertAndSend("Annotation-Exchange", "Annotation-routingKey", map);
        return "ok";
    }


}
