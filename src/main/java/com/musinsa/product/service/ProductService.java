package com.musinsa.product.service;

import com.musinsa.product.dao.ProductDao;
import com.musinsa.product.dto.CategoryMinMaxPriceBrand;
import com.musinsa.product.dto.CategoryMinMaxPriceBrand.BrandProductPrice;
import com.musinsa.product.dto.CategoryMinPriceBrandList;
import com.musinsa.product.dto.CheapestBrandProductList;
import com.musinsa.product.dto.CheapestBrandProductList.CategoryProductPrice;
import com.musinsa.product.dto.Product;
import com.musinsa.product.exception.CustomException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
  private final ProductDao productDao;

  public ProductService(ProductDao productDao) {
    this.productDao = productDao;
  }

  @Cacheable("categoryMinPriceBrandCache")
  public CategoryMinPriceBrandList getCategoriesPriceMinBrand() {
    final List<Product> minPricePerCategoryList;
    try {
      minPricePerCategoryList = productDao.selectMinPriceBrandPerCategory();
    } catch (Exception e) {
      throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while selectMinPriceBrandPerCategory from DB.", e);
    }

    final Set<String> categoryMinPriceDeduplicator = new HashSet<>();

    final List<Product> categoryMinPriceDeduplicatedList = minPricePerCategoryList.stream()
        .filter(minPricePerCategory -> categoryMinPriceDeduplicator.add(minPricePerCategory.getCategoryName() + minPricePerCategory.getPrice()))
        .collect(Collectors.toList());

    final BigDecimal totalPrice = categoryMinPriceDeduplicatedList.stream()
        .map(Product::getPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    return new CategoryMinPriceBrandList(categoryMinPriceDeduplicatedList, totalPrice);
  }

  @Cacheable("totalPriceMinBrandCache")
  public CheapestBrandProductList getTotalPriceMinBrand() {
    final List<Product> cheapestBrandProducts;
    try {
      cheapestBrandProducts = productDao.selectCheapestBrandProducts();
    } catch (Exception e) {
      throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while selectCheapestBrandProducts from DB.", e);
    }

    final String cheapestBrandName = cheapestBrandProducts.get(0).getBrandName();

    final List<CategoryProductPrice> categoryProductPriceList = new LinkedList<>();
    BigDecimal totalPrice = BigDecimal.ZERO;
    for (Product product : cheapestBrandProducts) {
      categoryProductPriceList.add(new CategoryProductPrice(product.getCategoryName(), product.getPrice()));
      totalPrice = totalPrice.add(product.getPrice());
    }

    return new CheapestBrandProductList(cheapestBrandName, categoryProductPriceList, totalPrice);
  }

  @Cacheable("categoryPriceMinMaxBrand")
  public CategoryMinMaxPriceBrand getCategoryPriceMinMaxBrand(String categoryName) {
    final Product minPriceProduct;
    try {
       minPriceProduct = productDao.selectCategoryMinPrice(categoryName);
    } catch (Exception e) {
      throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while selectCategoryMinPrice from DB.", e);
    }

    final Product maxPriceProduct;
    try {
      maxPriceProduct = productDao.selectCategoryMaxPrice(categoryName);
    } catch (Exception e) {
      throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while selectCategoryMaxPrice from DB.", e);
    }

    return new CategoryMinMaxPriceBrand(categoryName,
        new BrandProductPrice(minPriceProduct.getBrandName(), minPriceProduct.getPrice()),
        new BrandProductPrice(maxPriceProduct.getBrandName(), maxPriceProduct.getPrice()));
  }

  @CacheEvict(value = {"categoryMinPriceBrandCache", "totalPriceMinBrandCache", "categoryPriceMinMaxBrand"}, allEntries = true)
  public void createProduct(Product product) {
    try {
      productDao.insert(product);
    } catch (Exception e) {
      throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while insert product to DB.", e);
    }
  }

  @CacheEvict(value = {"categoryMinPriceBrandCache", "totalPriceMinBrandCache", "categoryPriceMinMaxBrand"}, allEntries = true)
  public void updateProduct(String category, String brand, Product product) {
    try {
      productDao.updateProductByCategoryAndBrand(category, brand, product);
    } catch (Exception e) {
      throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while update product from DB.", e);
    }
  }

  @CacheEvict(value = {"categoryMinPriceBrandCache", "totalPriceMinBrandCache", "categoryPriceMinMaxBrand"}, allEntries = true)
  public void deleteProduct(String category, String brand) {
    try {
      productDao.deleteProductByCategoryAndBrand(category, brand);
    } catch (Exception e) {
      throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while delete product from DB.", e);
    }
  }
}
