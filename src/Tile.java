package src;

import java.awt.*;
import java.util.Random;

public class Tile implements Comparable<Tile> {
	private int x;
	private int y;
	private int width;
	private int height;
	private Color color;
	private boolean flipped;

	public Tile(int width, int height) {
		this(width, height, new Color(new Random().nextInt(0x1000000)));
	}

	public Tile(int width, int height, Color colour) {
		x = 0;
		y = 0;
		this.width = width;
		this.height = height;
		this.color = colour;
		flipped = false;
	}

	public void setCoordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Tile(int size) {
		this(size, size);
	}

	public int getHeight() {
		return this.height;
	}

	public int getWidth() {
		return this.width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void flipTile() {
		int temp = width;
		width = height;
		height = temp;
		flipped = !flipped;
		TilingProblem.flips++;
	}

	public Tile flip() {
		flipTile();
		return this;
	}

	public boolean isFlipped() {
		return flipped;
	}

	public int getArea() {
		return this.width * this.height;
	}

	public Color getColor() {
		return this.color;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Tile) {
			Tile tile = (Tile) obj;
			// There is a chance that colours are the same
			return tile.color == this.color;
		}
		return super.equals(obj);
	}

//	public boolean equals(Object obj) {
//		if (obj instanceof Tile) {
//			Tile tile = (Tile) obj;
//			return (tile.width == this.width) && (tile.height == this.height);
//		}
//		return super.equals(obj);
//	}

	@Override
	public int compareTo(Tile o) {
		return this.getArea() - o.getArea();
	}
}