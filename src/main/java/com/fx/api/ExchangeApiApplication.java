package com.fx.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExchangeApiApplication {
  private static final Logger logger = LoggerFactory.getLogger(ExchangeApiApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(ExchangeApiApplication.class, args);
    logger.info("Application Started");
  }
}
