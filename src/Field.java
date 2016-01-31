package src;

import java.util.ArrayList;
import java.util.List;

public class Field {
	private ArrayList<Tile>[][] field;
	private int width, height;


	@SuppressWarnings("unchecked")
	public Field(int width, int height) {
		this.width = width;
		this.height = height;
		field = (ArrayList<Tile>[][]) new ArrayList<?>[width][height];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				field[i][j] = new ArrayList<>();
			}
		}
	}

	public Field(Field src) {
		this(src.width, src.height);
		this.field = src.field.clone();
	}

	public List<Tile> getTiles(int x, int y) {
		return field[x][y];
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void uncheckedPlaceTile(Tile tile, int x, int y) {
		for (int i = x; i < (x + tile.getWidth()); i++) {
			for (int j = y; j < (y + tile.getHeight()); j++) {
				field[i][j].add(tile);
			}
		}
		TilingProblem.placements++;
	}

	public boolean placeTile(Tile tile, int x, int y) {
		if ((width - x >= tile.getWidth()) && (height - y >= tile.getHeight())) {
			uncheckedPlaceTile(tile, x, y);
			return true;
		}
		return false;
	}

	public boolean canBePlaced(Tile tile, int x, int y) {
		if (!(width - x >= tile.getWidth()) || !(height - y >= tile.getHeight())) {
			return false;
		}
		for (int i = x; i < (x + tile.getWidth()); i++) {
			for (int j = y; j < (y + tile.getHeight()); j++) {
				if (!field[i][j].isEmpty()) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean placeTileSecure(Tile tile, int x, int y) {
		if (canBePlaced(tile, x, y)) {
			uncheckedPlaceTile(tile, x, y);
			return true;
		}
		return false;
	}

	public void undo(Tile tile, int x, int y) {
		for (int i = x; i < (x + tile.getWidth()); i++) {
			for (int j = y; j < (y + tile.getHeight()); j++) {
				if (i < width && j < height)
					field[i][j].remove(tile);
			}
		}
		TilingProblem.undoes++;
	}

	public boolean isOccupied(int x, int y) {
		return !field[x][y].isEmpty();
	}
}