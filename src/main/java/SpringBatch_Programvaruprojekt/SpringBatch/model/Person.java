package SpringBatch_Programvaruprojekt.SpringBatch.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author Çagri Çimen
 * @author Wilmer Hallin Jacobson
 * @author Tilda Engström
 *
 * The Person class represents a person entity and is used for mapping data from and to the "persons" table in the database.
 */
@Getter
@Setter
@Entity
@Table(name = "persons")
public class Person {

    @Id
    @Column(name = "id", unique = true, columnDefinition = "BIGINT", updatable = false)
    private long id;
    @Column(name = "first_name", nullable = false, columnDefinition = "TEXT")
    private String firstName;
    @Column(name = "last_name", nullable = false, columnDefinition = "TEXT")
    private String lastName;
    @Column(name = "date_of_birth", nullable = false, columnDefinition = "DATE")
    private LocalDate dateOfBirth;

    /**
     * Constructor for Person
     * @param id an id for each person
     * @param firstName the person's first name
     * @param lastName the person's last name
     * @param dateOfBirth the date the person was born
     */
    public Person(long id, String firstName, String lastName, LocalDate dateOfBirth) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * An empty constructor for Person
     */
    public Person() {
    }

    /**
     * Concatenates all class variables into a readable string
     * @return Returns a readable string that includes all the Person class variables.
     */
    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", DOB=" + dateOfBirth +
                '}';
    }
}

