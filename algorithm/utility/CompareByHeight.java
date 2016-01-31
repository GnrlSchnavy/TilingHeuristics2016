package algorithm.utility;

import src.Tile;

import java.util.Comparator;

public class CompareByHeight implements Comparator<Tile> {

    @Override
    public int compare(Tile t0, Tile t1) {
        if (t0.getHeight() > t1.getHeight()) {
            return 1;
        } else if (t0.getHeight() < t1.getHeight()) {
            return -1;
        } else {
            return 0;
        }
    }
}
