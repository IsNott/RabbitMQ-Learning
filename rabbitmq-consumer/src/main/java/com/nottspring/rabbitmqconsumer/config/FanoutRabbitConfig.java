package com.nottspring.rabbitmqconsumer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Nott
 * @Date 2022/7/14
 */
//消费者不需要也配置，配置后也可当做生产者
@Configuration
public class FanoutRabbitConfig {

    public static final String FANOUT = "MyFanoutExchange";

    /**
     * 创建三个队列 ：fanout.A   fanout.B  fanout.C
     * 将三个队列都绑定在交换机 fanoutExchange 上
     * 因为是扇型交换机, 路由键无需配置,配置也不起作用
     */
    @Bean
    public Queue fanOutA() {
        return new Queue("fanout.A");
    }

    @Bean
    public Queue fanOutB() {
        return new Queue("fanout.B");
    }

    @Bean
    public Queue fanOutC() {
        return new Queue("fanout.C");
    }

    //创建交换机
    @Bean
    FanoutExchange MyFanoutExchange() {
        return new FanoutExchange(FANOUT);
    }

    //订阅
    @Bean
    Binding bindingA() {
        return BindingBuilder.bind(fanOutA()).to(MyFanoutExchange()); //扇形交换机不需要路由键，直接订阅
    }

    @Bean
    Binding bindingB() {
        return BindingBuilder.bind(fanOutB()).to(MyFanoutExchange()); //扇形交换机不需要路由键，直接订阅
    }

    @Bean
    Binding bindingC() {
        return BindingBuilder.bind(fanOutC()).to(MyFanoutExchange()); //扇形交换机不需要路由键，直接订阅
    }


}
