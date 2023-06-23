package com.curenosm.chapter3;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class Chapter3Application {

	public static void main(String[] args) {
		SpringApplication.run(Chapter3Application.class, args);
	}

	public static boolean isPalindrome(String str) {
		StringBuilder sb = new StringBuilder();
		for (char c : str.toCharArray()) {
			if (Character.isLetterOrDigit(c)) {
				sb.append(c);
			}
		}
		String forward = sb.toString().toLowerCase();
		String backward = sb.reverse().toString().toLowerCase();
		return forward.equals(backward);
	}

	public static boolean isPalindromeFunctional(String str) {
		StringBuilder forward = str.toLowerCase().codePoints().filter(Character::isLetterOrDigit)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append);

		return forward.toString().equals(new StringBuilder(forward).reverse().toString());
	}

	public static int sumDoublesDivisibleBy3(int start, int end) {
		return IntStream.rangeClosed(start, end).peek(n -> log.info("original: {}", n)).map(n -> 2 * n)
				.peek(n -> log.info("doubled: {}", n)).filter(n -> n % 3 == 0).peek(n -> log.info("filtered: {}", n))
				.sum();
	}

	public static boolean isPrime(int num) {
		int limit = (int) (Math.sqrt(num));
		return num == 2 || (num > 1 && IntStream.rangeClosed(2, limit).noneMatch(divisor -> num % divisor == 0));
	}

	@Bean
	public ApplicationRunner streams() {
		return args -> {
			// Recipe 3.1 Create a stream
			String names = Stream.of("Gomez", "Morticia", "Wednesday", "Pugsley").collect(Collectors.joining(","));
			System.out.println(names);

			String[] namesArr = { "Herman", "Lily", "Eddie", "Marilyn", "Grandpa" };
			names = Arrays.stream(namesArr).collect(Collectors.joining(","));
			System.out.println(names);

			List<BigDecimal> nums = Stream.iterate(BigDecimal.ONE, n -> n.add(BigDecimal.ONE)).limit(10)
					.collect(Collectors.toList());

			System.out.println(nums);
			Stream.iterate(LocalDate.now(), ld -> ld.plusDays(1L)).limit(10).forEach(System.out::println);

			Stream.generate(Math::random).limit(10).forEach(System.out::println);

			List<String> bradyBunch = Arrays.asList("Greg", "Marcia", "Peter", "Jan", "Bobby", "Cindy");
			names = bradyBunch.stream().collect(Collectors.joining(","));
			System.out.println(names);

			// IntStream, DoubleStream, LongStream

			IntStream.range(0, bradyBunch.size()).forEach(System.out::println);
			assert IntStream.rangeClosed(1, 100).count() == 100;

			LongStream.range(0, bradyBunch.size()).forEach(System.out::println);
			assert LongStream.rangeClosed(1, 100).count() == 100;

			List<Integer> ints = IntStream.range(10, 15).boxed().toList();
			System.out.println(ints);

			List<Long> longs = LongStream.rangeClosed(10, 15).boxed() // Necessary for
																		// collectors to
																		// convert
																		// primitives to
																		// List<T>
					.toList();
			System.out.println(longs);

		};
	}

	@Bean
	public ApplicationRunner generatingCollections() {
		return args -> {
			// Recipe 3.2 Generating collections from a primitive stream

			List<Integer> ints = IntStream.range(10, 15).boxed().toList();
			System.out.println(ints);

			List<Long> longs = LongStream.rangeClosed(10, 15).mapToObj(Long::valueOf).toList();
			System.out.println(longs);

			List<Integer> ints2 = IntStream.range(1, 34).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

			double[] doubleArray = DoubleStream.iterate(0.0, v -> Math.pow(v + 1, 2)).limit(100).toArray();

			int[] intArr = IntStream.iterate(0, v -> v + 2).limit(100).toArray();

			System.out.println(Arrays.toString(doubleArray));

		};
	}

	@Bean
	public ApplicationRunner reductionOperations() {
		return args -> {
			// Recipe 3.3 Reduce

			OptionalDouble avg = IntStream.range(10, 15).average();

			long count = IntStream.range(1, 34).count();

			OptionalDouble max = DoubleStream.iterate(0.0, v -> Math.pow(v + 1, 2)).limit(100).max();

			OptionalInt intArr = IntStream.iterate(0, v -> v + 2).limit(100).min();

			long suma = IntStream.range(1, 34).sum();

			double res = DoubleStream.iterate(0.0, v -> Math.pow(v + 1, 2)).limit(100).reduce(0, Double::sum);

			String[] strings = "this is an arrayt of strings".split(" ");
			int sum1 = Arrays.stream(strings).mapToInt(String::length).sum();

			int sum2 = IntStream.rangeClosed(1, 10).reduce(Integer::sum).orElse(0);

			int sum = IntStream.rangeClosed(1, 10).reduce(0, (x, y) -> {
				System.out.printf("x=%d, y=%d%n", x, y);
				return x + 2 * y;
			});

			// String Concat is inefficient
			String s = Stream.of("this", "is", "a", "list")
					.collect(() -> new StringBuilder(), (sb, str) -> sb.append(str), (sb1, sb2) -> sb1.append(sb2))
					.toString();

			s = Stream.of("this", "is", "a", "list").collect(StringBuilder::new, // Supplier
					StringBuilder::append, // Adding one
					StringBuilder::append // Combine two results
			).toString();

			s = Stream.of("this", "is", "a", "list").collect(Collectors.joining("_"));

			List<Book> books = Arrays.asList(new Book(1, "Title"), new Book(2, "Title 2"), new Book(3, "Title 3"));

			HashMap<Integer, Book> bookMap = books.stream().reduce(new HashMap<Integer, Book>(), (map, book) -> {
				map.put(book.getId(), book);
				return map;
			}, (map1, map2) -> {
				map1.putAll(map2);
				return map1;
			});

			bookMap.forEach((k, v) -> System.out.printf("%d%n:%s", k, v));
		};
	}

	@Bean
	public ApplicationRunner checkSortingUsingReduce() {
		return args -> {
			BigDecimal total = Stream.iterate(BigDecimal.ONE, n -> n.add(BigDecimal.ONE)).limit(10)
					.reduce(BigDecimal.ZERO, (acc, val) -> acc.add(val));
			System.out.printf("The total is %s", total);

			List<String> strings = Arrays.asList("Hello", "Arigato", "World");
			Set<String> sorted = strings.stream()
					.sorted(Comparator.comparingInt(String::length).thenComparing(Comparator.reverseOrder()))
					.collect(Collectors.toCollection(LinkedHashSet::new));

			System.out.println(sorted);
			strings.stream().reduce((prev, cur) -> {
				return cur;
			});

		};
	}

	@Bean
	public ApplicationRunner convertingStringsToStreamsAndBack() {
		return args -> {
			int res = sumDoublesDivisibleBy3(1, 10000);
			// Palindrome methods
		};
	}

	@Bean
	public ApplicationRunner countingElements() {
		return args -> {
			int res = sumDoublesDivisibleBy3(1, 10000);
			long count = Stream.of(1, 2, 5, 21, 1, 0, 21, 2).count();
			System.out.printf("There are %d elements in the stream%n", count);

			count = Stream.of(1, 2, 5, 21, 1, 0, 21, 2).collect(Collectors.counting()); // Downstream
																						// collectors
																						// for
																						// downstream
																						// post-processing

			System.out.printf("There are %d elements in the stream%n", count);
		};
	}

	@Bean
	public ApplicationRunner summaryStatistics() {
		return args -> {
			DoubleSummaryStatistics stats = DoubleStream.generate(Math::random).limit(1_000_000) // Java
																									// 7+
																									// allow
																									// underscore
																									// for
																									// numeric
																									// literals
					.summaryStatistics();
			System.out.println(stats);
			System.out.println(stats.getMax());
			System.out.println(stats.getMin());
			System.out.println(stats.getAverage());
			System.out.println(stats.getCount());
			System.out.println(stats.getSum());

			List<Team> teams = List.of(new Team(1, "Dallas", 23.53));

			DoubleSummaryStatistics teamStats = teams.stream().mapToDouble(Team::getSalary).collect(
					DoubleSummaryStatistics::new, DoubleSummaryStatistics::accept, DoubleSummaryStatistics::combine);

			teams.stream().collect(Collectors.summarizingDouble(Team::getSalary));
		};
	}

	@Bean
	public ApplicationRunner findingElements() {
		return args -> {
			Optional<Integer> firstEven = Stream.of(3, 1, 2, 3, 5, 7, 8).filter(n -> n % 2 == 0).findFirst();
			System.out.println(firstEven);

			Optional<Integer> firstEvenGT10 = Stream.of(3, 1, 5, 11, 21, 9).filter(n -> n > 10).filter(n -> n % 2 == 0)
					.findFirst();
			System.out.println(firstEvenGT10);

			List<String> wordList = Arrays.asList("this", "is", "a", "stream");
			Set<String> words = new HashSet<>(wordList);
			Set<String> words2 = new HashSet<>(words);

			// Now add and remove enough elements to force a rehash
			IntStream.rangeClosed(0, 50).forEachOrdered(i -> words2.add(String.valueOf(i)));

			words2.retainAll(wordList);
			System.out.println(words.equals(words2));
			System.out.println("Before: " + words);
			System.out.println("After: " + words2);

			Optional<Integer> any = Stream.of(3, 1, 4, 1, 5, 9, 2, 6, 5).unordered().parallel().map(this::delay)
					.findAny();
			System.out.println("Any: " + any);

			Optional<Integer> sequential = Stream.of(3, 1, 4, 1, 5, 9, 2, 6, 5).unordered().map(this::delay).findAny();
			System.out.println("Sequential: " + sequential);
		};
	}

	@Bean
	public ApplicationRunner predicates() {
		return args -> {
			BigInteger prime = new BigInteger("1092810927390121");
			boolean probablePrime = prime.isProbablePrime(3); // 1 - 1/2^(certainty)

		};
	}

	@Bean
	public ApplicationRunner mapAndFlatMap() {
		return args -> {
			Customer c1 = new Customer("Sheridan");
			Customer c2 = new Customer("Ivanova");
			Customer c3 = new Customer("Garibaldi");
			c1.addOrder(new Order(1)).addOrder(new Order(2)).addOrder(new Order(2));

			c2.addOrder(new Order(4)).addOrder(new Order(5));
			List<Customer> customers = Arrays.asList(c1, c2, c3);
			customers.stream().map(Customer::getName).forEach(System.out::println);

			customers.stream().map(Customer::getOrders).forEach(System.out::println);

			customers.stream().map(customer -> customer.getOrders().stream()).forEach(System.out::println);

			// Demonstration of flatMap
			customers.stream().flatMap(customer -> customer.getOrders().stream()).forEach(System.out::println);
		};
	}

	/**
	 * Works if the number of streams is small otherwise use flatMap
	 */
	@Bean
	public ApplicationRunner concatenatingStreams() {
		return args -> {
			Stream<String> first = Stream.of("a", "b", "c").parallel();
			Stream<String> second = Stream.of("X", "Y", "Z");
			List<String> strings = Stream.concat(first, second).toList();
			List<String> stringList = Arrays.asList("a", "b", "c", "X", "Y", "Z");
			System.out.println(strings);
			System.out.println(stringList);
			// assert stringList.equals(strings);
		};
	}

	@Bean
	public ApplicationRunner lazyStreams() {
		return args -> {
			OptionalInt firstEvenDoubleDivBy3 = IntStream.range(100, 200).map(this::multByTwo).filter(this::divByThree)
					.findFirst();
		};
	}

	public int multByTwo(int n) {
		System.out.printf("Inside multByTwo with arg %d%n", n);
		return 2 * n;
	}

	public boolean divByThree(int n) {
		System.out.printf("Inside divByThree with arg %d%n", n);
		return n % 3 == 0;
	}

	public Integer delay(Integer n) {
		try {
			Thread.sleep((long) (Math.random() * 100));
		}
		catch (InterruptedException ignored) {

		}
		return n;
	}

	@Getter
	@Setter
	@AllArgsConstructor
	public static class Book {

		private Integer id;

		private String title;

	}

}
