package collectors;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

class AverageInProgress {
	private long count;
	private double sum;

	public void include(double number) {
		sum += number;
		count++;
	}

	public double getAverage() {
		return sum / count;
	}

	public double getTotal() {
		return sum;
	}

	public long getCount() {
		return count;
	}
	
	public void merge(AverageInProgress a) {
		this.sum += a.sum;
		this.count += a.count;
	}
}

public class Averager {

	public static void main(String[] args) {
		long start = System.nanoTime();
		AverageInProgress avg = 
			ThreadLocalRandom.current().doubles()
			.parallel()
			.unordered()
				.limit(1_000_000_000L)
				.collect(AverageInProgress::new,
						 AverageInProgress::include,
						 AverageInProgress::merge);
		long end = System.nanoTime();
		System.out.printf("Average of %d numbers is %9.7f and computed in %12.9f ms\n", 
				avg.getCount(), avg.getAverage(), (end - start ) / 1_000_000.0);
	}

}
