package com.musinsa.product.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.Value;

@Value
public class CategoryMinPriceBrandList {
  private final List<Product> categoryMinPriceBrands;
  private final BigDecimal totalPrice;
}
