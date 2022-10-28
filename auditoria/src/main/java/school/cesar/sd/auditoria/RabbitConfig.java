package school.cesar.sd.auditoria;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
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
    FanoutExchange exchange() {

        return new FanoutExchange(fanoutExchangeName);
    }

    @Bean
    Binding binding(Queue queue, FanoutExchange exchange) {

        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();

        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);

        return container;

    }

    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {

        return new MessageListenerAdapter(receiver,"receiveMessage");
    }
}
