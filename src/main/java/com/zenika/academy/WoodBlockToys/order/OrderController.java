package com.zenika.academy.WoodBlockToys.order;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping(path = "/orders",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @Transactional
    public ResponseEntity<Order> addOrder(@Valid @RequestBody Order order) {
        try {
            return ResponseEntity.ok(orderService.saveOrder(order));
        } catch (EntityExistsException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_UTF8_VALUE)
    @Transactional
    public ResponseEntity<Order> orderUpdate(@PathVariable Long id, @Valid @RequestBody Order order) {
        try {
            return ResponseEntity.ok(orderService.updateOrder(order, id));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
            return new ResponseEntity<>("The order with id " + id + " has been deleted", HttpStatus.ACCEPTED);
        } catch (EmptyResultDataAccessException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @GetMapping(path = "/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(orderService.getOrder(id));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Order>> getOrderList() {
        return ResponseEntity.ok(orderService.getOrderList());
    }
}
