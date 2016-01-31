package algorithm.minvertex;

import src.Tile;

import java.util.Comparator;

public class CompareTiles implements Comparator<Tile> {

	@Override
	public int compare(Tile t0, Tile t1) {
		int a0 = t0.getArea();
		int a1 = t1.getArea();
		if (a0 != a1) {
			return 2 * (a0 - a1);
		} else {
			//width-height ratio
			double r0 = Math.max(t0.getWidth(), t0.getHeight()) / Math.min(t0.getWidth(), t0.getHeight());
			double r1 = Math.max(t1.getWidth(), t1.getHeight()) / Math.min(t1.getWidth(), t1.getHeight());
			if (r0 > r1) {
				return 1;
			} else if (r1 < r0) {
				return -1;
			}
		}
		return 0;
	}
}
