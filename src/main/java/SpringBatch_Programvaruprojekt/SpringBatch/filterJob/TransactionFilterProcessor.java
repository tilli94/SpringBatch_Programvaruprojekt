package SpringBatch_Programvaruprojekt.SpringBatch.filterJob;

import SpringBatch_Programvaruprojekt.SpringBatch.model.RemovedTransaction;
import SpringBatch_Programvaruprojekt.SpringBatch.model.Transaction;
import SpringBatch_Programvaruprojekt.SpringBatch.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class TransactionFilterProcessor implements ItemProcessor<Transaction, RemovedTransaction> {

    @Autowired
    private TransactionRepository transactionRepository;

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
              //Deletes from DB, writes to removed items DB
              RemovedTransaction removed = new RemovedTransaction(transaction.getId(), transaction.getSender(), transaction.getReceiver(), transaction.getDate(), transaction.getAmount());
              transactionRepository.delete(transaction);

              return removed;
        }

    }
}

