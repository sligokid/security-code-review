package com.land.registry.auth;

import java.util.TimeZone;
import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
@EntityScan(basePackageClasses = {
    // JPA 2.1 converters to turn Java 8 Date/Time fields in the domain models into SQL types
    MainApplication.class, Jsr310JpaConverters.class})

public class MainApplication {

  @PostConstruct
  void init() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
  }

  public static void main(final String[] args) {
    SpringApplication.run(MainApplication.class, args);
  }

}
