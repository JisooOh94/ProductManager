package com.musinsa.product.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Value;

@Value
public class CategoryMinMaxPriceBrandForTest {
  private final String categoryName;
  private final BrandProductPrice minProductPrice;
  private final BrandProductPrice maxProductPrice;

  @JsonCreator
  public CategoryMinMaxPriceBrandForTest(@JsonProperty("categoryName") String categoryName, @JsonProperty("minProductPrice") BrandProductPrice minProductPrice,
      @JsonProperty("maxProductPrice") BrandProductPrice maxProductPrice) {
    this.categoryName = categoryName;
    this.minProductPrice = minProductPrice;
    this.maxProductPrice = maxProductPrice;
  }

  @Value
  public static class BrandProductPrice {
    private final String brandName;
    private final BigDecimal price;

    @JsonCreator
    public BrandProductPrice(@JsonProperty("brandName") String brandName, @JsonProperty("price") BigDecimal price) {
      this.brandName = brandName;
      this.price = price;
    }
  }
}
