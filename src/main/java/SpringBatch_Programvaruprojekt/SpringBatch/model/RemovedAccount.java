package SpringBatch_Programvaruprojekt.SpringBatch.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author Çagri Çimen
 * @author Wilmer Hallin Jacobson
 * @author Tilda Engström
 *
 * The RemovedAccount class represents a removed account entity and is used for mapping data from and to the "removed_accounts" table in the database.
 */
@Getter
@Setter
@Entity
@Table(name = "removed_accounts")
public class RemovedAccount {
    @Id
    @Column(name = "id", unique = true, columnDefinition = "BIGINT", updatable = false)
    private long id;
    @Column(name = "owner", nullable = false, columnDefinition = "BIGINT")
    private long owner;
    @Column(name = "balance", nullable = false, columnDefinition = "DECIMAL(13,4)")
    private BigDecimal balance;

    /**
     * Constructor for Account
     * @param id the id of the account
     * @param owner the id of the person that owns the account
     * @param balance the sum of money that the account currently holds
     */
    public RemovedAccount(long id, long owner, BigDecimal balance) {
        this.id = id;
        this.owner = owner;
        this.balance = balance;
    }
    /**
     * An empty constructor for RemovedAccount
     */
    public RemovedAccount() {
    }

    /**
     * Concatenates all class variables into a readable string
     * @return Returns a readable string that includes all the RemovedAccount class variables.
     */
    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", owner=" + owner +
                ", balance=" + balance +
                '}';
    }
}