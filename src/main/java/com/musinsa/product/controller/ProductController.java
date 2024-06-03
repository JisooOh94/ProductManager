package com.musinsa.product.controller;

import com.musinsa.product.dto.CategoryMinMaxPriceBrand;
import com.musinsa.product.dto.CategoryMinPriceBrandList;
import com.musinsa.product.dto.CheapestBrandProductList;
import com.musinsa.product.dto.Product;
import com.musinsa.product.service.ProductService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

  @Autowired
  private ProductService productService;

  @GetMapping("/categories/price/min/brand")
  public CategoryMinPriceBrandList getCategoriesPriceMinBrand() {
    return productService.getCategoriesPriceMinBrand();
  }


  @GetMapping("/total-price/min/brand")
  public CheapestBrandProductList getTotalPriceMinBrand() {
    return productService.getTotalPriceMinBrand();
  }

  @GetMapping("/{category}/price/min-max/brand")
  public CategoryMinMaxPriceBrand getCategoryPriceMinMaxBrand(@PathVariable String category) {
    return productService.getCategoryPriceMinMaxBrand(category);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void createProduct(@RequestBody Product product) {
    productService.createProduct(product);
  }

  @PutMapping("/{category}/{brand}")
  @ResponseStatus(HttpStatus.OK)
  public void updateProduct(@PathVariable String category, @PathVariable String brand, @RequestBody Product product) {
    productService.updateProduct(category, brand, product);
  }

  @DeleteMapping("/{category}/{brand}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteProduct(@PathVariable String category, @PathVariable String brand) {
    productService.deleteProduct(category, brand);
  }
}
