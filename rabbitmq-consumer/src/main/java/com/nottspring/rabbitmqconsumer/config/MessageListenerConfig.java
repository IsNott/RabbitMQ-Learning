package com.nottspring.rabbitmqconsumer.config;

import com.nottspring.rabbitmqconsumer.receiver.MyAckReceiver;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author Nott
 * @Date 2022/7/14
 */

/**
 * 和生产者的消息确认机制不同，因为消息接收本来就是在监听消息，符合条件的消息就会消费下来。
 * 消费者确认机制有三种：
 * <p>
 * ①自动确认， 这也是默认的消息确认情况。  AcknowledgeMode.NONE
 * RabbitMQ成功将消息发出（即将消息成功写入TCP Socket）中立即认为本次投递已经被正确处理，不管消费者端是否成功处理本次投递。
 * 所以这种情况如果消费端消费逻辑抛出异常，也就是消费端没有处理成功这条消息，那么就相当于丢失了消息。
 * 一般这种情况我们都是使用try catch捕捉异常后，打印日志用于追踪数据，这样找出对应数据再做后续处理。
 * <p>
 * ② 根据情况确认， 这个不做介绍
 * <p>
 * ③ 手动确认 ， 这个比较关键，也是我们配置接收消息确认机制时，多数选择的模式。
 * 消费者收到消息后，手动调用basic.ack/basic.nack/basic.reject后，RabbitMQ收到这些消息后，才认为本次投递成功。
 * basic.ack用于肯定确认
 * basic.nack用于否定确认（注意：这是AMQP 0-9-1的RabbitMQ扩展）
 * basic.reject用于否定确认，但与basic.nack相比有一个限制:一次只能拒绝单条消息
 * <p>
 * channel.basicReject(deliveryTag, true);  拒绝消费当前消息，如果第二参数传入true，就是将数据重新丢回队列里，那么下次还会消费这消息。设置false，就是告诉服务器，我已经知道这条消息数据了，因为一些原因拒绝它，而且服务器也把这个消息丢掉就行。 下次不想再消费这条消息了。
 * 使用拒绝后重新入列这个确认模式要谨慎，因为一般都是出现异常的时候，catch异常再拒绝入列，选择是否重入列。
 * 但是如果使用不当会导致一些每次都被你重入列的消息一直消费-入列-消费-入列这样循环，会导致消息积压。
 * <p>
 * channel.basicNack(deliveryTag, false, true);
 * 第一个参数依然是当前消息到的数据的唯一id;
 * 第二个参数是指是否针对多条消息；如果是true，也就是说一次性针对当前通道的消息的tagID小于当前这条消息的，都拒绝确认。
 * 第三个参数是指是否重新入列，也就是指不确认的消息是否重新丢回到队列里面去。
 */

//消费者确认消息配置
@Configuration
public class MessageListenerConfig {
    @Resource
    private CachingConnectionFactory connectionFactory;
    @Autowired
    private MyAckReceiver myAckReceiver;

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        // 并发消费
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(1);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); //MQ默认设置自动确认，改成手动
        container.setQueueNames("TestDirectQueue"); //设置队列
        container.setMessageListener(myAckReceiver);
        return container;
    }

}
