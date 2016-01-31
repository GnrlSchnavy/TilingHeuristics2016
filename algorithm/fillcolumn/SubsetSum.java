package algorithm.fillcolumn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubsetSum {

	public List<List<Integer>> sums;

	SubsetSum() {
		sums = new ArrayList<>();
	}

	// http://stackoverflow.com/a/4633515
	private void sum_up_recursive(ArrayList<Integer> numbers, int target, ArrayList<Integer> partial) {
		int s = 0;
		for (int x : partial) s += x;
		if (s == target) {
			sums.add(partial);
//			System.out.println("sum(" + Arrays.toString(partial.toArray()) + ")=" + target);
		}
		if (s >= target)
			return;
		for (int i = 0; i < numbers.size(); i++) {
			ArrayList<Integer> remaining = new ArrayList<>();
			int n = numbers.get(i);
			for (int j = i + 1; j < numbers.size(); j++) remaining.add(numbers.get(j));
			ArrayList<Integer> partial_rec = new ArrayList<>(partial);
			partial_rec.add(n);
			sum_up_recursive(remaining, target, partial_rec);
		}
	}

	public void subsetSum(ArrayList<Integer> numbers, int target) {
		sum_up_recursive(numbers, target, new ArrayList<>());
	}
}
