package domain;

import java.time.LocalDateTime;

public class Friendship extends Entity <Tuple<Long, Long>> {
    private LocalDateTime dataPrietenie;
    private int status;

    public Friendship(LocalDateTime dataPrietenie, int sta){
        this.dataPrietenie = dataPrietenie; this.status = sta;
    }

    public LocalDateTime getDataPrietenie() {
        return dataPrietenie;
    }

    public void setDataPrietenie(LocalDateTime dataPrietenie) {
        this.dataPrietenie = dataPrietenie;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "prieteni= " + getId() +
                "dataPrietenie=" + dataPrietenie +
                ", status=" + status +
                '}';
    }
}
