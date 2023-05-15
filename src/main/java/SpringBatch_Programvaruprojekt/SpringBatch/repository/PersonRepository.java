package SpringBatch_Programvaruprojekt.SpringBatch.repository;

import SpringBatch_Programvaruprojekt.SpringBatch.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A Spring Data JPA Repository for Person that enables easier communication with the database
 */
public interface PersonRepository extends JpaRepository<Person, Long> {
}
