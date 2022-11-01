package school.cesar.sd.produtor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Scanner;

@Component
public class Produtor implements CommandLineRunner {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void run(String... args) throws Exception {

        Scanner sc = new Scanner(System.in);

        String escolha = "S";
        String estacao;
        String evento;
        String json;
        String routingKey = "1234";

        ObjectMapper mapper = new ObjectMapper();

        while(true) {

            System.out.print("Informe a estação de origem: ");
            estacao = sc.nextLine();

            System.out.print("Evento: ");
            evento = sc.nextLine();

            long timestamp = System.currentTimeMillis();

            Message msg = new Message(estacao, timestamp, evento);
            json = mapper.writeValueAsString(msg);
            rabbitTemplate.convertAndSend("fanout-exchange", "", json);

            if (Objects.equals(evento, "power-on") || Objects.equals(evento, "power-off")) {

                rabbitTemplate.convertAndSend("direct-exchange", routingKey, json);
            }

            System.out.println();
            System.out.println("---------------------------------------");
            System.out.println("-- Mensagem enviada --");
            System.out.println("---------------------------------------");
            System.out.println();

        }
    }
}
