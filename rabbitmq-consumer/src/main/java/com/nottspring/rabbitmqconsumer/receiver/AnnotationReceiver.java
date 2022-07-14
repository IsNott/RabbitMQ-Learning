package com.nottspring.rabbitmqconsumer.receiver;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @author Nott
 * @Date 2022/7/14
 */

@Component
@Slf4j
public class AnnotationReceiver {

    /*
     * @RabbitListener 和 @RabbitHandler 搭配使用
     * @RabbitListener 可以标注在类上面，需配合 @RabbitHandler 注解一起使用
     * @RabbitListener 标注在类上面表示当有收到消息的时候，就交给 @RabbitHandler 的方法处理，具体使用哪个方法处理，根据 MessageConverter 转换后的参数类型
     * @RabbitListener 除了可以定义队列和交换机的绑定，还有其他常用的属性：ackMode（确认机制）、concurrency（并发消费）
     */

    //当Queue不存在，由我们主动创建
    // 通过 @RabbitListener 的 bindings 属性声明 Binding（若 RabbitMQ 中不存在该绑定所需要的 Queue、Exchange、RouteKey 则自动创建，若存在则抛出异常）
    @RabbitListener(bindings = {
            //主动创建Queue
            @QueueBinding(value = @Queue(value = "Annotation-Queue", durable = "false", autoDelete = "true"),
                    //指定交换机名称和类型
                    exchange = @Exchange(value = "Annotation-Exchange", type = ExchangeTypes.DIRECT),
                    //指定路由键
                    key = "Annotation-routingKey"),
    }, ackMode = "MANUAL") //AcknowledgeMode:NONE、MANUAL、AUTO
    @RabbitHandler
    //使用 @Payload 和 @Headers 注解可以消息中的 body 与 headers 信息
    public void consumerNoQueue(@Payload Map msg, @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag, Channel channel) throws IOException {
        // deliveryTag: 对于每个Channel来说，每个消息都会有一个DeliveryTag，一般用接收消息的顺序(index)来表示，一条消息就为1
        try {
            log.info("Consumer's AnnotationReceiver Received message: " + msg.toString());
            channel.basicAck(deliveryTag, true); //手动ack
            log.info("Consumer's AnnotationReceiver acknowledge messageTag: " + deliveryTag);
        } catch (IOException e) {
            channel.basicReject(deliveryTag, true); //出错重新入列
        }
    }
}
