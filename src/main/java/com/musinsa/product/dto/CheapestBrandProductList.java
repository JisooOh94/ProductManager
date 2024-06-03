package com.musinsa.product.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.Value;

@Value
public class CheapestBrandProductList {
  private final String cheapestBrandName;
  private final List<CategoryProductPrice> categoryProductPriceList;
  private final BigDecimal priceTotal;

  @Value
  public static class CategoryProductPrice {
    private final String categoryName;
    private final BigDecimal price;
  }
}
