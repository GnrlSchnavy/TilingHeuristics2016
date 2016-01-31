package src;

import algorithm.bruteforce.BruteForce;
import algorithm.given.Default;
import algorithm.fillcolumn.FillColumns;
import algorithm.TilingAlgorithm;
import algorithm.minvertex.minVertex;
import algorithm.minwhitespace.MinWhiteSpace;

public class AlgorithmFactory {
	private Field field;
	private TilingFrame frame;

	AlgorithmFactory(Field field, TilingFrame frame) {
		this.field = field;
		this.frame = frame;
	}

	TilingAlgorithm getAlgorithm(String algorithm) {
		switch (algorithm) {
			case "BruteForce":
				return new BruteForce(field, frame);
			case "minVertex":
				return new minVertex(field, frame);
			case "MinWhiteSpace":
				return new MinWhiteSpace(field, frame);
			case "FillColumns":
				return new FillColumns(field, frame);
			default:
				return new Default(field, frame);
		}
	}
}
