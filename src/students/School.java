package students;

import java.util.Arrays;
import java.util.List;

public class School {

	public static void main(String[] args) {
		List<Student> roster = Arrays.asList(
				Student.ofNamesGpaCourses("Fred", "Jones", 3.2F, "Math", "Physics"),
				Student.ofNamesGpaCourses("Jim", "Smith", 3.4F, "Math", "Physics", "Chemistry"),
				Student.ofNamesGpaCourses("Shiela", "Pearl", 2.9F, "Math"),
				Student.ofNamesGpaCourses("Alice", "Barnett", 2.2F, "Chemistry", "Physics", "Biology"),
				Student.ofNamesGpaCourses("Fred", "Jones", 3.1F, "Math"),
				Student.ofNamesGpaCourses("Fred", "Blair", 3.7F, "Math", "Art"),
				Student.ofNamesGpaCourses("Jenny", "Smith", 3.1F, "Art", "French", "Geography")
				);
		
		roster.stream()
			.filter(s->s.getGpa()> 3.3F)
			.map(s->s.getLastName() + " scored " + s.getGpa())
			.forEach(System.out::println);
	}

}
