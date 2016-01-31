package algorithm.fillcolumn;

import java.util.Comparator;

public class SortVertexByY implements Comparator<Vertex> {

	@Override
	public int compare(Vertex vertex, Vertex t1) {
		return (int) (vertex.getY() - t1.getY());
	}
}
