package com.zenika.academy.WoodBlockToys.barrel;

import com.zenika.academy.WoodBlockToys.bloc.woodessence.WoodEssence;
import com.zenika.academy.WoodBlockToys.bloc.woodessence.WoodEssenceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BarrelService {
    private BarrelRepository barrelRepository;
    private WoodEssenceRepository woodEssenceRepository;

    private BarrelFactory barrelFactory = new BarrelFactory();

    public BarrelService(BarrelRepository barrelRepository, WoodEssenceRepository woodEssenceRepository) {
        this.barrelRepository = barrelRepository;
        this.woodEssenceRepository = woodEssenceRepository;
    }

    @Transactional
    Barrel saveBarrel(Barrel barrel) {
        return barrelRepository.save(barrelFactory.generateBarrel(barrel, getWoodEssenceList()));
    }

    void deleteBarrel(Long id) {
        barrelRepository.deleteById(id);
    }

    Barrel getBarrel(Long id) {
        Optional<Barrel> currentBarrel = barrelRepository.findById(id);

        if (currentBarrel.isEmpty()) {
            throw new EntityNotFoundException("Barrel not found");
        } else {
            return currentBarrel.get();
        }
    }

    List<Barrel> getBarrelList() {
        List<Barrel> barrelList = new ArrayList<>();
        barrelRepository.findAll().forEach(barrelList::add);
        return barrelList;
    }

    List<WoodEssence> getWoodEssenceList() {
        List<WoodEssence> woodEssenceList = new ArrayList<>();
        woodEssenceRepository.findAll().forEach(woodEssenceList::add);
        return woodEssenceList;
    }
}
