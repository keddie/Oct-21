package students;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class School {

	public static void main(String[] args) {
		List<Student> roster = Arrays.asList(
				Student.ofNamesGpaCourses("Fred", "Jones", 3.2F, "Math", "Physics"),
				Student.ofNamesGpaCourses("Jim", "Smith", 3.4F, "Math", "Physics", "Chemistry"),
				Student.ofNamesGpaCourses("Shiela", "Pearl", 2.9F, "Math"),
				Student.ofNamesGpaCourses("Alice", "Barnett", 2.2F, "Chemistry", "Physics", "Biology"),
				Student.ofNamesGpaCourses("Shiela", "Smith", 3.9F, "Math", "Organic Chemistry"),
				Student.ofNamesGpaCourses("Fred", "Jones", 3.1F, "Math"),
				Student.ofNamesGpaCourses("Fred", "Blair", 3.7F, "Math", "Art"),
				Student.ofNamesGpaCourses("Jenny", "Smith", 3.1F, "Art", "French", "Geography"));

		Predicate<Student> smart = s -> s.getGpa() > 3.3F;
		roster.stream()
		.filter(smart)
		.map(s -> s.getLastName() + " scored " + s.getGpa())
		.forEach(System.out::println);
		System.out.println("--------------");

		roster.stream()
		.filter(smart.negate())
		.forEach(s -> System.out.println("Smart: " + s));
		System.out.println("--------------");

		roster.stream()
		.sorted((a,b) -> a.getCourses().size() - b.getCourses().size())
		.forEach(System.out::println);
		System.out.println("--------------");

		roster.stream()
		.sorted(Comparator.<Student>comparingInt(s->s.getCourses().size()))
		.forEach(System.out::println);
		System.out.println("--------------");
		
		roster.stream()
		.max(Comparator.<Student>comparingDouble(s->s.getGpa()))
		.ifPresent(s -> System.out.println("smartest is " + s));
		System.out.println("--------------");
		
		Stream.<Student>empty()
		.max(Comparator.<Student>comparingDouble(s->s.getGpa()))
		.ifPresent(s -> System.out.println("smartest is " + s));
		System.out.println("--------------");
		
		roster.stream()
		.peek(System.out::println)
//		.sorted(Comparator.<Student>comparingInt(s->s.getCourses().size()))
		.findAny()
		;
		System.out.println("--------------");
		
		roster.stream()
		.flatMap(s-> Stream.of(s.getFirstName(), s.getLastName()))
		.forEach(System.out::println);
		System.out.println("--------------");

		roster.stream()
		.flatMap(s-> s.getCourses().stream())
		.distinct()
		.sorted()
		.forEach(System.out::println);
		System.out.println("--------------");
		
		roster.stream()
		.collect(Collectors.groupingBy(Student::getFirstName))
		.entrySet().forEach(System.out::println);
		System.out.println("--------------");
	}

}







