package kz.kassayev.test.controller;

import kz.kassayev.test.model.Order;
import kz.kassayev.test.model.Person;
import kz.kassayev.test.repository.BookRepository;
import kz.kassayev.test.repository.OrderRepository;
import kz.kassayev.test.repository.PersonRepository;
import kz.kassayev.test.service.MessageService;
import kz.kassayev.test.service.MessageServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Date;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    private final PersonRepository personRepository;
    private MessageService messageService;

    @Autowired
    public OrderController(OrderRepository orderRepository, BookRepository bookRepository, PersonRepository personRepository, MessageService messageService) {
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
        this.personRepository = personRepository;
        this.messageService = messageService;
    }

    @PostMapping
    public String create(@RequestBody @Valid Order order) {
        Optional<Person> optionalPerson = personRepository.findById(order.getPerson().getId());


        order.setPerson(optionalPerson.get());
        order.setIssuedAt(new Date());


        messageService.sendOrderMessage(order);

        return "\"redirect:/order";
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> update(@RequestBody @Valid Order order, @PathVariable Long id) {
        Optional<Person> optionalPerson = personRepository.findById(order.getPerson().getId());
        if (!optionalPerson.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (!optionalOrder.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        order.setPerson(optionalPerson.get());
        order.setId(optionalOrder.get().getId());
        orderRepository.save(order);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Order> delete(@PathVariable Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (!optionalOrder.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        orderRepository.delete(optionalOrder.get());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (!optionalOrder.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        return ResponseEntity.ok(optionalOrder.get());
    }

    @GetMapping
    public ResponseEntity<Page<Order>> getAll(Pageable pageable) {
        return ResponseEntity.ok(orderRepository.findAll(pageable));
    }

    @GetMapping("/person/{personId}")
    public ResponseEntity<Page<Order>> getByPersonId(@PathVariable Long personId, Pageable pageable) {
        return ResponseEntity.ok(orderRepository.findByPersonId(personId, pageable));
    }

}
