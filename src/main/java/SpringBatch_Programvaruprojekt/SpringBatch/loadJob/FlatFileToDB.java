package SpringBatch_Programvaruprojekt.SpringBatch.loadJob;


import SpringBatch_Programvaruprojekt.SpringBatch.listener.JobCompletionNotificationListener;
import SpringBatch_Programvaruprojekt.SpringBatch.model.Account;
import SpringBatch_Programvaruprojekt.SpringBatch.model.Person;
import SpringBatch_Programvaruprojekt.SpringBatch.model.Transaction;
import SpringBatch_Programvaruprojekt.SpringBatch.repository.AccountRepository;
import SpringBatch_Programvaruprojekt.SpringBatch.repository.PersonRepository;
import SpringBatch_Programvaruprojekt.SpringBatch.repository.TransactionRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;



/**
 * @author Çagri Çimen
 * @author Wilmer Hallin Jacobson
 * @author Tilda Engström
 *
 * A configuration class for managing batch operations related to loading data from flat file into database
 * Sets up Spring Batch infrastructure needed for jobs and steps for loading data.
 */
@Configuration
public class FlatFileToDB extends DefaultBatchConfiguration {

    /* File paths for flat files to read data from. */
    public static final String PERSONS_FILE_PATH = "persons_10k.csv";
    public static final String ACCOUNTS_FILE_PATH = "accounts_20k.csv";
    public static final String TRANSACTIONS_FILE_PATH = "transactions_100k.csv";

    /* The size of the data chunk that will be processed per batch. */
    @Value("100")
    private Integer chunkSize;

    /**
     * Sets up a job to process data from flat files.
     * The steps of the job are executed in the following order: personLoadStep -> accountLoadStep -> transactionLoadStep.
     *
     * @param jobRepository The repository that stores job details.
     * @param listener The listener to notify upon job completion.
     * @param personLoadStep The first step of the job, which loads Person data.
     * @param transactionLoadStep The second step of the job, which loads Transaction data.
     * @param accountLoadStep The final step of the job, which loads Account data.
     * @return A fully configured Job instance.
     */
    @Bean
    public Job loadJob(JobRepository jobRepository,
                       JobCompletionNotificationListener listener,
                       Step personLoadStep, Step transactionLoadStep, Step accountLoadStep) {
        return new JobBuilder("loadJob", jobRepository)
                .repository(jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(personLoadStep)
                .next(accountLoadStep)
                .next(transactionLoadStep)
                .end()
                .build();
    }

    /**
     * Sets up a step to load data into the Person table.
     * This step reads data from a flat file and writes the processed data to the Person table.
     * Implements chunk processing
     *
     * @param jobRepository The repository that stores job details.
     * @param transactionManager The transaction manager to handle data processing transactions.
     * @param personLoadWriter The writer to store the Person data.
     * @return A fully configured Step instance.
     */
    @Bean
    public Step personLoadStep(JobRepository jobRepository,
                           PlatformTransactionManager transactionManager,
                           RepositoryItemWriter<Person> personLoadWriter) {
        StepBuilder stepBuilder = new StepBuilder("personLoadStep", jobRepository);
        SimpleStepBuilder<Person, Person> simpleStepBuilder = stepBuilder
                .<Person, Person>chunk(chunkSize, transactionManager)
                .reader(personLoadReader())
                .writer(personLoadWriter);

        simpleStepBuilder.transactionManager(transactionManager);
        return simpleStepBuilder.build();
    }

    /**
     * Sets up a step to load data into the Account table.
     * This step reads data from a flat file and writes the processed data to the Account table.
     * Implements chunk processing
     *
     * @param jobRepository The repository that stores job details.
     * @param transactionManager The transaction manager to handle data processing transactions.
     * @param accountLoadWriter The writer to store the Account data.
     * @return A fully configured Step instance.
     */
    @Bean
    public Step accountLoadStep(JobRepository jobRepository,
                            PlatformTransactionManager transactionManager,
                            RepositoryItemWriter<Account> accountLoadWriter) {
        StepBuilder stepBuilder = new StepBuilder("accountLoadStep", jobRepository);
        SimpleStepBuilder<Account, Account> simpleStepBuilder = stepBuilder
                .<Account, Account>chunk(chunkSize, transactionManager)
                .reader(accountLoadReader())
                .writer(accountLoadWriter);

        simpleStepBuilder.transactionManager(transactionManager);
        return simpleStepBuilder.build();
    }

    /**
     * Sets up a step to load data into the Transaction table.
     * This step reads data from a flat file and writes the data to the Transaction table.
     * Implements chunk processing
     *
     * @param jobRepository The repository that stores job details.
     * @param transactionManager The transaction manager to handle data processing transactions.
     * @param transactionLoadWriter The writer to store the Transaction data.
     * @return A fully configured Step instance.
     */
    @Bean
    public Step transactionLoadStep(JobRepository jobRepository,
                                    PlatformTransactionManager transactionManager,
                                    RepositoryItemWriter<Transaction> transactionLoadWriter) {
        StepBuilder stepBuilder = new StepBuilder("transactionLoadStep", jobRepository);
        SimpleStepBuilder<Transaction, Transaction> simpleStepBuilder = stepBuilder
                .<Transaction, Transaction>chunk(chunkSize, transactionManager)
                .reader(transactionLoadReader())
                .writer(transactionLoadWriter);

        simpleStepBuilder.transactionManager(transactionManager);
        return simpleStepBuilder.build();
    }

    /**
     * Provides a reader for Person data from a flat file.
     * The returned reader is configured to parse lines in the file using a comma as a delimiter
     * and map the data to a Person object.
     *
     * @return A FlatFileItemReader configured to read Person data from a flat file.
     */
    @Bean
    public FlatFileItemReader<Person> personLoadReader() {
        FlatFileItemReader<Person> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource(PERSONS_FILE_PATH));
        reader.setLineMapper(new DefaultLineMapper<>() {{
            setLineTokenizer(new DelimitedLineTokenizer(",") {{
                setNames("id", "first_name", "last_name", "date_of_birth");
                setQuoteCharacter('\'');
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                setTargetType(Person.class);
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                setCustomEditors(Collections.singletonMap(LocalDate.class, new PropertyEditorSupport() {
                    @Override
                    public void setAsText(String text) throws IllegalArgumentException {
                        setValue(LocalDate.parse(text, dateFormatter));
                    }
                }));
            }});
        }});
        return reader;
    }

    /**
     * Provides a reader for Account data from a flat file.
     * The returned reader is configured to parse lines in the file using a comma as a delimiter
     * and map the data to an Account object.
     *
     * @return A FlatFileItemReader configured to read Account data from a flat file.
     */
    @Bean
    public FlatFileItemReader<Account> accountLoadReader() {
        FlatFileItemReader<Account> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource(ACCOUNTS_FILE_PATH));
        reader.setLineMapper(new DefaultLineMapper<>() {{
            setLineTokenizer(new DelimitedLineTokenizer(",") {{
                setNames("id", "owner", "balance");
                setQuoteCharacter('\'');
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                setTargetType(Account.class);
            }});
        }});
        return reader;
    }

    /**
     * Provides a reader for Transaction data from a flat file.
     * The returned reader is configured to parse lines in the file using a comma as a delimiter
     * and map the data to a Transaction object.
     *
     * @return A FlatFileItemReader configured to read Transaction data from a flat file.
     */
    @Bean
    public FlatFileItemReader<Transaction> transactionLoadReader() {
        FlatFileItemReader<Transaction> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource(TRANSACTIONS_FILE_PATH));
        reader.setLineMapper(new DefaultLineMapper<>() {{
            setLineTokenizer(new DelimitedLineTokenizer(",") {{
                setNames("id", "sender", "receiver", "date", "amount");
                setQuoteCharacter('\'');
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                setTargetType(Transaction.class);
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                setCustomEditors(Collections.singletonMap(LocalDate.class, new PropertyEditorSupport() {
                    @Override
                    public void setAsText(String text) throws IllegalArgumentException {
                        setValue(LocalDate.parse(text, dateFormatter));
                    }
                }));
            }});
        }});
        return reader;
    }

    /**
     * Provides a writer for Person data to a PersonRepository.
     * The returned writer is configured to use the save method of the provided PersonRepository.
     *
     * @param personRepository The repository to which Person data will be written.
     * @return A RepositoryItemWriter configured to write Person data to the provided PersonRepository.
     */
    @Bean
    public RepositoryItemWriter<Person> personLoadWriter(PersonRepository personRepository) {
        RepositoryItemWriter<Person> writer = new RepositoryItemWriter<>();
        writer.setRepository(personRepository);
        writer.setMethodName("save");
        return writer;
    }

    /**
     * Provides a writer for Account data to an AccountRepository.
     * The returned writer is configured to use the save method of the provided AccountRepository.
     *
     * @param accountRepository The repository to which Account data will be written.
     * @return A RepositoryItemWriter configured to write Account data to the provided AccountRepository.
     */
    @Bean
    public RepositoryItemWriter<Account> accountLoadWriter(AccountRepository accountRepository) {
        RepositoryItemWriter<Account> writer = new RepositoryItemWriter<>();
        writer.setRepository(accountRepository);
        writer.setMethodName("save");
        return writer;
    }

    /**
     * Provides a writer for Transaction data to a TransactionRepository.
     * The returned writer is configured to use the save method of the provided TransactionRepository.
     *
     * @param transactionRepository The repository to which Transaction data will be written.
     * @return A RepositoryItemWriter configured to write Transaction data to the provided TransactionRepository.
     */
    @Bean
    public RepositoryItemWriter<Transaction> transactionLoadWriter(TransactionRepository transactionRepository) {
        RepositoryItemWriter<Transaction> writer = new RepositoryItemWriter<>();
        writer.setRepository(transactionRepository);
        writer.setMethodName("save");
        return writer;
    }

}

