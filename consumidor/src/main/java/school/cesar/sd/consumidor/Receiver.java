package school.cesar.sd.consumidor;

import org.springframework.stereotype.Component;

@Component
public class Receiver {

    public String receiveMessage(String message) {

        System.out.println("Mensagem recebida: " + message);
        return "Resposta do consumidor: " + message;
    }
}
