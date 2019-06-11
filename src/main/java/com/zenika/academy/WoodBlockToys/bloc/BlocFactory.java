package com.zenika.academy.WoodBlockToys.bloc;

import com.zenika.academy.WoodBlockToys.bloc.woodessence.WoodEssence;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class BlocFactory {


    private final List<String> blocColors = Arrays.asList("blue", "white", "red",
            "yellow", "orange", "green", "black");
    private final List<String> blocShape = Arrays.asList("square",
            "triangular", "hex", "rectangular", "cylindrical");
    private final List<Integer> blocHeight = Arrays.asList(1, 4, 7);
    private final List<String> blocFinish = Arrays.asList("matte", "shiny", "satiny", "raw");


    private void shuffleLists() {
        Collections.shuffle(blocColors);
        Collections.shuffle(blocShape);
        Collections.shuffle(blocHeight);
        Collections.shuffle(blocFinish);
    }

    private int getRandomBlocVolumeInRange() {
        Random r = new Random();
        return r.nextInt((30 - 10) + 1) + 10;
    }

    public Bloc generateBloc(String finish, List<WoodEssence> blocWoodEssence) {
        shuffleLists();
        Collections.shuffle(blocWoodEssence);
        Bloc bloc = Bloc.builder().build();
        bloc.setFinish(finish);
        bloc.setWoodEssence(blocWoodEssence.get(0).getType());
        if (finish.equals("mixed")) {
            bloc.setFinish(blocFinish.get(0));
        }
        if (bloc.getFinish().equals("raw")) {
            bloc.setColor("none");
            bloc.setFinish("raw");
        } else {
            bloc.setColor(blocColors.get(0));
            bloc.setWoodEssence("pine");
            //Swapping pine essence to index 0 for price computing
            for (int i = 0; i < blocWoodEssence.size(); i++) {
                if (blocWoodEssence.get(i).getType().equals("pine")) {
                    Collections.swap(blocWoodEssence, 0, i);
                }
            }
        }

        bloc.setVolume(getRandomBlocVolumeInRange());
        bloc.setBaseType(blocShape.get(0));
        bloc.setHeight(blocHeight.get(0));
        bloc.setPrice(computeBlocPrice(bloc, blocWoodEssence.get(0)));
        return bloc;
    }

    private double computeBlocPrice(Bloc bloc, WoodEssence woodEssence) {
        double blocPrice = 0;
        blocPrice += ((woodEssence.getPrice() / 1000000) * 3 * bloc.getVolume());
        blocPrice += 0.35;
        blocPrice *= 1.20;
        return blocPrice;
    }
}
