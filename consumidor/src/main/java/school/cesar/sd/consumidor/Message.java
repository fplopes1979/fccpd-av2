package school.cesar.sd.consumidor;

public class Message {

    private String estacao;

    private long timestamp;
    private String evento;


    public Message() {
    }

    public String getEstacao() {
        return estacao;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getEvento() {
        return evento;
    }
}