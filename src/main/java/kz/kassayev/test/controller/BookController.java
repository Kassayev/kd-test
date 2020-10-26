package kz.kassayev.test.controller;

import kz.kassayev.test.model.Book;
import kz.kassayev.test.model.Order;
import kz.kassayev.test.repository.BookRepository;
import kz.kassayev.test.repository.OrderRepository;
import kz.kassayev.test.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;
    private MessageService messageService;

    @Autowired
    public BookController(BookRepository bookRepository, OrderRepository orderRepository, MessageService messageService) {
        this.bookRepository = bookRepository;
        this.orderRepository = orderRepository;
        this.messageService = messageService;

    }

    @PostMapping
    public ResponseEntity<Book> create(@RequestBody @Valid Book book) {
        Optional<Order> optionalOrder = orderRepository.findById(book.getOrder().getId());
        if (!optionalOrder.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        book.setOrder(optionalOrder.get());
        Book savedBook = bookRepository.save(book);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedBook.getId()).toUri();
        return ResponseEntity.created(location).body(savedBook);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> update(@RequestBody @Valid Book book, @PathVariable Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(book.getOrder().getId());
        if (!optionalOrder.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        Optional<Book> optionalBook = bookRepository.findById(id);
        if (!optionalBook.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        book.setOrder(optionalOrder.get());
        book.setId(optionalBook.get().getId());
        bookRepository.save(book);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Book> delete(@PathVariable Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (!optionalBook.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        bookRepository.delete(optionalBook.get());

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<Book>> getAll(Pageable pageable) {
        return ResponseEntity.ok(bookRepository.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getById(@PathVariable Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (!optionalBook.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        return ResponseEntity.ok(optionalBook.get());
    }
}
