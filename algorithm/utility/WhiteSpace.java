package algorithm.utility;

import src.Field;

import java.util.ArrayList;
import java.util.List;

public class WhiteSpace {

	public static final int RIGHT = 0;
	public static final int DOWN = 1;
	public static final int LEFT = 2;
	public static final int UP = 3;

	private Field field;
	public int minX, minY, maxX, maxY;

	public List<List<Coordinate>> blocks;

	public WhiteSpace(Field field) {
		this.field = field;
		minX = field.getWidth();
		minY = field.getHeight();
		maxX = maxY = 0;
	}

	public List<List<Coordinate>> findBlocks() {
		blocks = new ArrayList<>();
		for (int x = 0; x < field.getWidth(); x++) {
			for (int y = 0; y < field.getHeight(); y++) {
				Coordinate c = new Coordinate(x, y);
				if (isWhiteSpace(c)) {
					boolean skip = false;
					for (List<Coordinate> whitespace : blocks) {
						if (whitespace.contains(c)) {
							skip = true;
							break;
						}
					}
					if (skip) {
						continue;
					}
					List<Coordinate> block = new ArrayList<>();
					findContinuousBlock(block, c);
					blocks.add(block);
				}
			}
		}
		return blocks;
	}

	private void findContinuousBlock(List<Coordinate> block, Coordinate c) {
		minX = Math.min(minX, c.getX());
		minY = Math.min(minY, c.getY());
		maxX = Math.max(maxX, c.getX());
		maxY = Math.max(maxY, c.getY());
		block.add(c);
		boolean[] directions = getAvailableDirections(block, c);
		for (int i = 0; i < 4; i++) {
			Coordinate nextBlock = getNextCoordinate(c, i);
			if (directions[i] && !block.contains(nextBlock)) {
				findContinuousBlock(block, nextBlock);
			}
		}
	}

	private boolean isWhiteSpace(Coordinate c) {
		return field.getTiles(c.getX(), c.getY()).isEmpty();
	}

	public static boolean isWhiteSpace(Field field, Coordinate c) {
		return field.getTiles(c.getX(), c.getY()).isEmpty();
	}

	private boolean[] getAvailableDirections(List<Coordinate> block, Coordinate c) {
		boolean[] directionsAvailable = {true, true, true, true};
		for (int i = 0; i < 4; i++) {
			Coordinate nextBlock = getNextCoordinate(c, i);
			if (touchesBoundary(nextBlock) || block.contains(nextBlock) || !isWhiteSpace(nextBlock)) {
				directionsAvailable[i] = false;
			}
		}
		return directionsAvailable;
	}

	private boolean touchesBoundary(Coordinate c) {
		return c.getX() < 0 || c.getX() >= field.getWidth() || c.getY() < 0 || c.getY() >= field.getHeight();
	}

	public static boolean touchesBoundary(Field field, Coordinate c) {
		return c.getX() < 0 || c.getX() >= field.getWidth() || c.getY() < 0 || c.getY() >= field.getHeight();
	}

	public static Coordinate getNextCoordinate(Coordinate c, int d) {
		int x = c.getX();
		int y = c.getY();
		x = d == LEFT ? x - 1 : (d == RIGHT ? x + 1 : x);
		y = d == UP ? y - 1 : (d == DOWN ? y + 1 : y);
		return new Coordinate(x, y);
	}

}
