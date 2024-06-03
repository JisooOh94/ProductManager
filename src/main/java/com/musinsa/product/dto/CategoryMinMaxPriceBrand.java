package com.musinsa.product.dto;

import java.math.BigDecimal;
import lombok.Value;

@Value
public class CategoryMinMaxPriceBrand {
  private final String categoryName;
  private final BrandProductPrice minProductPrice;
  private final BrandProductPrice maxProductPrice;

  @Value
  public static class BrandProductPrice {
    private final String brandName;
    private final BigDecimal price;
  }
}
