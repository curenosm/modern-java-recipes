package com.curenosm.chapter10;

import java.net.http.HttpResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Chapter10Application {

  public static void main (String[] args) {
    SpringApplication.run(Chapter10Application.class, args);

    ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080", String.class);

  }

}
