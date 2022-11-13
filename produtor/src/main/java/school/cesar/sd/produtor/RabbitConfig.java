package school.cesar.sd.produtor;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    static final String allQueueName = "all-queue";
    static final String localQueueName = "local-queue";
    static final String topicExchangeName = "topic-exchange";

    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Queue allQueue() {

        return new Queue(allQueueName, false);
    }

    @Bean
    Binding allBinding(Queue allQueue, TopicExchange exchange) {

        return BindingBuilder.bind(allQueue).to(exchange).with("#");
    }

    @Bean
    Queue localQueue() {
        return new Queue(localQueueName, false);
    }

    @Bean
    Binding localBinding(Queue localQueue, TopicExchange exchange) {

        return BindingBuilder.bind(localQueue).to(exchange).with("local.*");
    }

    /*@Bean
    Queue directQueue() {

        return new Queue(directQueueName, false);
    }

    @Bean
    Queue requestResponseQueue() {

        return new Queue(requestResponseQueueName, false);
    }

    @Bean
    DirectExchange directExchange() {

        return new DirectExchange(directExchangeName);
    }

    @Bean
    FanoutExchange exchange(){

        return new FanoutExchange(fanoutExchangeName);
    }*/

}