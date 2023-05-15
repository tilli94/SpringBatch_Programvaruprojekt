package SpringBatch_Programvaruprojekt.SpringBatch.filterJob;

import SpringBatch_Programvaruprojekt.SpringBatch.model.*;
import SpringBatch_Programvaruprojekt.SpringBatch.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;


public class PersonFilterProcessor implements ItemProcessor<Person, RemovedPerson>{

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private RemovedAccountRepository removedAccountRepository;
    @Autowired
    private RemovedTransactionRepository removedTransactionRepository;


    public RemovedPerson process(final Person person) {

        LocalDate currentDate = LocalDate.now();
        LocalDate dateOfBirth = person.getDateOfBirth();
        long years = ChronoUnit.YEARS.between(dateOfBirth, currentDate);

        if (years >= 18) {
            return null;
        }
        else {
            List<Account> accountsToRemove = accountRepository.findByOwnerIs(person.getId());
            List<Long> accountIdsToRemove = accountsToRemove.stream().map(Account::getId).collect(Collectors.toList());

            List<Transaction> transactionsToRemoveByReceiver = transactionRepository.findByReceiverIn(accountIdsToRemove);
            for(Transaction transaction : transactionsToRemoveByReceiver){
                RemovedTransaction removedTransaction = new RemovedTransaction(transaction.getId(), transaction.getSender(), transaction.getReceiver(), transaction.getDate(), transaction.getAmount());
                removedTransactionRepository.save(removedTransaction);
                transactionRepository.delete(transaction);
            }
            List<Transaction> transactionsToRemoveBySender = transactionRepository.findBySenderIn(accountIdsToRemove);
            for(Transaction transaction : transactionsToRemoveBySender){
                RemovedTransaction removedTransaction = new RemovedTransaction(transaction.getId(), transaction.getSender(), transaction.getReceiver(), transaction.getDate(), transaction.getAmount());
                removedTransactionRepository.save(removedTransaction);
                transactionRepository.delete(transaction);
            }
            for(Account account : accountsToRemove){
                RemovedAccount removedAccount = new RemovedAccount(account.getId(), account.getOwner(), account.getBalance());
                removedAccountRepository.save(removedAccount);
                accountRepository.delete(account);
            }

            RemovedPerson personToRemove = new RemovedPerson(person.getId(), person.getFirstName(), person.getLastName(), person.getDateOfBirth());
            personRepository.delete(person);

            return personToRemove;
        }
    }
}



