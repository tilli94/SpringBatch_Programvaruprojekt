package SpringBatch_Programvaruprojekt.SpringBatch.repository;

import SpringBatch_Programvaruprojekt.SpringBatch.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * A Spring Data JPA Repository for Account that enables easier communication with the database
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByOwnerIs (long id);

}
