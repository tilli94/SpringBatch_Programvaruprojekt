package SpringBatch_Programvaruprojekt.SpringBatch;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

/**
 * @author Çagri Çimen
 * @author Wilmer Hallin Jacobson
 * @author Tilda Engström
 *
 * The main class of the Spring Batch application.
 * This class is responsible for launching the application and the batch jobs.
 *
 * It implements the CommandLineRunner interface, allowing the execution of batch jobs
 * and other tasks when the application starts.
 */
@SpringBootApplication
@EnableScheduling
public class SpringBatchApplication implements CommandLineRunner {

	private final JobLauncher jobLauncher;
	private final Job filterJob;
	private final Job loadJob;

	public SpringBatchApplication(JobLauncher jobLauncher, Job filterJob, Job loadJob) {
		this.jobLauncher = jobLauncher;
		this.filterJob = filterJob;
		this.loadJob = loadJob;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchApplication.class, args);
	}

	/**
	 This method is called when the application starts and is responsible for loading data into the database.
	 @param args the command-line arguments passed to the application's main method
	 @throws Exception if there is an error during the execution of the job
	 */
	@Override
	public void run(String... args) throws Exception {

		JobExecution loadJobExecution = jobLauncher.run(loadJob, new JobParameters());
		System.out.println("Job Status: " + loadJobExecution.getStatus());

		//To run filterJob directly after loadJob when application starts
		JobExecution filterJobExecution = jobLauncher.run(filterJob, incrementer.getNext(new JobParameters()));
		System.out.println("Job Status: " + filterJobExecution.getStatus());
	}

	/**
	 Scheduled method for starting the batch job "filterJob".
	 This method is triggered based on the specified scheduling configuration.
	 	(fixedDelay = 1000000, initialDelay = 30000) runs Job after 30 seconds, runs again 1000seconds after completed execution
	 	(cron = "0 04 11 * * *") sets start time with cron expressions
	 @throws Exception if there is an error during the execution of the job
	 */
	/*@Scheduled (fixedDelay = 1000000, initialDelay = 30000)
			 //(cron = "0 04 11 * * *")  //Cron expressions: https://docs.oracle.com/cd/E12058_01/doc/doc.1014/e12030/cron_expressions.htm
	public void runFilter() throws Exception {
		JobExecution filterJobExecution = jobLauncher.run(filterJob, new JobParameters()); // incrementer.getNext(new JobParameters()));
		System.out.println("Job Status: " + filterJobExecution.getStatus());
	}
	 */

	/**
	 Custom implementation of JobParametersIncrementer that generates unique job parameters
	 by adding the current timestamp as a string parameter with the key "time.id".
	 This ensures that each execution of the job receives different job parameters,
	 enabling distinct identification and tracking of job instances.
	 @param parameters the base JobParameters to build upon
	 @return the new JobParameters instance with the added timestamp parameter
	 */
	public JobParametersIncrementer incrementer = parameters -> new JobParametersBuilder(parameters)
			.addString("time.id", LocalDateTime.now().toString())
			.toJobParameters();
}
