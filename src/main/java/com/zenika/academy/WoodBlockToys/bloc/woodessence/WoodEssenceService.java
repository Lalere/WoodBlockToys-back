package com.zenika.academy.WoodBlockToys.bloc.woodessence;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WoodEssenceService {
    private WoodEssenceRepository woodEssenceRepository;

    public WoodEssenceService(WoodEssenceRepository woodEssenceRepository) {
        this.woodEssenceRepository = woodEssenceRepository;
    }

    @Transactional(noRollbackFor = {EntityExistsException.class})
    WoodEssence saveWoodEssence(WoodEssence woodEssence) {
        Optional<WoodEssence> currentWoodEssenceByEmail = woodEssenceRepository.findByType(woodEssence.getType());

        if (currentWoodEssenceByEmail.isPresent()) {
            throw new EntityExistsException("This woodEssence already exists");
        } else {
            return woodEssenceRepository.save(woodEssence);
        }
    }

    @Transactional(noRollbackFor = {EntityNotFoundException.class})
    WoodEssence updateWoodEssence(WoodEssence woodEssence, Long id) {
        woodEssence.setId(id);
        Optional<WoodEssence> currentWoodEssence = woodEssenceRepository.findById(id);

        if (currentWoodEssence.isEmpty()) {
            throw new EntityNotFoundException("WoodEssence not found");
        } else {
            return woodEssenceRepository.save(woodEssence);
        }
    }

    void deleteWoodEssence(Long id) {
        woodEssenceRepository.deleteById(id);
    }

    WoodEssence getWoodEssence(Long id) {
        Optional<WoodEssence> currentWoodEssence = woodEssenceRepository.findById(id);

        if (currentWoodEssence.isEmpty()) {
            throw new EntityNotFoundException("WoodEssence not found");
        } else {
            return currentWoodEssence.get();
        }
    }

    List<WoodEssence> getWoodEssenceList() {
        List<WoodEssence> woodEssenceList = new ArrayList<>();
        woodEssenceRepository.findAll().forEach(woodEssenceList::add);
        return woodEssenceList;
    }
}
