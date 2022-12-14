package school.cesar.sd.auditoria;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

    public void receiveMessage(String message) throws JsonProcessingException {

        System.out.println();
        System.out.println("Mensagem recebida: <" + message + ">" + " em formato JSON.");
        System.out.println("Unmarshalling...");

        ObjectMapper mapper = new ObjectMapper();
        Message msg = mapper.readValue(message, Message.class);
        System.out.println("Conteúdo da mensagem: Estação " + msg.getEstacao() + "| Timestamp " + msg.getTimestamp() + "| Evento " + msg.getEvento());
        System.out.println("-----------------------------------------------------");
        System.out.println();
    }
}
