package com.musinsa.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import lombok.Value;

@Value
public class Brand {
  @JsonIgnore
  private final int brandId;
  private final String brandName;
  @JsonIgnore
  private final BigDecimal priceSum;
}
