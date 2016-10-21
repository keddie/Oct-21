package concordance;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Concordance {

	public static void main(String[] args) {
		try (Stream<String> input = Files.lines(Paths.get("PrideAndPrejudice.txt"))) {
			input.flatMap(s->Stream.of(s.split("\\W+")))
			.filter(s->s.length() != 0)
			.map(String::toLowerCase)
			.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
			.entrySet().stream()
			.sorted(Map.Entry.<String, Long>comparingByValue().reversed())
			.limit(200)
			.forEach(e->System.out.printf("%20s : %5d\n", e.getKey(), e.getValue()));
		} catch (IOException e1) {
			e1.printStackTrace();
		} 
	}
}
