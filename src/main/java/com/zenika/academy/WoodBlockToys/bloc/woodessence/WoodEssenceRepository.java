package com.zenika.academy.WoodBlockToys.bloc.woodessence;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface WoodEssenceRepository extends CrudRepository<WoodEssence, Long> {
    Optional<WoodEssence> findByType (String type);
}
