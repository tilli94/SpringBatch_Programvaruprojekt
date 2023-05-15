package SpringBatch_Programvaruprojekt.SpringBatch.repository;

import SpringBatch_Programvaruprojekt.SpringBatch.model.RemovedPerson;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A Spring Data JPA Repository for RemovedPerson that enables easier communication with the database
 */
public interface RemovedPersonRepository extends JpaRepository<RemovedPerson, Long> {
}
