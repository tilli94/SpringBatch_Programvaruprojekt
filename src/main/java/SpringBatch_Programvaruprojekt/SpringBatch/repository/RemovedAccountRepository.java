package SpringBatch_Programvaruprojekt.SpringBatch.repository;

import SpringBatch_Programvaruprojekt.SpringBatch.model.RemovedAccount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A Spring Data JPA Repository for RemovedAccount that enables easier communication with the database
 */
public interface RemovedAccountRepository extends JpaRepository<RemovedAccount, Long> {
}
