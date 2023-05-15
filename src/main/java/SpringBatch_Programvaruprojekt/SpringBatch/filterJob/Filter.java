package SpringBatch_Programvaruprojekt.SpringBatch.filterJob;


import SpringBatch_Programvaruprojekt.SpringBatch.listener.JobCompletionNotificationListener;
import SpringBatch_Programvaruprojekt.SpringBatch.listener.LoggingChunkListener;
import SpringBatch_Programvaruprojekt.SpringBatch.model.Person;
import SpringBatch_Programvaruprojekt.SpringBatch.model.RemovedPerson;
import SpringBatch_Programvaruprojekt.SpringBatch.model.RemovedTransaction;
import SpringBatch_Programvaruprojekt.SpringBatch.model.Transaction;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author Çagri Çimen
 * @author Wilmer Hallin Jacobson
 * @author Tilda Engström
 *
 * A configuration class for managing batch operations related to data filtering in the database.
 * Sets up Spring Batch infrastructure needed for jobs and steps for filtering operations.
 */
@Configuration
@EnableScheduling
public class Filter extends DefaultBatchConfiguration {

    /* The size of the data chunk that will be processed per batch. */
    @Value("100")
    private Integer chunkSize;

    /* The EntityManagerFactory used for JPA operations. */
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    /**
     * Defines the job that will handle the filtering of data entities.
     *
     * @param jobRepository Repository for Job instances.
     * @param listener Listener for job completion events.
     * @param personFilterStep Step for filtering Person entities.
     * @param transactionFilterStep Step for filtering Transaction entities.
     *
     * @return The Job instance.
     */
    @Bean
    public Job filterJob(JobRepository jobRepository,
                         JobCompletionNotificationListener listener,
                         Step personFilterStep,
                         Step transactionFilterStep) {
        return new JobBuilder("filterJob", jobRepository)
                //.preventRestart() //if used: this Job does not support being started again. Restarting a Job that is not restartable causes a JobRestartException to be thrown.
                .repository(jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(transactionFilterStep)
                .next(personFilterStep)
                .end()
                .build();
    }

    /**
     * Defines the step that will handle the filtering of Person entities.
     * Implements chunk processing
     *
     * @param jobRepository Repository for Job instances.
     * @param transactionManager The transaction manager for the step.
     * @param removedPersonWriter Writer for removed Person entities.
     * @param chunkListener Listener for chunk processing events.
     *
     * @return The Step instance.
     */

    @Bean
    public Step personFilterStep(JobRepository jobRepository,
                                 PlatformTransactionManager transactionManager,
                                 ItemWriter<RemovedPerson> removedPersonWriter,
                                 LoggingChunkListener chunkListener) {
        StepBuilder stepBuilder = new StepBuilder("personFilterStep", jobRepository);
        SimpleStepBuilder<Person, RemovedPerson> simpleStepBuilder = stepBuilder
                .<Person, RemovedPerson>chunk(chunkSize, transactionManager)
                .reader(personReaderFromDatabase())
                .processor(personFilterProcessor())
                .writer(removedPersonWriter)
                //.listener(chunkListener)
                ;
        simpleStepBuilder.transactionManager(transactionManager);
        return simpleStepBuilder.build();
    }

    /**
     * Defines the step that will handle the filtering of Transaction entities.
     * Implements chunk processing
     *
     * @param jobRepository Repository for Job instances.
     * @param transactionManager The transaction manager for the step.
     * @param removedTransactionWriter Writer for removed Transaction entities.
     * @param chunkListener Listener for chunk processing events.
     *
     * @return The Step instance.
     */
    @Bean
    protected Step transactionFilterStep(JobRepository jobRepository,
                                         PlatformTransactionManager transactionManager,
                                         ItemWriter<RemovedTransaction> removedTransactionWriter,
                                         LoggingChunkListener chunkListener) {
        StepBuilder stepBuilder = new StepBuilder("transactionFilterStep", jobRepository);
        SimpleStepBuilder<Transaction, RemovedTransaction> simpleStepBuilder = stepBuilder
                .<Transaction, RemovedTransaction>chunk(chunkSize, transactionManager)
                .reader(transactionReaderFromDatabase())
                .processor(transactionFilterProcessor())
                .writer(removedTransactionWriter)
                //.listener(chunkListener)
                ;
        simpleStepBuilder.transactionManager(transactionManager);
        return simpleStepBuilder.build();
    }

    /**
     * Creates a JpaCursorItemReader for reading Person entities from the database.
     * @return The JpaCursorItemReader instance.
     */
    @Bean
    public JpaCursorItemReader<Person> personReaderFromDatabase() {
        return new JpaCursorItemReaderBuilder<Person>()
                .name("personReaderFromDatabase")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT p FROM Person p ORDER BY p.id ASC")
                .build();
    }

    /**
     * Creates a JpaCursorItemReader for reading Transaction entities from the database.
     * @return The JpaCursorItemReader instance.
     */
    @Bean
    public JpaCursorItemReader<Transaction> transactionReaderFromDatabase() {
        return new JpaCursorItemReaderBuilder<Transaction>()
                .name("transactionReaderFromDatabase")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT t FROM Transaction t ORDER BY t.id ASC")
                .build();
    }

    /**
     * Create a PersonFilterProcessor for processing Person entities.
     * Filtering data
     * @return The PersonFilterProcessor instance.
     */
    @Bean
    public PersonFilterProcessor personFilterProcessor() {
        return new PersonFilterProcessor();
    }

    /**
     * Create a TransactionFilterProcessor for processing Transaction entities.
     * @return The TransactionFilterProcessor instance.
     */
    @Bean
    public TransactionFilterProcessor transactionFilterProcessor() {
        return new TransactionFilterProcessor();
    }


    /**
     * Create a JpaItemWriter for writing removed Person entities to the database.
     *
     * @param entityManagerFactory The EntityManagerFactory used for JPA operations.
     * @return The JpaItemWriter instance.
     */
    @Bean
    public JpaItemWriter<RemovedPerson> removedPersonWriter(EntityManagerFactory entityManagerFactory) {
        JpaItemWriter<RemovedPerson> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    /**
     * Create a JpaItemWriter for writing removed Transaction entities to the database.
     *
     * @return The JpaItemWriter instance.
     * @param entityManagerFactory The EntityManagerFactory used for JPA operations.
     */
    @Bean
    public JpaItemWriter<RemovedTransaction> removedTransactionWriter(EntityManagerFactory entityManagerFactory) {
        JpaItemWriter<RemovedTransaction> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

}
