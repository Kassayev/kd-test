package kz.kassayev.test.repository;

import kz.kassayev.test.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
