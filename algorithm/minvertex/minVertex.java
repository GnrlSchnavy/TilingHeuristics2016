package algorithm.minvertex;

import algorithm.TilingAlgorithm;
import algorithm.fillcolumn.Vertex;
import algorithm.utility.Coordinate;
import algorithm.utility.WhiteSpace;
import src.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class minVertex extends TilingAlgorithm {

	private static final double c = 0.5;
	private Stack<Tile> placedTiles;
	private List<Vertex> vertices;
	private WhiteSpace w;
	private int width, height;
	private int undoes;
	private long startTime;
	public minVertex(Field field, TilingFrame frame) {
		super(field, frame);
		width = field.getWidth();
		height = field.getHeight();
	}

	private ArrayList<Vertex> generateVertexArray(int x, int y, int w, int h) {
		w -= 1;
		h -= 1;
		ArrayList<Vertex> corners = new ArrayList<>();
		corners.add(new Vertex(x - c, y - c));
		corners.add(new Vertex(x + w + c, y - c));
		corners.add(new Vertex(x - c, y + h + c));
		corners.add(new Vertex(x + w + c, y + h + c));
		return corners;
	}

	private ArrayList<Vertex> initVertices() {
		return generateVertexArray(0, 0, field.getWidth(), field.getHeight());
	}

	private String formatVertices(List<Vertex> vertices) {
		String out = "";
		for (Vertex vertex : vertices) {
			out += vertex + "\t";
		}
		out += "\n";
		return out;
	}

	private void updateVertices(Tile tile) {
		ArrayList<Vertex> corners = generateVertexArray(tile.getX(), tile.getY(), tile.getWidth(), tile.getHeight());
		for (Vertex corner : corners) {
			if (!vertices.remove(corner)) {
				vertices.add(corner);
			}
		}
	}

	private void place(Tile tile, int x, int y) {
		putOnField(tile, x, y);
		tile.setCoordinates(x, y);
		updateVertices(tile);
		placedTiles.push(tile);
	}

	private void undoLastTile() {
		Tile lastTile = placedTiles.pop();
		undo(lastTile);
		updateVertices(lastTile);
	}

	private TileSet getAllForms(TileSet tiles) {
		TileSet newList = tiles.copy();
		for (int i = 0; i < tiles.size(); i++) {
			Tile t = tiles.peek(i);
			if (t.getWidth() != t.getHeight()) {
				newList.add(newList.indexOf(t) + 1, new Tile(t.getHeight(), t.getWidth(), t.getColor()));
			}
		}
		return newList;
	}

	private TileSet getSubset(TileSet tiles, Tile tile) {
		TileSet subset = tiles.copy();
		do {
			subset.remove(tile);
		} while (subset.contains(tile));
		return subset;
	}

	private List<Coordinate> availableLocations(Tile tile, int x1, int y1, int x2, int y2) {
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

	private List<Coordinate> availableLocations(Tile tile, List<Coordinate> coordinates) {
		List<Coordinate> positions = new ArrayList<>();
		for (Coordinate c : coordinates) {
			if (field.canBePlaced(tile, c.getX(), c.getY())) {
				positions.add(c);
			}
		}
		return positions;
	}

	private boolean[] getAvailableDirections(Coordinate c) {
		boolean[] directionsAvailable = {true, true, true, true};
		for (int i = 0; i < 4; i++) {
			Coordinate nextBlock = WhiteSpace.getNextCoordinate(c, i);
			if (WhiteSpace.touchesBoundary(field, nextBlock) || !WhiteSpace.isWhiteSpace(field, nextBlock)) {
				directionsAvailable[i] = false;
			}
		}
		return directionsAvailable;
	}

	private boolean isCorner(boolean[] directionsAvailable) {
		for (int i = 0; i < 4; i++) {
			if (!directionsAvailable[i] && !directionsAvailable[(i + 1) % 4]) {
				return true;
			}
		}
		return false;
	}

	private Coordinate compensateCorner(Coordinate c, boolean[] directions, Tile tile) {
		int x = c.getX();
		if (!directions[WhiteSpace.RIGHT] && tile.getWidth() <= c.getX()) {
			x = c.getX() - tile.getWidth() + 1;
		}
		int y = c.getY();
		if (!directions[WhiteSpace.DOWN] && tile.getHeight() <= c.getY()) {
			y = c.getY() - tile.getHeight() + 1;
		}
		return new Coordinate(x, y);
	}

	private List<Coordinate> findCorners(List<List<Coordinate>> whiteBlocks, Tile tile) {
		List<Coordinate> joinedList = new ArrayList<>();
		List<Coordinate> corners = new ArrayList<>();
		whiteBlocks.forEach(joinedList::addAll);
		for (Coordinate c : joinedList) {
			boolean[] availableDirections = getAvailableDirections(c);
			if (isCorner(availableDirections)) {
				corners.add(compensateCorner(c, availableDirections, tile));
			}
		}
		return corners;
	}

	private Tile minVertexSize(TileSet forms) {
		forms.sort(new CompareTiles().reversed());
		Tile tileToPlace = forms.peek(0);
		List<Coordinate> availableLocations = availableLocations(tileToPlace, 0, 0, width - tileToPlace.getWidth() + 1, height - tileToPlace.getHeight() + 1);
		int index;
		if (availableLocations.size() > 0) {
			index = 0;
		} else if (tileToPlace.getWidth() != tileToPlace.getHeight()) {
			tileToPlace = forms.peek(1);
			availableLocations = availableLocations(tileToPlace, 0, 0, width - tileToPlace.getWidth() + 1, height - tileToPlace.getHeight() + 1);
			if (availableLocations.size() > 0) {
				index = 1;
			} else {
				return null;
			}
		} else {
			return null;
		}

		List<List<Coordinate>> whiteBLocks = w.findBlocks();
		int x = availableLocations.get(index).getX();
		int y = availableLocations.get(index).getY();
		place(tileToPlace, x, y);
		int vertexSize = vertices.size();
		undoLastTile();
		for (Tile form : forms) {
			List<Coordinate> corners = findCorners(whiteBLocks, form);
			availableLocations = availableLocations(form, corners);
			for (Coordinate location : availableLocations) {
				place(form, location.getX(), location.getY());
				if (vertices.size() < vertexSize) {
					tileToPlace = form;
					vertexSize = vertices.size();
					x = location.getX();
					y = location.getY();
				}
				undoLastTile();
			}
		}
		tileToPlace.setCoordinates(x, y);
		return tileToPlace;
	}

	private boolean solve(TileSet forms) {
		long currentTime = System.currentTimeMillis();
		if (forms.isEmpty()) {
			return true;
		}
		if(currentTime - startTime>(60000*5)){
			System.out.println("overtime");
			System.exit(0);
		}

		Tile chosenTile = minVertexSize(forms);
		if (chosenTile != null) {
			place(chosenTile, chosenTile.getX(), chosenTile.getY());

			if (solve(getSubset(forms, chosenTile))) {
				return true;
			} else {
				undoLastTile();
				undoes--;
				if (undoes <= 0) {
					List<List<Coordinate>> whiteBlocks = w.findBlocks();
					List<Coordinate> joinedList = new ArrayList<>();
					whiteBlocks.forEach(joinedList::addAll);
					int minX, minY, maxX, maxY;
					minX = minY = Integer.MAX_VALUE;
					maxX = maxY = -Integer.MAX_VALUE;
					for (Coordinate block : joinedList) {
						minX = Math.min(minX, block.getX());
						maxX = Math.max(maxX, block.getX());
						minY = Math.min(minY, block.getY());
						maxY = Math.max(maxY, block.getY());
					}
					bruteforce(forms, minX, minY, maxX+1, maxY+1);
					return true;
				}
				//?

			}
		}
		return false;
	}

	private boolean bruteforce(TileSet forms, int minX, int minY, int maxX, int maxY) {
		long currentTime = System.currentTimeMillis();
		if (forms.isEmpty()) {
			return true;
		}
		if(currentTime - startTime>(60000*5)){
			System.out.println("overtime");
			System.exit(0);
		}
		for (Tile form : forms) {
			List<Coordinate> availableLocations = availableLocations(form, minX, minY, maxX, maxY);
			if (availableLocations.size() > 0) {
				Coordinate location = availableLocations.get(0);
				place(form, location.getX(), location.getY());
				if (bruteforce(getSubset(forms, form), minX, minY, maxX, maxY)) {
					return true;
				} else {
					undoLastTile();
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
		startTime = System.currentTimeMillis();
		undoes = 10;
		vertices = initVertices();
		w = new WhiteSpace(field);
		placedTiles = new Stack<>();
		TileSet tileForms = getAllForms(tiles);
		solve(tileForms);
	}
}
