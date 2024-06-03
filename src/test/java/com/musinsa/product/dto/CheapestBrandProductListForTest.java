package com.musinsa.product.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;
import lombok.Value;

@Value
public class CheapestBrandProductListForTest {
  private final String cheapestBrandName;
  private final List<CategoryProductPrice> categoryProductPriceList;
  private final BigDecimal priceTotal;

  @JsonCreator
  public CheapestBrandProductListForTest(@JsonProperty("cheapestBrandName") String cheapestBrandName, @JsonProperty("categoryProductPriceList") List<CategoryProductPrice> categoryProductPriceList,
      @JsonProperty("priceTotal") BigDecimal priceTotal) {
    this.cheapestBrandName = cheapestBrandName;
    this.categoryProductPriceList = categoryProductPriceList;
    this.priceTotal = priceTotal;
  }

  @Value
  public static class CategoryProductPrice {
    private final String categoryName;
    private final BigDecimal price;

    @JsonCreator
    public CategoryProductPrice(@JsonProperty("categoryName") String categoryName, @JsonProperty("price") BigDecimal price) {
      this.categoryName = categoryName;
      this.price = price;
    }
  }
}
