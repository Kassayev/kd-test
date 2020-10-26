package kz.kassayev.test.controller;

import kz.kassayev.test.model.Person;
import kz.kassayev.test.repository.OrderRepository;
import kz.kassayev.test.repository.PersonRepository;
import kz.kassayev.test.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {
    private final PersonRepository personRepository;
    private final OrderRepository orderRepository;
    private MessageService messageService;

    @Autowired
    public PersonController(PersonRepository personRepository, OrderRepository orderRepository, MessageService messageService) {
        this.personRepository = personRepository;
        this.orderRepository = orderRepository;
        this.messageService = messageService;
    }

    @PostMapping("/")
    public ResponseEntity<Person> create(@RequestBody @Valid Person person) {
        Person savedPerson = personRepository.save(person);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedPerson.getId()).toUri();

        return ResponseEntity.created(location).body(savedPerson);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Person> update(@PathVariable Long id, @RequestBody @Valid Person person) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        if (!optionalPerson.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        person.setId(optionalPerson.get().getId());
        personRepository.save(person);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Person> delete(@PathVariable Long id) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        if (!optionalPerson.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        deletePersonInTransaction(optionalPerson.get());

        return ResponseEntity.noContent().build();
    }

    @Transactional
    public void deletePersonInTransaction(Person person) {
        orderRepository.deleteByPersonId(person.getId());
        personRepository.delete(person);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getById(@PathVariable Long id) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        if (!optionalPerson.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        return ResponseEntity.ok(optionalPerson.get());
    }

    @GetMapping("/")
    public ResponseEntity<Page<Person>> getAll(Pageable pageable) {
        return ResponseEntity.ok(personRepository.findAll(pageable));
    }
}
