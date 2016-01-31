package algorithm;

import algorithm.utility.Coordinate;
import src.Field;
import src.Tile;
import src.TilingAssignment;
import src.TilingFrame;

import java.util.List;

public abstract class TilingAlgorithm {
	private static final int DELAY = 0;
	public static final long FOREVER = Long.MAX_VALUE;
	private boolean draw;

	protected Field field;
	protected TilingFrame frame;

	protected TilingAlgorithm(Field field, TilingFrame frame) {
		this.field = field;
		this.frame = frame;
		draw = true;
	}

	protected String formatTiles(List<Tile> tiles) {
		if (tiles.isEmpty()) {
			return "Empty";
		}
		StringBuilder out = new StringBuilder();
		for (Tile t : tiles) {
			out.append(formatTile(t)).append("  ");
		}
		return out.toString();
	}

	protected String formatTile(Tile t) {
		return t.getWidth() + "x" + t.getHeight();
	}

	protected void w8(long milliseconds){
		try{
			Thread.sleep(milliseconds);
		}catch(Exception e){
			System.out.println(e.toString());
		}
	}

	private void draw() {
		if (draw) {
			frame.redraw(DELAY);
		}
	}

	protected void putOnField(Tile tile, int x, int y) {
		field.uncheckedPlaceTile(tile, x, y);
		draw();
	}

	protected void putOnField(Tile tile, Coordinate c) {
		putOnField(tile, c.getX(), c.getY());
	}

	protected boolean checkedPlaceTile(Tile tile, int x, int y) {
		if (field.placeTileSecure(tile, x, y)) {
			draw();
			return true;
		}
		return false;
	}

	protected void undo(Tile tile) {
		field.undo(tile, tile.getX(), tile.getY());
		draw();
	}

	public abstract void placeTiles(TilingAssignment assignment);
}
