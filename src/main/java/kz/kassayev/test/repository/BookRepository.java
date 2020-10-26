package kz.kassayev.test.repository;

import kz.kassayev.test.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
