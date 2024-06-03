package com.musinsa.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableCaching(proxyTargetClass = true)
@EnableTransactionManagement(proxyTargetClass = true)
public class MusinsaProductManagerApplication {
  public static void main(String[] args) {
    SpringApplication.run(MusinsaProductManagerApplication.class, args);
  }
}
