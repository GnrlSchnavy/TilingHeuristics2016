package algorithm.fillcolumn;

import algorithm.TilingAlgorithm;
import algorithm.minvertex.minVertex;
import algorithm.utility.CompareBySide;
import src.*;

import java.util.*;


public class FillColumns extends TilingAlgorithm {

	private ArrayList<Vertex> vertices;

	public FillColumns(Field field, TilingFrame frame) {
		super(field, frame);
	}

	private ArrayList<Vertex> generateVertexArray(int x, int y, int w, int h) {
		ArrayList<Vertex> corners = new ArrayList<>();
		corners.add(new Vertex(x, y));
		corners.add(new Vertex(x + w, y));
		corners.add(new Vertex(x, y + h));
		corners.add(new Vertex(x + w, y + h));
		return corners;
	}

	private ArrayList<Vertex> initVertices() {
		return generateVertexArray(0, 0, field.getWidth(), field.getHeight());
	}

	private void updateVertices(Tile tile) {
		ArrayList<Vertex> corners = generateVertexArray(tile.getX(), tile.getY(), tile.getWidth(), tile.getHeight());
		for (Vertex corner : corners) {
			if (!vertices.remove(corner)) {
				vertices.add(corner);
			}
		}
	}

	private boolean place(Tile tile, int x1, int y1, int x2, int y2) {
		int x, y;
		for (x = x1; x < x2; x++) {
			for (y = y1; y < y2; y++) {
				if (checkedPlaceTile(tile, x, y)) {
					updateVertices(tile);
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

	private Map<Integer, List<Tile>> generateInvertedIndex(TileSet tiles) {
		tiles.sort(new CompareBySide().reversed());
		Map<Integer, List<Tile>> index = new HashMap<>();
		int indexSize = Math.max(tiles.peek(0).getWidth(), tiles.peek(0).getHeight());
		for (int i = 1; i <= indexSize; i++) {
			index.put(i, new ArrayList<>());
		}
		for (Tile t : tiles) {
			int width = t.getWidth();
			int height = t.getHeight();
			index.get(t.getWidth()).add(t);
			if (width != height) {
				index.get(t.getHeight()).add(t);
			}
		}
		return index;
	}

	private List<Integer> greedySubsetSum(List<Integer> list, int target) {
		List<Integer> sums = new ArrayList<>();
		int sum = 0;
		for (Integer i : list) {
			if (sum + i <= target) {
				sums.add(i);
				sum += i;
			}
//			if (sum == target) {
//				break;
//			}
		}
		return sums;
	}

	private List<Tile> fillColumn(TileSet tiles, int height) {
		Map<Integer, List<Tile>> index = generateInvertedIndex(tiles);
		ArrayList<Tile> parts = new ArrayList<>();
//		printMap(index);
		ArrayList<Integer> availableHeights = new ArrayList<>();
		for (Integer h : index.keySet()) {
			for (int i = 0; i < index.get(h).size(); i++) {
				availableHeights.add(h);
			}
		}
		Collections.reverse(availableHeights);

//		SubsetSum s = new SubsetSum();
//		s.subsetSum(availableHeights, height);
//		List<Integer> sums = s.sums;

		List<Integer> sums = greedySubsetSum(availableHeights, height);
//		System.out.println(sums);

		for (Integer i : sums) {
			Tile chosen = index.get(i).remove(0);
			for (List<Tile> ts : index.values()) {
				ts.remove(chosen);
			}
			parts.add(chosen);
			if (chosen.getHeight() != i) {
				chosen.flip();
			}
		}

		return parts;
	}

	private void printMap(Map<Integer, List<Tile>> map) {
		for (Integer height : map.keySet()) {
			System.out.print(height + "\t\t" + formatTiles(map.get(height)) + "\n");
		}
	}

	// To test if the references are correct, i.e. no duplicate tiles
	private TileSet rebuildList(Map<Integer, List<Tile>> index) {
		TileSet tileset = new TileSet();
		for (List<Tile> list : index.values()) {
			for (Tile tile : list) {
				boolean isUnique = true;
				for (Tile uTile : tileset) {
					if (tile == uTile) {
						isUnique = false;
						break;
					}
				}
				if (isUnique) {
					tileset.add(tile);
				}
			}
		}
		return tileset;
	}

	@Override
	public void placeTiles(TilingAssignment assignment) {
		vertices = initVertices();

		TileSet tiles = assignment.getTiles();
		int height = field.getHeight();
		List<Tile> ts;
		while (vertices.size() == 4) {
			ts = fillColumn(tiles, height);
			for (Tile t : ts) {
				place(t);
			}
			tiles.removeAll(ts);
		}
		new minVertex(field, frame).placeTiles(tiles);
	}
}
