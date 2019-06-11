package com.zenika.academy.WoodBlockToys.bloc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Bloc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private double price;

    @NotNull
    private int volume;

    @NotNull
    private String baseType;

    @NotNull
    private int height;

    @NotNull
    private String color;

    private String finish;

    @NotNull
    private String woodEssence;
}
