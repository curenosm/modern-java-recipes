package com.curenosm.chapter4;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;

@Slf4j
@SpringBootApplication
public class Chapter4Application {

	public static void main(String[] args) {
		SpringApplication.run(Chapter4Application.class, args);
	}

	/**
	 * The {@code sorted} method on Stream produces a new, sorted stream using the natural
	 * ordering for the class.
	 * The next image illustrates better:
	 * TODO: 30/07/2023
	 * @implNote The {@code sorted} method
	 * @see <a href="https://docs.oracle.com/javase/tutorial/figures/uiswing/components/ComboBoxDemo2.png">ComboBoxDemo2.png</a>
	 * @since 1.0.0
	 * @return Lambda to be executed immediately after our application context has been
	 * created.
	 * <img src="https://cdn-icons-png.flaticon.com/512/154/154878.png?w=826&t=st=1690775518~exp=1690776118~hmac=ff5c1acde4e702a0b9c86932290661e3283f985e1051746b5cac06f0bc3e80bf"/>
	 */
	@Bean
	@Order(1)
	public CommandLineRunner sortingUsingComparator() {
		return args -> {
			List<String> sampleStrings = Arrays.asList("this", "is", "a", "list", "of", "strings");

			List<Golfer> golfers = Arrays.asList(new Golfer("Mark", "Robinson", 68),
					new Golfer("Patricia", "Patrick", 8), new Golfer("Charles", "Petersen", 17));

			List<Golfer> res = sortByScoreThenLastThenFirst(golfers);
			System.out.println(res);

		};
	}

	@Bean
	@Order(2)
	public CommandLineRunner convertingAStreamIntoACollection() {
		return args -> {
			List<String> superHeroes = Stream.of("Mr. Furious", "The Blue Raja", "The Shoveler")
					.collect(Collectors.toList());

			// HashSet
			Set<String> villains = Stream
					.of("Casanova Frankenstein", "The Disco Boys", "The Not-So-Goodie Mob", "Casanova Frankenstein")
					.collect(Collectors.toSet());

			// LinkedList
			List<String> actors = Stream.of("Hank Azaria", "Janeane Garofalo")
					.collect(Collectors.toCollection(LinkedList::new)); // Collection
																		// supplier

			// Arrays
			Object[] arr = Stream.of("The Waffler", "Reverse Psychologist", "PMS Avenger").toArray();
			String[] wannabes = Stream.of("The Waffler", "Reverse Psychologist", "PMS Avenger").toArray(String[]::new);

			// Maps
			Set<Actor> actorset = new HashSet<>();
			actorset.add(new Actor("Sup", "Last"));
			Map<String, String> actorMap = actorset.stream().collect(Collectors.toMap(Actor::getName, Actor::getRole));

			actorMap.forEach((key, value) -> System.out.printf("%s played %s%n", key, value));

			Map<String, String> actorMap2 = actorset.stream()
					.collect(Collectors.toConcurrentMap(Actor::getName, Actor::getRole));

			actorMap2.forEach((key, value) -> System.out.printf("%s played %s%n", key, value));

		};
	}

	@Bean
	@Order(3)
	public ApplicationRunner addingLinearCollectionToAMap() {
		return args -> {
			List<Book> books = Arrays.asList(new Book(1, "Modern Java Recipes", 49.99),
					new Book(2, "Java 8 in Action", 49.99), new Book(3, "Java SE8 for the Really Impatient", 49.99),
					new Book(4, "Functional Programming in Java", 27.64), new Book(5, "Making Java Groovy", 45.99),
					new Book(6, "Gradle recipes for Android", 23.76));
			Map<Integer, Book> map = books.stream().collect(Collectors.toMap(Book::getId, b -> b));

			System.out.println(map);

			map = books.stream().collect(Collectors.toMap(Book::getId, Function.identity()));

			System.out.println(map);
		};
	}

	@Bean
	@Order(4)
	public ApplicationRunner sortingMaps() {
		return args -> {
			ClassPathResource dictionary = new ClassPathResource("words.txt");
			try (Stream<String> lines = Files.lines(Path.of(dictionary.getURI()))) {
				System.out.println(lines.toString());
				lines.filter(s -> s.length() > 20)
						.collect(
							Collectors.groupingBy(
								String::length, // Ascending order by default
								Collectors.counting()))
						.forEach((len, num) -> {
							System.out.printf("%d: %d%n", len, num);
						});
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("Number of words of each length (desc order)");
			// lines implements Closeable
			try (Stream<String> lines = Files.lines(Path.of(dictionary.getURI()))) {
				Map<Integer, Long> map = lines.filter(s -> s.length() > 20)
					.collect(
						Collectors.groupingBy(
							String::length,
							Collectors.counting()
						));

				map.entrySet()
					.stream()
					.sorted(
						Map.Entry.comparingByKey(Comparator.reverseOrder()))
					.forEach(e -> System.out.printf("Length %d: %2d words%n", e.getKey(), e.getValue()));
			} catch (IOException e) {
				e.printStackTrace();
			}

		};
	}

	/**
	 * In case we wanted to group elements by categories
	 */
	@Bean
	@Order(5)
	public ApplicationRunner partitioningGrouping() {
		return args -> {

			List<String> strings = Arrays.asList("this", "is", "a", "long", "list", "of", "strings", "to",
				"user", "as", "a", "demo");

			Map<Boolean, List<String>> lengthMap = strings.stream()
				.collect(Collectors.partitioningBy(e -> e.length() % 2 == 0));

			lengthMap.forEach((key, value) -> System.out.printf("%5s: %s%n", key, value));

			strings
				.stream()
				.collect(Collectors.groupingBy(String::length))
				.forEach((key, value) -> System.out.printf("%5s: %s%n", key, value));

		};
	}

	@Bean
	@Order(6)
	public ApplicationRunner downstreamCollectors() {
		return args -> {
			List<String> strings = Arrays.asList("this", "is", "a", "long", "list", "of", "strings", "to",
				"user", "as", "a", "demo");

			strings
				.stream()
				.collect(
					Collectors.partitioningBy(
						e -> e.length() % 2 == 0,
						Collectors.counting()))
				.forEach((key, value) -> System.out.printf("%5s: %s%n", key, value));

			// Equivalent to Stream methods
			/*
			count   								counting
			map     								mapping
			min     								minBy
			max     								maxBy
			IntStream.sum						summingInt
			DoubleStream.sum				summingDouble
			LongStream.summarizing	summarizingInt
			*/
		};
	}

	@Bean
	@Order(7)
	public ApplicationRunner findingMaxAndMin() {
		return args -> {
			List<Employee> employees = Arrays.asList(
				new Employee("Cersei", 250_000, "Lannister"),
				new Employee("Jamie", 150_000, "Lannister"),
				new Employee("Tyrion", 1_000, "Lannister"),
				new Employee("Tywin", 1_000_000, "Lannister"),
				new Employee("Jon Snow", 75_000, "Stark"),
				new Employee("Robb", 120_000, "Stark"),
				new Employee("Eddard", 125_000, "Stark"),
				new Employee("Sansa", 0, "Stark"),
				new Employee("Arrya", 1_000, "Stark")
			);

			Employee defaultEmployee = new Employee(
				"A man (or woman) has no name",
				0,
				"Black and White"
			);

			Optional<Employee> optionalEmp = employees.stream()
				.reduce(
					BinaryOperator.maxBy(
						Comparator.comparing(Employee::getSalary)));

			Optional<Employee> maxSalary = employees
				.stream()
				.max(Comparator.comparingInt(Employee::getSalary));

			OptionalInt maxSalary2 = employees
				.stream()
				.mapToInt(Employee::getSalary)
				.max();

			Optional<Employee> maxSalary3 = employees
				.stream()
				.max(Comparator.comparingInt(Employee::getSalary));

			System.out.println("Emp with max salary:" + optionalEmp.orElse(defaultEmployee));
			System.out.println("Emp with max salary:" + maxSalary.orElse(defaultEmployee));
			System.out.println("Emp with max salary:" + maxSalary2.orElse(0));
			System.out.println("Emp with max salary:" + maxSalary3.orElse(defaultEmployee));
		};
	}

	@Bean
	@Order(8)
	public ApplicationRunner creatingImmutableCollections() {
		return args -> {
			Map<String, Integer> map = Collections.unmodifiableMap(
				new HashMap<String, Integer>() {{
					put("have", 1);
					put("the", 2);
					put("high", 3);
					put("ground", 4);
				}}
			);
		};
	}

	/**
	 * With this annotation you promise not to corrupt the input array type
	 * This illustrates the Java 9 factory methods List.of(...), Set.of(...), Map.of(...)
	 */
	@SafeVarargs
	public final <T> List<T> createImmutableList(T... elements) {
		return Arrays.stream(elements)
			.collect(
				Collectors.collectingAndThen(
					Collectors.toList(),
					Collections::unmodifiableList));
	}

	@Bean
	@Order(9)
	public ApplicationRunner implementingCollectorInterface() {
		return args -> {
			System.out.println(oddLengthStringSet("Hello", "as", "buddy"));
		};
	}

	public SortedSet<String> oddLengthStringSet(String... strings) {
		Collector<String, ?, SortedSet<String>> intoSet = Collector.of(
			TreeSet<String>::new,
			SortedSet<String>::add,
			(left, right) -> {
				left.addAll(right);
				return left;
			}
		);

		return Stream
			.of(strings)
			.filter(s -> s.length() % 2 != 0)
			.collect(intoSet);
	}


	@SafeVarargs
	public final <T> Set<T> createImmutableSet(T... elements) {
		return Arrays.stream(elements)
			.collect(
				Collectors.collectingAndThen(
					Collectors.toSet(),
					Collections::unmodifiableSet
				));
	}

	public List<String> defaultSort(List<String> strings) {
		Collections.sort(strings);
		return strings;
	}

	public List<String> defaultSortUsingStreams(List<String> strings) {
		return strings.stream().sorted().toList();
	}

	public List<String> lengthSortUsingSorted(List<String> strings) {
		return strings.stream().sorted((s1, s2) -> s1.length() - s2.length()).toList();
	}

	public List<String> lengthSortUsingComparator(List<String> strings) {
		return strings.stream().sorted(Comparator.comparingInt(String::length)).toList();
	}

	public List<String> lengthSortThenAlphaSort(List<String> strings) {
		return strings.stream().sorted(Comparator.comparing(String::length).thenComparing(Comparator.naturalOrder()))
				.toList();
	}

	public List<Golfer> sortByScoreThenLastThenFirst(List<Golfer> golfers) {
		return golfers.stream().sorted(Comparator.comparingInt(Golfer::getScore).thenComparing(Golfer::getLast)
				.thenComparing(Golfer::getFirst)).toList();
	}

}
