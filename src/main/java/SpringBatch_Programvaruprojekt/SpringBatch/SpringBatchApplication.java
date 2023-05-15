package SpringBatch_Programvaruprojekt.SpringBatch;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

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
	 * When the application starts, this method loads the data into the database
	 * @param args incoming main method arguments
	 * @throws Exception
	 */
	@Override
	public void run(String... args) throws Exception {
		JobExecution loadJobExecution = jobLauncher.run(loadJob, new JobParameters());
		System.out.println("Job Status: " + loadJobExecution.getStatus());


//To run filterJob directly after loadJob when application starts
//		JobExecution filterJobExecution = jobLauncher.run(filterJob, incrementer.getNext(new JobParameters()));
//		System.out.println("Job Status: " + filterJobExecution.getStatus());
	}

	/**
	 * Scheduled start of batch job "filterJob"
	 * @throws Exception
	 */
	@Scheduled
			//(cron = "0 04 11 * * *")  //Cron expressions: https://docs.oracle.com/cd/E12058_01/doc/doc.1014/e12030/cron_expressions.htm
			(fixedDelay = 1000000, initialDelay = 30000)
	public void runFilter() throws Exception {
		JobExecution filterJobExecution = jobLauncher.run(filterJob, new JobParameters()); // incrementer.getNext(new JobParameters()));
		System.out.println("Job Status: " + filterJobExecution.getStatus());
	}

	JobParametersIncrementer incrementer = parameters -> new JobParametersBuilder(parameters)
			.addString("time.id", LocalDateTime.now().toString())
			.toJobParameters();

}
