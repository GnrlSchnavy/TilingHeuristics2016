package src;

import algorithm.TilingAlgorithm;

import java.io.IOException;

public class TilingProblem {
	public static final int DELAY = 50;

	private Field field;
	private TilingFrame frame;
	public static long placements, undoes, flips;

	TilingProblem() {
		placements = undoes = flips = 0;
	}

	public void init(int fieldWidth, int fieldHeight, int scale) {
		field = new Field(fieldWidth, fieldHeight);
		frame = new TilingFrame(this.field, scale);
	}

	public void start(String filename, String algorithm) {
		TilingAssignment assignment;
		try {
			assignment = TilingAssignment.loadFromFile(System.getProperty("user.dir") + "/configurations/" + filename);
			init(assignment.getWidth(), assignment.getHeight(), assignment.getScale());
			System.out.println(filename);

			TilingAlgorithm tilingAlgorithm = new AlgorithmFactory(field, frame).getAlgorithm(algorithm);
			long startTime = System.currentTimeMillis();
			tilingAlgorithm.placeTiles(assignment);
			long endTime = System.currentTimeMillis();
			System.out.println("Total execution time: " + (endTime - startTime) + "ms");
			System.out.printf("Placements: %d\nUndoes: %d\nTotal: %d\n\n", placements, undoes, placements + undoes);
//			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// Default values
		String filename = "55-4-4.tiles";
		String algorithm = "minVertex";
		if (args.length > 0) {
			filename = args[0];
			algorithm = args[1];
		}
		new TilingProblem().start(filename, algorithm);
	}

}
