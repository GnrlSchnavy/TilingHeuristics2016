package algorithm.given;

import algorithm.TilingAlgorithm;
import src.Field;
import src.Tile;
import src.TilingAssignment;
import src.TilingFrame;

public class Default extends TilingAlgorithm {

    public Default(Field field, TilingFrame frame) {
        super(field, frame);
    }

    @Override
    public void placeTiles(TilingAssignment assignment) {
//        int numberOfTiles = assignment.getTiles().size();
//        int numberOfTileSizes = assignment.getTiles().getNumberOfTileSizes();

        Tile tile = assignment.getTiles().get(0);
        int x = 0;
        int y = 0;
        while (assignment.getTiles().size() >= 1) {
            System.out.printf("Placing tile of size: \t %d, %d at %d, %d\n",
                    tile.getWidth(), tile.getHeight(), x, y);

            putOnField(tile, x, y);
            System.out.printf("Undoing last placed tile\n");
//            undo(tile, x, y);

            tile = assignment.getTiles().get(0);
        }
    }
}
