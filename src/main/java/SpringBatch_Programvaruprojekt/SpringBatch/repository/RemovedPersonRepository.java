package SpringBatch_Programvaruprojekt.SpringBatch.repository;

import SpringBatch_Programvaruprojekt.SpringBatch.model.RemovedPerson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RemovedPersonRepository extends JpaRepository<RemovedPerson, Long> {
}
