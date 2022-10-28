package school.cesar.sd.produtor;

public class Message {

    private final String estacao;
    private final long timestamp;
    private final String evento;

    public Message(String estacao, long timestamp, String evento) {
        this.estacao = estacao;
        this.timestamp = timestamp;
        this.evento = evento;

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