package com.musinsa.product.mapper;

import com.musinsa.product.dto.Product;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductMapper {
  List<Product> selectMinPriceBrandPerCategory();

  List<Product> selectCheapestBrandProducts();

  Product selectCategoryMinPrice(@Param("categoryName") String categoryName);

  Product selectCategoryMaxPrice(@Param("categoryName") String categoryName);

  void updateBrandName(@Param("brandName") String brandName, @Param("newBrandName") String newBrandName);

  void deleteBrandProducts(@Param("brandName") String brandName);

  void insert(Product product);

  void updateProductByCategoryAndBrand(@Param("category") String category, @Param("brand") String brand, @Param("product") Product product);

  void deleteProductByCategoryAndBrand(@Param("category") String category, @Param("brand") String brand);
}
