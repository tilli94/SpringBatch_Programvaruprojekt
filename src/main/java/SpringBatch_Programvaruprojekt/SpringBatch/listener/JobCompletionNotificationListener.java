package SpringBatch_Programvaruprojekt.SpringBatch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

/**
 * @author Çagri Çimen
 * @author Wilmer Hallin Jacobson
 * @author Tilda Engström
 *
 * This class serves as a listener for the completion of a job in a Spring Batch context.
 * It logs the status of the job after its execution - whether it has completed successfully or failed.
 */
@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    /**
     * This method is invoked after a job execution.
     * It checks the status of the job and logs the appropriate message.
     *
     * @param jobExecution The execution of a job.
     */
    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB  {} FINISHED !!!", jobExecution.getJobId());
        }
        else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            log.info("!!! JOB  {} FAILED !!!", jobExecution.getJobId());
        }
    }
}
