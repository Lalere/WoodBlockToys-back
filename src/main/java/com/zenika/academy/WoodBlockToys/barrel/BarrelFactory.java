package com.zenika.academy.WoodBlockToys.barrel;

import com.zenika.academy.WoodBlockToys.bloc.Bloc;
import com.zenika.academy.WoodBlockToys.bloc.BlocFactory;
import com.zenika.academy.WoodBlockToys.bloc.woodessence.WoodEssence;

import java.util.ArrayList;
import java.util.List;


class BarrelFactory {
    private BlocFactory blocFactory = new BlocFactory();

    Barrel generateBarrel(Barrel wishedBarrel, List<WoodEssence> woodEssenceList) {
        List<Bloc> blocList = new ArrayList<>();
        Barrel finalBarrel = Barrel.builder()
                .numberOfPieces(wishedBarrel.getNumberOfPieces())
                .finish(wishedBarrel.getFinish())
                .build();

        double finalBarrelPrice = 0;
        int finalNumberOfPieces = 0;

        for (int i = 0; i < 5 || finalNumberOfPieces < wishedBarrel.getNumberOfPieces(); i++) {
            blocList.clear();
            finalBarrelPrice = 0;
            finalNumberOfPieces = 0;
            if (i == 5) {
                throw new IllegalArgumentException("The parameters of the wished barrel are incompatibles");
            }

            while (finalBarrelPrice < wishedBarrel.getPrice()) {
                Bloc bloc = blocFactory.generateBloc(wishedBarrel.getFinish(), woodEssenceList);
                finalBarrelPrice += bloc.getPrice();
                blocList.add(bloc);
                finalNumberOfPieces++;
            }
        }
        if (finalBarrelPrice > wishedBarrel.getPrice()) {
            Bloc excessBloc = blocList.get(blocList.size() - 1);
            finalBarrelPrice -= excessBloc.getPrice();
            finalNumberOfPieces--;
            blocList.remove(blocList.size() - 1);
        }
        finalBarrel.setBlocList(blocList);
        finalBarrel.setPrice(Math.round(finalBarrelPrice));
        finalBarrel.setNumberOfPieces(finalNumberOfPieces);
        return finalBarrel;
    }
}
