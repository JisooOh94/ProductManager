package com.musinsa.product.service;

import com.musinsa.product.dao.BrandDao;
import com.musinsa.product.dao.ProductDao;
import com.musinsa.product.dto.Brand;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BrandService {

  private final BrandDao brandDao;
  private final ProductDao productDao;

  public BrandService(BrandDao brandDao, ProductDao productDao) {
    this.brandDao = brandDao;
    this.productDao = productDao;
  }

  public void createBrand(String brandName) {
    brandDao.insertBrand(brandName);
  }

  @Transactional
  @CacheEvict(value = {"categoryMinPriceBrandCache", "totalPriceMinBrandCache", "categoryPriceMinMaxBrand"}, allEntries = true)
  public void updateBrand(String brandName, String newBrandName) {
    brandDao.updateBrand(brandName, newBrandName);
    productDao.updateBrandName(brandName, newBrandName);
  }

  @Transactional
  @CacheEvict(value = {"categoryMinPriceBrandCache", "totalPriceMinBrandCache", "categoryPriceMinMaxBrand"}, allEntries = true)
  public void deleteBrand(String brandName) {
    brandDao.deleteBrand(brandName);
    productDao.deleteBrandProducts(brandName);
  }

  public List<String> getAllBrands() {
    final List<Brand> brands = brandDao.selectAll();
    return brands.stream().map(Brand::getBrandName).collect(Collectors.toList());
  }
}
