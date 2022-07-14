package com.nott.spring.rabbitprovider.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Nott
 * @Date 2022/7/13
 */

//测试主题交换机
@Configuration
public class TopicRabbitConfig {

    public static final String MAN = "topic.man";
    public static final String WOMAN = "topic.woman";
    public static final String TOPIC = "topic.#";
    public static final String TOPICEXCHANGE = "topicExchange";

    @Bean
    public Queue firstQueue() {
        return new Queue(MAN);
    }

    @Bean
    public Queue secondQueue() {
        return new Queue(WOMAN);
    }

    @Bean
    TopicExchange testTopicExchange() {
        return new TopicExchange(TOPICEXCHANGE);
    }

    //将firstQueue和topicExchange绑定,而且绑定的键值为topic.man
    //这样只要是消息携带的路由键是topic.man,才会分发到该队列
    @Bean
    Binding bindingTopMsg() {
        return BindingBuilder.bind(firstQueue()).to(testTopicExchange()).with(MAN);
    }

    //将secondQueue和topicExchange绑定,而且绑定的键值为用上通配路由键规则topic.#
    // 这样只要是消息携带的路由键是以topic.开头,都会分发到该队列
    @Bean
    Binding bindingTopMsgCopy() {
        return BindingBuilder.bind(secondQueue()).to(testTopicExchange()).with(TOPIC);
    }
}
