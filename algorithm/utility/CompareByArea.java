package algorithm.utility;

import src.Tile;

import java.util.Comparator;

public class CompareByArea implements Comparator<Tile> {

    @Override
    public int compare(Tile t0, Tile t1) {
        int a0 = t0.getArea();
        int a1 = t1.getArea();
        return a0-a1;
    }
}
