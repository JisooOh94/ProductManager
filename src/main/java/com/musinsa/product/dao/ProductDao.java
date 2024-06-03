package com.musinsa.product.dao;

import com.musinsa.product.dto.Product;
import com.musinsa.product.mapper.ProductMapper;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ProductDao {
  private final ProductMapper productMapper;

  public ProductDao(ProductMapper productMapper) {
    this.productMapper = productMapper;
  }

  public List<Product> selectMinPriceBrandPerCategory() {
    return productMapper.selectMinPriceBrandPerCategory();
  }

  public List<Product> selectCheapestBrandProducts() {
    return productMapper.selectCheapestBrandProducts();
  }

  public Product selectCategoryMinPrice(String categoryName) {
    return productMapper.selectCategoryMinPrice(categoryName);
  }

  public Product selectCategoryMaxPrice(String categoryName) {
    return productMapper.selectCategoryMaxPrice(categoryName);
  }

  public void updateBrandName(String brandName, String newBrandName) {
    productMapper.updateBrandName(brandName, newBrandName);
  }

  public void deleteBrandProducts(String brandName) {
    productMapper.deleteBrandProducts(brandName);
  }

  public void insert(Product product) {
    productMapper.insert(product);
  }

  public void updateProductByCategoryAndBrand(String category, String brand, Product product) {
    productMapper.updateProductByCategoryAndBrand(category, brand, product);
  }

  public void deleteProductByCategoryAndBrand(String category, String brand) {
    productMapper.deleteProductByCategoryAndBrand(category, brand);
  }
}
