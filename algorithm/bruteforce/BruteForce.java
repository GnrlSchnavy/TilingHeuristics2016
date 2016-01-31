package algorithm.bruteforce;

import algorithm.TilingAlgorithm;
import src.*;

import java.util.Stack;

public class BruteForce extends TilingAlgorithm {
	private Stack<Tile> placedTiles;

	public BruteForce(Field field, TilingFrame frame) {
		super(field, frame);
	}

	private boolean place(Tile tile) {
		int x, y;
		for (x = 0; x < field.getWidth() - tile.getWidth() + 1; x++) {
			for (y = 0; y < field.getHeight() - tile.getHeight() + 1; y++) {
				if (checkedPlaceTile(tile, x, y)) {
					tile.setCoordinates(x, y);
					placedTiles.push(tile);
					return true;
				}
			}
		}
		return false;
	}

	private boolean placeTile(Tile t) {
		// Tiles are not flipped twice
		if (t.isFlipped()) {
			return place(t);
		}
		if (place(t)) {
			return true;
		} else if (t.getWidth() != t.getHeight()) {
			if (place(t.flip())) {
				return true;
			} else {
				t.flip();
				return false;
			}
		}
		return false;
	}

	private void undoLastTile() {
		undo(placedTiles.pop());
	}

	// Debugging methods
	private String formatPlacedTiles() {
		String out = "";
		for (Tile tile : placedTiles) {
			out += formatTile(tile) + "  ";
		}
		return out;
	}

	private String getState(TileSet working) {
		return "Placed: " + formatPlacedTiles() + "\n" +
				"Working: " + formatTiles(working) + "\n";
	}

	public boolean placeTilesRecursively(TileSet tiles) {
		if (tiles.isEmpty()) {
			return true;
		}
		// preselection heuristics
		for (int i = 0; i < tiles.size(); i++) {
			if (placeTile(tiles.peek(i))) {
				TileSet clonedTiles = tiles.copy();
				clonedTiles.remove(i);
				clonedTiles.stream().filter(Tile::isFlipped).forEach(Tile::flip);
				if (!placeTilesRecursively(clonedTiles)) {
					undoLastTile();
					if (!tiles.peek(i).isFlipped()) {
						tiles.peek(i).flip();
						i--;
					}
				} else {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void placeTiles(TilingAssignment assignment) {
		TileSet tiles = assignment.getTiles();
		placeTiles(tiles);
	}

	public void placeTiles(TileSet tiles) {
		placedTiles = new Stack<>();
		placeTilesRecursively(tiles);
//        frame.redraw(0);
	}
}
