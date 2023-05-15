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
 * The Account class represents an account entity and is used for mapping data from and to the "accounts" table in the database.
 */
@Getter
@Setter
@Entity
@Table(name = "accounts")
public class Account {
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
    public Account(long id, long owner, BigDecimal balance) {
        this.id = id;
        this.owner = owner;
        this.balance = balance;
    }

    /**
     * An empty constructor for Account
     */
    public Account() {
    }

    /**
     * Concatenates all class variables into a readable string
     * @return Returns a readable string that includes all the Account class variables.
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