package school.cesar.sd.produtor;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    static final String fanoutExchangeName = "fanout-exchange";
    static final String queueName = "school-queue";

    @Bean
    Queue queue() {

        return new Queue(queueName, false);
    }

    @Bean
    FanoutExchange exchange(){

        return new FanoutExchange(fanoutExchangeName);
    }

    @Bean
    Binding binding(Queue queue, FanoutExchange exchange) {

        return BindingBuilder.bind(queue).to(exchange);
    }


}