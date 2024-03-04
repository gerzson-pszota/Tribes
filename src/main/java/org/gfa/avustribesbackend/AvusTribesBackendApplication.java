package org.gfa.avustribesbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AvusTribesBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(AvusTribesBackendApplication.class, args);
  }
}
