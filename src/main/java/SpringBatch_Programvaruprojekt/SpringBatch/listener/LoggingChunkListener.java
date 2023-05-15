package SpringBatch_Programvaruprojekt.SpringBatch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

@Component
public class LoggingChunkListener implements ChunkListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingChunkListener.class);

    @Override
    public void beforeChunk(ChunkContext context) {
        LOGGER.info("Starting chunk: {}", context.getStepContext().getStepName());
    }

    @Override
    public void afterChunk(ChunkContext context) {
        LOGGER.info("Finished chunk: {}", context.getStepContext().getStepName());
    }

}