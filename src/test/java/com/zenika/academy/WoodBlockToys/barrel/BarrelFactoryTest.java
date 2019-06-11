package com.zenika.academy.WoodBlockToys.barrel;

import com.zenika.academy.WoodBlockToys.bloc.Bloc;
import com.zenika.academy.WoodBlockToys.bloc.woodessence.WoodEssence;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BarrelFactoryTest {

    private BarrelFactory barrelFactory = new BarrelFactory();

    @Test
    public void generatePaintedBarrel() {
        Barrel wishedBarrel = Barrel.builder()
                .numberOfPieces(50000)
                .price(100000)
                .finish("matte")
                .build();

        List<WoodEssence> woodEssenceList = new ArrayList<>();
        woodEssenceList.add(WoodEssence.builder().price(900).type("oak").build());
        woodEssenceList.add(WoodEssence.builder().price(300).type("pine").build());
        woodEssenceList.add(WoodEssence.builder().price(600).type("beech").build());
        woodEssenceList.add(WoodEssence.builder().price(40000).type("mahogany").build());

        Barrel finalBarrel = barrelFactory.generateBarrel(wishedBarrel, woodEssenceList);

        assertThat(finalBarrel.getPrice()).isLessThanOrEqualTo(wishedBarrel.getPrice());
        assertThat(finalBarrel.getNumberOfPieces()).isGreaterThanOrEqualTo(finalBarrel.getNumberOfPieces());

        List<Bloc> blocList = finalBarrel.getBlocList();
        for (Bloc bloc : blocList) {
            assertThat(bloc.getFinish()).isEqualTo("matte");
            assertThat(bloc.getWoodEssence()).isEqualTo("pine");
            assertThat(bloc.getVolume()).isBetween(10, 30);
            assertThat(bloc.getColor()).isNotEqualTo("none");
        }
    }

    @Test
    public void generateRawBarrel() {
        Barrel wishedBarrel = Barrel.builder()
                .numberOfPieces(50000)
                .price(100000)
                .finish("raw")
                .build();

        List<WoodEssence> woodEssenceList = new ArrayList<>();
        woodEssenceList.add(WoodEssence.builder().price(900).type("oak").build());
        woodEssenceList.add(WoodEssence.builder().price(300).type("pine").build());
        woodEssenceList.add(WoodEssence.builder().price(600).type("beech").build());
        woodEssenceList.add(WoodEssence.builder().price(40000).type("mahogany").build());

        Barrel finalBarrel = barrelFactory.generateBarrel(wishedBarrel, woodEssenceList);

        assertThat(finalBarrel.getPrice()).isLessThanOrEqualTo(wishedBarrel.getPrice());
        assertThat(finalBarrel.getNumberOfPieces()).isGreaterThanOrEqualTo(finalBarrel.getNumberOfPieces());

        List<Bloc> blocList = finalBarrel.getBlocList();
        for (Bloc bloc : blocList) {
            assertThat(bloc.getColor()).isEqualTo("none");
            assertThat(bloc.getFinish()).isEqualTo("raw");
            assertThat(bloc.getVolume()).isBetween(10, 30);
        }
    }
}