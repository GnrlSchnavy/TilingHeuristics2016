package algorithm.minwhitespace;

import algorithm.TilingAlgorithm;
import algorithm.utility.CompareByArea;
import algorithm.utility.Coordinate;
import algorithm.utility.WhiteSpace;
import src.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MinWhiteSpace extends TilingAlgorithm {
	private List<List<Coordinate>> emptyBlocks;
	private Stack<Tile> placedTiles;
	private WhiteSpace w;

	public MinWhiteSpace(Field field, TilingFrame frame) {
		super(field, frame);
	}

	private boolean place(Tile tile, int x1, int y1, int x2, int y2) {
		int x, y;
		for (x = x1; x < x2; x++) {
			for (y = y1; y < y2; y++) {
				if (checkedPlaceTile(tile, x, y)) {
					tile.setCoordinates(x, y);
					placedTiles.push(tile);
					return true;
				}
			}
		}
		return false;
	}

	private boolean place(Tile tile, int x, int y) {
		return place(tile, x, y, field.getWidth() - tile.getWidth() + 1, field.getHeight() - tile.getHeight() + 1);
	}

	private boolean place(Tile tile) {
		return place(tile, 0, 0);
	}

	private void uncheckedPlace(Tile tile, int x, int y) {
		putOnField(tile, x, y);
		tile.setCoordinates(x, y);
		placedTiles.push(tile);
	}

	private List<Coordinate> testPlace(Tile tile, int x1, int y1, int x2, int y2) {
		List<Coordinate> positions = new ArrayList<>();
		int x, y;
		for (x = x1; x < x2; x++) {
			for (y = y1; y < y2; y++) {
				if (field.canBePlaced(tile, x, y)) {
					positions.add(new Coordinate(x, y));
				}
			}
		}
		return positions;
	}

	private List<Coordinate> testPlace(Tile tile, int x, int y) {
		return testPlace(tile, x, y, field.getWidth() - tile.getWidth() + 1, field.getHeight() - tile.getHeight() + 1);
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
	// End Debugging Methods

	private Tile getSmallestTile(TileSet tiles) {
		TileSet clone = tiles.copy();
		clone.sort(new CompareByArea());
		return clone.get(0);
	}

	private boolean isWhiteSpaceTooSmall(Tile tile) {
		for (List<Coordinate> block : emptyBlocks) {
			if (block.size() < tile.getArea()) {
				return true;
			}
		}
		return false;
	}

	private void updateEmptyBlocks() {
		emptyBlocks = w.findBlocks();
	}

	private boolean whitespaceHeuristic(TileSet tiles, Tile tile) {
		// Placement heuristics
		updateEmptyBlocks();
		// optimize x y value for whitespace
		List<Coordinate> availableLocations = testPlace(tile, 0, 0);
		// Define precisely what smallest is (currently area)
		Tile smallestTile = getSmallestTile(tiles);
//			System.out.println("  " + formatTile(smallestTile));

		while (!availableLocations.isEmpty()) {
			Coordinate c = availableLocations.remove(0);
			uncheckedPlace(tile, c.getX(), c.getY());
			updateEmptyBlocks();
			if (!isWhiteSpaceTooSmall(smallestTile)) {
				return true;
			}
			undoLastTile();
		}
		return false;
		// whitespace not too small to place, focus on the whitespace now
		// Placement heuristic end
	}

	public boolean placeTilesRecursively(TileSet tiles) {
		if (tiles.isEmpty()) {
			return true;
		}
		// preselection heuristic

		// preselection heuristic end
		for (int i = 0; i < tiles.size(); i++) {
			Tile tile = tiles.peek(i);

//			if (placeTile(tile)) {
			if (whitespaceHeuristic(tiles, tile)) {
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
		w = new WhiteSpace(field);
		TileSet tiles = assignment.getTiles();
		placedTiles = new Stack<>();
		placeTilesRecursively(tiles);
	}
}
