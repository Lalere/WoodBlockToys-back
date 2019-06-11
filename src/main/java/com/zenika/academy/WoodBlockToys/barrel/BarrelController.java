package com.zenika.academy.WoodBlockToys.barrel;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;


@RestController
@RequestMapping(path = "/barrels",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class BarrelController {
    private final BarrelService barrelService;

    public BarrelController(BarrelService barrelService) {
        this.barrelService = barrelService;
    }


    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @Transactional
    public ResponseEntity<Barrel> addBarrel(@Valid @RequestBody Barrel barrel) {
        try {
            return ResponseEntity.ok(barrelService.saveBarrel(barrel));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteBarrel(@PathVariable Long id) {
        try {
            barrelService.deleteBarrel(id);
            return new ResponseEntity<>("The barrel with id " + id + " has been deleted", HttpStatus.ACCEPTED);
        } catch (EmptyResultDataAccessException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @GetMapping(path = "/{id}")
    public ResponseEntity<Barrel> getBarrel(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(barrelService.getBarrel(id));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Barrel>> getBarrelList() {
        return ResponseEntity.ok(barrelService.getBarrelList());
    }
}
