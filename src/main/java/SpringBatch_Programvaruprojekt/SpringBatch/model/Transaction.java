package SpringBatch_Programvaruprojekt.SpringBatch.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Çagri Çimen
 * @author Wilmer Hallin Jacobson
 * @author Tilda Engström
 *
 * The Transaction class represents a transaction entity and is used for mapping data from and to the "transactions" table in the database.
 */
@Getter
@Setter
@Entity
@Table(name = "transactions")
public class Transaction {

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

    /**
     * Constructor for Transaction
     * @param id transaction id
     * @param sender the sender of money in the performed transaction
     * @param receiver the receiver of money in the performed transaction
     * @param date the date the transaction took place
     * @param amount the amount of money that was moved during the transaction
     */
    public Transaction(long id, long sender, long receiver, LocalDate date, BigDecimal amount) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.date = date;
        this.amount = amount;
    }

    /**
     * An empty constructor for Transaction
     */
    public Transaction() {
    }

    /**
     * Concatenates all class variables into a readable string
     * @return Returns a readable string that includes all the Transactions class variables.
     */
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