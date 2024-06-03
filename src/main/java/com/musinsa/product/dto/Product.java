package com.musinsa.product.dto;

import java.math.BigDecimal;
import lombok.Value;

@Value
public class Product {
  private final String categoryName;
  private final String brandName;
  private final BigDecimal price;
}
