package school.cesar.sd.produtor;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    static final String fanoutExchangeName = "fanout-exchange";
    static final String directExchangeName = "direct-exchange";
    static final String queueName = "school-queue";
    static final String directQueueName = "direct-queue";

    @Bean
    Queue queue() {

        return new Queue(queueName, false);
    }

    @Bean
    Queue directQueue() {

        return new Queue(directQueueName, false);
    }

    @Bean
    DirectExchange directExchange() {

        return new DirectExchange(directExchangeName);
    }

    @Bean
    FanoutExchange exchange(){

        return new FanoutExchange(fanoutExchangeName);
    }

    @Bean
    Binding binding(Queue queue, FanoutExchange exchange) {

        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean
    Binding directBinding(Queue directQueue, DirectExchange directExchange) {

        return BindingBuilder.bind(directQueue).to(directExchange).withQueueName();
    }


}