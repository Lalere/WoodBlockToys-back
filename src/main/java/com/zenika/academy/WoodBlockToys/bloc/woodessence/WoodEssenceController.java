package com.zenika.academy.WoodBlockToys.bloc.woodessence;

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
@RequestMapping(path = "/woodEssences",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class WoodEssenceController {
    private final WoodEssenceService woodEssenceService;

    public WoodEssenceController(WoodEssenceService woodEssenceService) {
        this.woodEssenceService = woodEssenceService;
    }


    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @Transactional
    public ResponseEntity<WoodEssence> addWoodEssence(@Valid @RequestBody WoodEssence woodEssence) {
        try {
            return ResponseEntity.ok(woodEssenceService.saveWoodEssence(woodEssence));
        } catch (EntityExistsException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }


    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_UTF8_VALUE)
    @Transactional
    public ResponseEntity<WoodEssence> woodEssenceUpdate(@PathVariable Long id, @Valid @RequestBody WoodEssence woodEssence) {
        try {
            return ResponseEntity.ok(woodEssenceService.updateWoodEssence(woodEssence, id));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteWoodEssence(@PathVariable Long id) {
        try {
            woodEssenceService.deleteWoodEssence(id);
            return new ResponseEntity<>("The woodEssence with id " + id + " has been deleted", HttpStatus.ACCEPTED);
        } catch (EmptyResultDataAccessException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @GetMapping(path = "/{id}")
    public ResponseEntity<WoodEssence> getWoodEssence(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(woodEssenceService.getWoodEssence(id));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<WoodEssence>> getWoodEssenceList() {
        return ResponseEntity.ok(woodEssenceService.getWoodEssenceList());
    }
}
