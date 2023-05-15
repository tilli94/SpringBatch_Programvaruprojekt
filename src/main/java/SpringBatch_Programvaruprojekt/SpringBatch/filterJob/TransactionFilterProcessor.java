package SpringBatch_Programvaruprojekt.SpringBatch.filterJob;

import SpringBatch_Programvaruprojekt.SpringBatch.model.RemovedTransaction;
import SpringBatch_Programvaruprojekt.SpringBatch.model.Transaction;
import SpringBatch_Programvaruprojekt.SpringBatch.repository.TransactionRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
/**
 * @author Çagri Çimen
 * @author Wilmer Hallin Jacobson
 * @author Tilda Engström
 *
 * This class represents the processor in a Spring Batch Job which processes Transaction objects and converts them to RemovedTransaction objects.
 * It handles the business logic to decide which Transaction should be removed based on the transaction date criteria.
 * If a Transaction is older than 18 months, it is removed and saved as a RemovedTransaction.
 */
public class TransactionFilterProcessor implements ItemProcessor<Transaction, RemovedTransaction> {

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * The method to process each Transaction item. If the Transaction is older than 18 months, it is processed and removed.
     *
     * @param transaction The Transaction object to be processed.
     * @return The RemovedTransaction object if the Transaction was processed and removed, otherwise null.
     */
    @Override
    public RemovedTransaction process(final Transaction transaction) {

        LocalDate currentDate = LocalDate.now();
        LocalDate transactionDate = transaction.getDate();
        long months = ChronoUnit.MONTHS.between(transactionDate, currentDate);

          if ((months <= 18)
        ) {
            return null;
        }
        else {
              RemovedTransaction removed = new RemovedTransaction(transaction.getId(), transaction.getSender(), transaction.getReceiver(), transaction.getDate(), transaction.getAmount());
              transactionRepository.delete(transaction);

              return removed;
        }

    }
}

