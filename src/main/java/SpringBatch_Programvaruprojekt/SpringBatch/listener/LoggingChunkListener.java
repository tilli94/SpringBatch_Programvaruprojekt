package SpringBatch_Programvaruprojekt.SpringBatch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

/**
 * @author Çagri Çimen
 * @author Wilmer Hallin Jacobson
 * @author Tilda Engström
 *
 * This class serves as a listener for chunk processing in a Spring Batch context.
 * It logs the beginning and the completion of a chunk processing.
 */
@Component
public class LoggingChunkListener implements ChunkListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingChunkListener.class);

    /**
     * This method is invoked before a chunk processing.
     * It logs the name of the step associated with the chunk that's about to be processed.
     *
     * @param context The context of a chunk.
     */
    @Override
    public void beforeChunk(ChunkContext context) {
        LOGGER.info("Starting chunk: {}", context.getStepContext().getStepName());
    }

    /**
     * This method is invoked after a chunk processing.
     * It logs the name of the step associated with the chunk that has just been processed.
     *
     * @param context The context of a chunk.
     */
    @Override
    public void afterChunk(ChunkContext context) {
        LOGGER.info("Finished chunk: {}", context.getStepContext().getStepName());
    }

}