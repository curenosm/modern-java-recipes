package com.curenosm.chapter2;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class Chapter2Application {

  public static void main (String[] args) {
    SpringApplication.run(Chapter2Application.class, args);
  }


  @Bean
  public ApplicationRunner staticMethodsInInterfaces () {
    return args -> {
      // For each es un consumer
      //
    };
  }

}

class CustomConsumer implements Consumer {

  @Override
  public void accept (Object o) {

  }

}