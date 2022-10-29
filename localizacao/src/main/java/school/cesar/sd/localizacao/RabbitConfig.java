package school.cesar.sd.localizacao;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class RabbitConfig {

    static final String directExchangeName = "direct-exchange";
    static final String directQueueName = "direct-queue";
    static final String routingKey = "1234";
    @Bean
    Queue queue() {

        return new Queue(directQueueName, false);
    }
    @Bean
    DirectExchange exchange() {

        return new DirectExchange(directExchangeName);
    }
    @Bean
    Binding binding(Queue queue, DirectExchange exchange) {

        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }
    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();

        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(directQueueName);
        container.setMessageListener(listenerAdapter);

        return container;

    }
    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {

        return new MessageListenerAdapter(receiver,"receiveMessage");
    }

}
