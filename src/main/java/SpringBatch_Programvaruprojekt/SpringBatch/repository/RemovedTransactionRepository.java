package SpringBatch_Programvaruprojekt.SpringBatch.repository;

import SpringBatch_Programvaruprojekt.SpringBatch.model.RemovedTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A Spring Data JPA Repository for RemovedTransaction that enables easier communication with the database
 */
public interface RemovedTransactionRepository extends JpaRepository<RemovedTransaction, Long> {
}
