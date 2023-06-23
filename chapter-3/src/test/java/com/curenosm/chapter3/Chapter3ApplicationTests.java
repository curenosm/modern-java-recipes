package com.curenosm.chapter3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

// @SpringBootTest
class Chapter3ApplicationTests {

	@Test
	void debuggingStreamsWithPeek() throws Exception {
		assertEquals(1554, Chapter3Application.sumDoublesDivisibleBy3(100, 120));
	}

	@Test
	void testingFunctionalStylePalindromeChecker() throws Exception {
		assertTrue(Stream
				.of("anitalavalatina", "Madam, in Eden, I'm Adam", "Go hang a salami; I'm a lasagna hog",
						"Flee to me, remote elf", "A santa pets rats as Pat taps a star step at NASA")
				.allMatch(Chapter3Application::isPalindromeFunctional));
	}

	@Test
	public void testIsPrimeUsingAllMatch() throws Exception {
		assertTrue(IntStream.of(2, 3, 5, 7, 11, 13, 17, 19).allMatch(Chapter3Application::isPrime));
	}

	@Test
	public void testIfPrimeWithComposites() throws Exception {
		assertFalse(IntStream.of(8, 12, 16, 24, 66).anyMatch(Chapter3Application::isPrime));
	}

	@Test
	public void testEmptyStreams() throws Exception {
		assertTrue(Stream.empty().allMatch(e -> false));
		assertTrue(Stream.empty().noneMatch(e -> true));
		assertFalse(Stream.empty().anyMatch(e -> true));
	}

	/**
	 * BE CAREFUL NESTING STREAMS THIS WAY!!! Accessing an element of a deeply
	 * concatenated stream can result in deep call chains, or even StackOverflowException,
	 * essentially we're building a binary tree of streams.
	 * @throws Exception
	 */
	@Test
	public void concatenateMultipleStreams() throws Exception {
		Stream<String> first = Stream.of("a", "b", "c").parallel();
		Stream<String> second = Stream.of("X", "Y", "Z").parallel();
		Stream<String> third = Stream.of("alpha", "beta", "gamma").parallel();

		List<String> strings = Stream.concat(Stream.concat(first, second), third).toList();
		List<String> stringList = Arrays.asList("a", "b", "c", "X", "Y", "Z", "alpha", "beta", "gamma");

		assertEquals(strings, stringList);
	}

	@Test
	void concatenateMultipleStreamsReduce() {
		// Using reduce
		Stream<String> first = Stream.of("a", "b", "c").parallel();
		Stream<String> second = Stream.of("X", "Y", "Z").parallel();
		Stream<String> third = Stream.of("alpha", "beta", "gamma").parallel();

		Stream<String> fourth = Stream.empty();
		List<String> strings = Stream.of(first, second, third, fourth).reduce(Stream.empty(), Stream::concat).toList();
		List<String> stringList = Arrays.asList("a", "b", "c", "X", "Y", "Z", "alpha", "beta", "gamma");

		assertEquals(strings, stringList);
	}

	@Test
	void concatParallel() {
		// Using reduce
		Stream<String> first = Stream.of("a", "b", "c").parallel();
		Stream<String> second = Stream.of("X", "Y", "Z").parallel();
		Stream<String> third = Stream.of("alpha", "beta", "gamma").parallel();

		Stream<String> fourth = Stream.empty();
		Stream<String> total = Stream.concat(first, Stream.concat(second, third));
		assertTrue(total.isParallel());

	}

	@Test
	void flatMapNotParallel() {
		Stream<String> first = Stream.of("a", "b", "c").parallel();
		Stream<String> second = Stream.of("X", "Y", "Z").parallel();
		Stream<String> third = Stream.of("alpha", "beta", "gamma").parallel();
		Stream<String> fourth = Stream.empty();

		Stream<String> total = Stream.of(first, second, third, fourth).flatMap(Function.identity());

		assertFalse(total.isParallel());
		total = total.parallel();
		assert total.isParallel();
	}

	@Test
	public void someTests() {
		String s1 = "This is a string literal";
		String s2 = "This is different obviously";
		String s3 = "This is a string literal";

		assert s1 != s2;
		assert s2 != s3;
		assert s1 == s3;
	}

}
