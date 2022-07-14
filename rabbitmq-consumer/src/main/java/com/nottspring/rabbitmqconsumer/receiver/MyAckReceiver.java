package com.nottspring.rabbitmqconsumer.receiver;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareBatchMessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @author Nott
 * @Date 2022/7/14
 */
//手动确认消息监听类
//之前的相关监听器可以先注释掉，以免造成多个同类型监听器都监听同一个队列。
@Component
@Slf4j
public class MyAckReceiver implements ChannelAwareBatchMessageListener {
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            String msg = message.toString(); //Message对象重写了toString方法，获取的是发送的消息数据
            log.info("MyAckReceiver receiver a message which detail is : " + msg);
            log.info("Message from :" + message.getMessageProperties().getConsumerQueue());
            channel.basicAck(deliveryTag, true); //将该消息手动确认,第二个参数，手动确认可以被批处理，当该参数为 true 时，则可以一次性确认 delivery_tag 小于等于传入值的所有消息
        } catch (Exception e) {
            //假设确认时出错，否定确认消息重新入队
            channel.basicReject(deliveryTag, false);//第二个参数，true会重新放回队列，所以需要自己根据业务逻辑判断什么时候使用拒绝，预防消息积压
        }
    }

    @Override
    public void onMessageBatch(List<Message> list, Channel channel) {
        for (Message message : list) {
            try {
                this.onMessage(message, channel);
            } catch (Exception e) {
                log.error("Msg ack error at :" + message.getMessageProperties().getDeliveryTag());
            }

        }
    }
}
