package com.zenika.academy.WoodBlockToys.barrel;

import com.zenika.academy.WoodBlockToys.bloc.Bloc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Barrels")
public class Barrel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private double price;

    @NotNull
    private int numberOfPieces;

    @NotNull
    private String finish;

    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @OneToMany(fetch = FetchType.EAGER)
    private List<Bloc> blocList;
}
