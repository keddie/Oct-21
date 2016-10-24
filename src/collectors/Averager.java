package collectors;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

class AverageInProgress {
	private long count;
	private double sum;	

	static AtomicInteger getAverageCount = new AtomicInteger();
	static AtomicInteger getTotalCount   = new AtomicInteger();
	static AtomicInteger getCountCount   = new AtomicInteger();
	static AtomicInteger mergeCount      = new AtomicInteger();
	
	public void include(double number) {
		sum += number;
		count++;
	}

	public double getAverage() {
		getAverageCount.incrementAndGet();
		return sum / count;
	}

	public double getTotal() {
		getTotalCount.incrementAndGet();
		return sum;
	}

	public long getCount() {
		getCountCount.incrementAndGet();
		return count;
	}
	
	public void merge(AverageInProgress a) {
		int m = mergeCount.getAndIncrement();
		System.out.printf("Merge %d: (%f@%d) -> (%f@%d)\n",m,a.sum,a.count,this.sum,this.count);
		this.sum += a.sum;
		this.count += a.count;
	}

	static public String stats(){
		return String.format("getAverageCount=%d, getTotalCount=%d, getCountCount=%d, mergeCount=%d",getAverageCount.get(),getTotalCount.get(),getCountCount.get(),mergeCount.get());
	}
}

public class Averager {

	public static void main(String[] args) {
		System.out.println(AverageInProgress.stats());
		long start = System.nanoTime();
		Double average = 
			ThreadLocalRandom.current().doubles()
			.parallel()
			.unordered()
				.limit(1_000_000_000L)
				.average()
				.getAsDouble();
		long end = System.nanoTime();
		System.out.printf("Average is %f, time is %f\n",average,(end-start) / 1_000_000.0);
		System.out.println(AverageInProgress.stats());
	}

}
