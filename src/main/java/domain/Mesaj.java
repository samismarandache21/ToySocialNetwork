package domain;

import java.time.LocalDateTime;
import java.util.List;

public class Mesaj extends Entity<Long> {
    private String mesaj;                // Textul mesajului
    private Long from;                   // ID-ul utilizatorului care a trimis mesajul
    private List<Long> to;               // Lista ID-urilor utilizatorilor destinatari
    private LocalDateTime data;          // Data și ora trimiterii mesajului
    private Long replyTo;
    // Constructor
    public Mesaj(String mesaj, Long from, List<Long> to, LocalDateTime data, Long replyTo) {
        this.mesaj = mesaj;
        this.from = from;
        this.to = to;
        this.data = data;
        this.replyTo = replyTo;
    }

    // Getteri și setteri
    public String getMesaj() {
        return mesaj;
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public List<Long> getTo() {
        return to;
    }

    public void setTo(List<Long> to) {
        this.to = to;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }
    public Long getReplyTo() {
        return replyTo;
    }
    public void setReplyTo(Long replyTo) {
        this.replyTo = replyTo;
    }

    @Override
    public String toString() {
        return "Mesaj{" +
                "mesaj='" + mesaj + '\'' +
                ", from=" + from +
                ", to=" + to +
                ", data=" + data +
                ", replyTo=" + replyTo +
                '}';
    }
}
