package com.curenosm.chapter4;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Chapter4Application {

	public static void main(String[] args) {
		SpringApplication.run(Chapter4Application.class, args);
	}

	/**
	 * The {@code sorted} method on Stream produces a new, sorted stream using
	 * the natural ordering for the class.
	 *
	 * @return Lambda to be executed immediately after our application context has been created.
	 */
	@Bean
	public CommandLineRunner sortingUsingComparator() {
		return args -> {
			List<String> sampleStrings = Arrays.asList("this", "is", "a", "list", "of", "strings");
		};
	}

	public List<String> defaultSort(List<String> strings) {
		Collections.sort(strings);
		return strings;
	}

	public List<String> defaultSortUsingStreams(List<String> strings) {
		return strings
			.stream()
			.sorted()
			.toList();
	}

	public List<String> lengthSortUsingSorted(List<String> strings) {
		return strings
			.stream()
			.sorted((s1, s2) -> s1.length() - s2.length())
			.toList();
	}

	public List<String> lengthSortUsingComparator(List<String> strings) {
		return strings
			.stream()
			.sorted(Comparator.comparingInt(String::length))
			.toList();
	}

}
