package com.musinsa.product.dao;

import com.musinsa.product.dto.Brand;
import com.musinsa.product.mapper.BrandMapper;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class BrandDao {
  private final BrandMapper brandMapper;

  public BrandDao(BrandMapper brandMapper) {
    this.brandMapper = brandMapper;
  }

  public void insertBrand(String brandName) {
    brandMapper.insert(brandName);
  }

  public void updateBrand(String brandName, String newBrandName) {
    brandMapper.update(brandName, newBrandName);
  }

  public void deleteBrand(String brandName) {
    brandMapper.delete(brandName);
  }

  public List<Brand> selectAll() {
    return brandMapper.selectAll();
  }
}
