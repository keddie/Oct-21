package histogram;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Histogram {

	public static void main(String[] args) {
		Map<Integer, Long> data = Stream.generate(()->
			ThreadLocalRandom.current().nextInt(1, 7)
			+ ThreadLocalRandom.current().nextInt(1, 7)
			+ ThreadLocalRandom.current().nextInt(1, 7)
			+ ThreadLocalRandom.current().nextInt(1, 7)
			+ ThreadLocalRandom.current().nextInt(1, 7)
			+ ThreadLocalRandom.current().nextInt(1, 7)
			+ ThreadLocalRandom.current().nextInt(1, 7)
			+ ThreadLocalRandom.current().nextInt(1, 7)
			+ ThreadLocalRandom.current().nextInt(1, 7)
			+ ThreadLocalRandom.current().nextInt(1, 7)
				)
		.limit(100_000)
		.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		
		long maxVal = data.entrySet().stream()
				.mapToLong(Map.Entry::getValue)
				.max()
				.getAsLong();
		
		data.entrySet().stream()
		.sorted(Map.Entry.comparingByKey())
//		.forEach(System.out::println);
		.forEach(e -> System.out.printf("%2d: %s\n", 
				e.getKey(), 
				Stream.generate(()->"*")
					.limit(e.getValue() * 80 / maxVal)
					.collect(Collectors.joining())));
		
		;
	}

}
