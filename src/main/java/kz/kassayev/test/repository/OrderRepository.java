package kz.kassayev.test.repository;

import kz.kassayev.test.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByPersonId(Long personId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM Order o WHERE o.person.id = ?1")
    void deleteByPersonId(Long personId);
}
