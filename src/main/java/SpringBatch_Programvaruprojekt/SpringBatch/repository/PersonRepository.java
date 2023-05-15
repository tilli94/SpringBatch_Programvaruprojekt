package SpringBatch_Programvaruprojekt.SpringBatch.repository;

import SpringBatch_Programvaruprojekt.SpringBatch.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
