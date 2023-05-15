package SpringBatch_Programvaruprojekt.SpringBatch.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
@Getter
@Setter
@Entity
@Table(name = "RemovedTransactions")
public class RemovedTransaction {
    @Id
    @Column(name = "id", nullable = false, unique = true, columnDefinition = "BIGINT")
    private long id;
    @Column(name = "sender", nullable = false, columnDefinition = "INT")
    private long sender;
    @Column(name = "receiver", nullable = false, columnDefinition = "INT")
    private long receiver;
    @Column(name = "date", nullable = false, columnDefinition = "DATE")
    private LocalDate date;
    @Column(name = "amount", nullable = false, columnDefinition = "DECIMAL(13,4)")
    private BigDecimal amount;

    public RemovedTransaction(long id, long sender, long receiver, LocalDate date, BigDecimal amount) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.date = date;
        this.amount = amount;
    }

    public RemovedTransaction() {
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", date=" + date +
                ", amount=" + amount +
                '}';
    }
}
