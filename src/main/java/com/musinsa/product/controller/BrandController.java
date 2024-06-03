package com.musinsa.product.controller;

import com.musinsa.product.service.BrandService;
import java.util.List;
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
@RequestMapping("/brands")
public class BrandController {

  private BrandService brandService;

  public BrandController(BrandService brandService) {
    this.brandService = brandService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public void createBrand(@RequestBody String brandName) {
    brandService.createBrand(brandName);
  }

  @PutMapping("/{brandName}")
  @ResponseStatus(HttpStatus.OK)
  public void updateBrand(@PathVariable String brandName, @RequestBody String newBrandName) {
    brandService.updateBrand(brandName, newBrandName);
  }

  @DeleteMapping("/{brandName}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteBrand(@PathVariable String brandName) {
    brandService.deleteBrand(brandName);
  }

  @GetMapping
  public List<String> getAllBrands() {
    return brandService.getAllBrands();
  }
}
