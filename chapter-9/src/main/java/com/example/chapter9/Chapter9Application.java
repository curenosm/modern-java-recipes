package com.example.chapter9;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

@Slf4j
@SpringBootApplication
public class Chapter9Application {

  public static void main (String[] args) {
    SpringApplication.run(Chapter9Application.class, args);
  }

  /*
   * Working with parallel streams is beneficial in these cases:
   *   - A large amount of data is processed.
   *   - A time-consuming process for each element.
   *   - A source of data that is easy to divide, and.
   *   - Operations that are stateless and associative.
   */

  public static int doubleIt(int n) {
    try {
      Thread.sleep(100);
    } catch (InterruptedException ignore) {}
    return 2 * n;
  }


  @Bean
  @Order(1)
  public ApplicationRunner timingExample() {
    return args -> {

      System.out.println(Runtime.getRuntime().availableProcessors());

      Instant before = Instant.now();
      int total = IntStream.of(3, 2, 4, 1, 6, 9).map(Chapter9Application::doubleIt).sum();
      Instant after = Instant.now();
      Duration duration = Duration.between(before, after);
      System.out.println("Total of doubles = " + total);
      System.out.println("time = " + duration.toMillis() + "ms");

      log.info("Parallelization:");
      before = Instant.now();
      total = IntStream.of(3, 2, 4, 1, 6, 9)
        .parallel()
        .map(Chapter9Application::doubleIt)
        .sum();
      after = Instant.now();
      duration = Duration.between(before, after);
      System.out.println("Total of doubles = " + total);
      System.out.println("time = " + duration.toMillis() + "ms");

    };
  }

  @Bean
  @Order(3)
  public ApplicationRunner changingThePoolSize() {
    return args -> {
      System.out.println(Runtime.getRuntime().availableProcessors());
      System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "20");
      long total = LongStream.rangeClosed(1, 3_000).parallel().sum();
      int poolSize = ForkJoinPool.commonPool().getPoolSize();
      System.out.println("Pool size: " + poolSize);


      // Create your own forkjoinpool

      ForkJoinPool pool = new ForkJoinPool(15);
      ForkJoinTask<Long> task = pool.submit(() -> LongStream.rangeClosed(1, 3_000).parallel().sum());
      try {
        total = task.get();
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      } finally {
        pool.shutdown();
      }

      poolSize = pool.getPoolSize();
      System.out.println("Pool size: " + poolSize);

    };
  }


  @Bean
  @Order(4)
  public ApplicationRunner asynchronousOperation() {
    return args -> {
      ExecutorService service = Executors.newCachedThreadPool();
      Future<String> future = service.submit(new Callable<String>() {
        @Override
        public String call () throws Exception {
          Thread.sleep(100);
          return "Hello, World!";
        }
      });

      System.out.println("Processing");
      getIfNotCancelled(future);

      future = service.submit(() -> {
        Thread.sleep(10);
        return "Hello, World!";
      });

      System.out.println("More processing...");
      while(!future.isDone()) {
        System.out.println("Waiting...");
      }

      future = service.submit(() -> {
        Thread.sleep(10);
        return "Hello, World!";
      });

      future.cancel(true);

      System.out.println("Even more processing...");
      getIfNotCancelled(future);

    };
  }

  public void getIfNotCancelled(Future<String> future) {
    try {
      if (!future.isCancelled()) {
        System.out.println(future.get());
      } else {
        System.out.println("Cancelled");
      }
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
  }

  /*
  Since CompletableFuture extends of Future, all the previous methods apply as well-
   */
  @Bean
  @Order(5)
  public ApplicationRunner competingACompletableFuture() {
    return args -> {

    };
  }

  public CompletableFuture<Product> getProduct(int id) {
    try {
      Product product = getLocal(id);
      if (product != null) {
        return CompletableFuture.completedFuture(product);
      } else {
        CompletableFuture<Product> future = new CompletableFuture<>();
        Product p = getRemote(id);
        cache.put(id, p);
        future.complete(p);
        return future;
      }
    } catch (Exception e) {
      CompletableFuture<Product> future = new CompletableFuture<>();
      future.completeExceptionally(e);
      return future;
    }
  }

  public CompletableFuture<Product> getProductAsync(int id) {
    try {
      Product product = getLocal(id);
      if (product != null) {
        return CompletableFuture.completedFuture(product);
      } else {
        log.info("getRemote with id=" + id);
        return CompletableFuture.supplyAsync(() -> {
          Product p = getRemote(id);
          cache.put(id, p);
          return p;
        });
      }
    } catch (Exception e) {
      log.info("exception thrown");
      CompletableFuture<Product> future = new CompletableFuture<>();
      future.completeExceptionally(e);
      return future;
    }
  }

  Map<Integer, Product> cache = new HashMap<>();

  // Data holder, Java 17+
  record Product(int id, String name) {};

  private Product getLocal(int id) {
    return cache.get(id);
  }

  private Product getRemote(int id) {

    try {
      Thread.sleep(100);
      if (id == 666) {
        throw new RuntimeException("Evil request");
      }
    } catch( InterruptedException ignored) {}

    return new Product(id, "name");
  }


  @Bean
  @Order(6)
  public ApplicationRunner coordinatingCompletableFutures() {
    return args -> {
      CompletableFuture.supplyAsync(this::sleepThenReturnString)
        .thenApply(Integer::parseInt)
        .thenApply(x -> 2 * x)
        .thenAccept(System.out::println)
        .join();

      System.out.println("Running");

      boolean b = ForkJoinPool.commonPool().awaitQuiescence(1000, TimeUnit.MILLISECONDS);
      try {
        ExecutorService service = Executors.newFixedThreadPool(4);
        CompletableFuture.supplyAsync(this::sleepThenReturnString, service)
          .thenApply(Integer::parseInt)
          .thenApply(x -> 2 * x)
          .thenAccept(System.out::println)
          .join();
      } catch (Exception e) {}
    };
  }

  private String sleepThenReturnString() {
    try {
      Thread.sleep(100);
    } catch (InterruptedException ignored) {}
    return "42";
  }

  @Bean
  @Order(7)
  public ApplicationRunner coordinatingCompletablePt2() {
    return args -> {

    };
  }


}
