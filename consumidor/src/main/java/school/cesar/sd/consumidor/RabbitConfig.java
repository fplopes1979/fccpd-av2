package school.cesar.sd.consumidor;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    static String RandGeneratedStr() {

        // a list of characters to choose from in form of a string

        String AlphaNumericStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz0123456789";

        // creating a StringBuffer size of AlphaNumericStr

        StringBuilder s = new StringBuilder(10);

        int i;

        for (i = 0; i < 10; i++) {

            //generating a random number using math.random()

            int ch = (int) (AlphaNumericStr.length() * Math.random());

            //adding Random character one by one at the end of s

            s.append(AlphaNumericStr.charAt(ch));

        }

        return s.toString();
    }

    static final String queueName = RandGeneratedStr();
    static final String topicExchangeName = "topic-exchange";

    @Bean
    Queue queue() {

        return new Queue(queueName, false, true, true);
    }

    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding tempBinding(Queue tempQueue, TopicExchange topicExchange) {

        return BindingBuilder.bind(tempQueue).to(topicExchange).with("#");
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
