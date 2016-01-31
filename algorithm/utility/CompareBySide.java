package algorithm.utility;

import src.Tile;

import java.util.Comparator;

public class CompareBySide implements Comparator<Tile> {

    @Override
    public int compare(Tile t0, Tile t1) {
        int maxt0 = Math.max(t0.getWidth(), t0.getHeight());
        int maxt1 = Math.max(t1.getWidth(), t1.getHeight());
        if (maxt0 > maxt1) {
            return 1;
        } else if (maxt0 < maxt1) {
            return -1;
        } else {
            return 0;
        }
    }
}
