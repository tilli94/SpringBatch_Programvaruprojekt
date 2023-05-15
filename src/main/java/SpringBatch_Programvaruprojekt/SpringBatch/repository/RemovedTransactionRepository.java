package SpringBatch_Programvaruprojekt.SpringBatch.repository;

import SpringBatch_Programvaruprojekt.SpringBatch.model.RemovedTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RemovedTransactionRepository extends JpaRepository<RemovedTransaction, Long> {
}
