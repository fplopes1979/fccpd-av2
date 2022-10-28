package school.cesar.sd.auditoria;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

    public void receiveMessage(String message) throws JsonProcessingException {

        System.out.println("Received <" + message + ">");
        System.out.println("Converting from JSON to Java Object...");

        ObjectMapper mapper = new ObjectMapper();
        Message msg = mapper.readValue(message, Message.class);
        System.out.println(msg.getEstacao());
    }
}
