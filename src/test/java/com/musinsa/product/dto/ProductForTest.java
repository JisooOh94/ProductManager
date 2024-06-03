package com.musinsa.product.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Value;

@Value
public class ProductForTest {
  private final String categoryName;
  private final String brandName;
  private final BigDecimal price;

  @JsonCreator
  public ProductForTest(@JsonProperty("categoryName") String categoryName, @JsonProperty("brandName") String brandName, @JsonProperty("price") BigDecimal price) {
    this.categoryName = categoryName;
    this.brandName = brandName;
    this.price = price;
  }
}
