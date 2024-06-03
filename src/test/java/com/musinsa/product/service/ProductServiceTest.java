package com.musinsa.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;

import com.musinsa.product.dao.ProductDao;
import com.musinsa.product.dto.CategoryMinMaxPriceBrand;
import com.musinsa.product.dto.CategoryMinPriceBrandList;
import com.musinsa.product.dto.CheapestBrandProductList;
import com.musinsa.product.dto.Product;
import com.musinsa.product.exception.CustomException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
  @Mock
  private ProductDao productDao;

  @InjectMocks
  private ProductService productService;

  @BeforeEach
  void setUp() {
    productService = new ProductService(productDao);
  }

  @Test
  void getCategoriesPriceMinBrand_ShouldReturnValidData() throws Exception {
    // given
    List<Product> categoryMinPriceBrandList = new ArrayList<>();
    categoryMinPriceBrandList.add(new Product("상의", "A", new BigDecimal(15000)));
    categoryMinPriceBrandList.add(new Product("아우터", "B", new BigDecimal(40000)));
    given(productDao.selectMinPriceBrandPerCategory()).willReturn(categoryMinPriceBrandList);

    // when
    CategoryMinPriceBrandList result = productService.getCategoriesPriceMinBrand();

    // then
    assertThat(result.getCategoryMinPriceBrands().size()).isEqualTo(2);
    assertThat(result.getTotalPrice()).isEqualTo(new BigDecimal(55000));
  }

  @Test
  void getCategoriesPriceMinBrand_ShouldThrowCustomException_WhenDaoThrowsException() {
    // given
    given(productDao.selectMinPriceBrandPerCategory()).willThrow(new RuntimeException());

    // when
    assertThatExceptionOfType(CustomException.class)
        .isThrownBy(() -> productService.getCategoriesPriceMinBrand())
        .withMessageContaining("Error occurred while selectMinPriceBrandPerCategory from DB.")
        .matches(exception -> exception.getHttpStatus() == HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Test
  void getTotalPriceMinBrand_ReturnsCheapestBrandInfo() throws Exception {
    List<Product> mockProducts = Arrays.asList(
        new Product("상의", "A", new BigDecimal(15000)),
        new Product("아우터", "A", new BigDecimal(40000))
    );
    given(productDao.selectCheapestBrandProducts()).willReturn(mockProducts);

    CheapestBrandProductList result = productService.getTotalPriceMinBrand();

    assertThat(result.getCheapestBrandName()).isEqualTo("A");
    assertThat(result.getPriceTotal()).isEqualTo(BigDecimal.valueOf(55000));
    assertThat(result.getCategoryProductPriceList()).hasSize(2);
  }

  @Test
  void getCategoryPriceMinMaxBrand_ReturnsMinMaxPrices() throws Exception {
    String category = "스니커즈";
    Product minPriceProduct = new Product(category, "D", new BigDecimal(5000));
    Product maxPriceProduct = new Product(category, "B", new BigDecimal(75000));
    given(productDao.selectCategoryMinPrice(category)).willReturn(minPriceProduct);
    given(productDao.selectCategoryMaxPrice(category)).willReturn(maxPriceProduct);

    CategoryMinMaxPriceBrand result = productService.getCategoryPriceMinMaxBrand(category);

    assertThat(result.getMinProductPrice().getPrice()).isEqualTo(minPriceProduct.getPrice());
    assertThat(result.getMinProductPrice().getBrandName()).isEqualTo(minPriceProduct.getBrandName());
    assertThat(result.getMaxProductPrice().getPrice()).isEqualTo(maxPriceProduct.getPrice());
    assertThat(result.getMaxProductPrice().getBrandName()).isEqualTo(maxPriceProduct.getBrandName());
  }
}
