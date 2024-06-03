package com.musinsa.product.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;
import lombok.Value;

@Value
public class CategoryMinPriceBrandListForTest {
  private final List<ProductForTest> categoryMinPriceBrands;
  private final BigDecimal totalPrice;

  @JsonCreator
  public CategoryMinPriceBrandListForTest(@JsonProperty("categoryMinPriceBrands") List<ProductForTest> categoryMinPriceBrands,
      @JsonProperty("totalPrice") BigDecimal totalPrice) {
    this.categoryMinPriceBrands = categoryMinPriceBrands;
    this.totalPrice = totalPrice;
  }
}
