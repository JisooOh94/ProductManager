package com.musinsa.product;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.musinsa.product.dto.CategoryMinMaxPriceBrandForTest;
import com.musinsa.product.dto.CategoryMinMaxPriceBrandForTest.BrandProductPrice;
import com.musinsa.product.dto.CategoryMinPriceBrandListForTest;
import com.musinsa.product.dto.CheapestBrandProductListForTest;
import com.musinsa.product.dto.CheapestBrandProductListForTest.CategoryProductPrice;
import com.musinsa.product.dto.ProductForTest;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestMethodOrder(OrderAnnotation.class)
class MusinsaProductManagerApplicationTests {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @Autowired
  private MockMvc mockMvc;

  @Test
  @Order(1)
  public void getCategoriesPriceMinBrandTest() throws Exception {
    //given
    CategoryMinPriceBrandListForTest expectResult = new CategoryMinPriceBrandListForTest(Arrays.asList(
        new ProductForTest("상의", "C", BigDecimal.valueOf(10000)),
        new ProductForTest("아우터", "E", BigDecimal.valueOf(5000)),
        new ProductForTest("바지", "D", BigDecimal.valueOf(3000)),
        new ProductForTest("스니커즈", "A", BigDecimal.valueOf(9000)),
        new ProductForTest("가방", "A", BigDecimal.valueOf(2000)),
        new ProductForTest("모자", "D", BigDecimal.valueOf(1500)),
        new ProductForTest("양말", "I", BigDecimal.valueOf(1700)),
        new ProductForTest("액세서리", "F", BigDecimal.valueOf(1900))),
        BigDecimal.valueOf(34100)
    );

    //when
    MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/products/categories/price/min/brand"))
        .andReturn()
        .getResponse();

    //then
    int actualStatusCode = response.getStatus();
    assertEquals(HttpStatus.OK.value(), actualStatusCode);

    CategoryMinPriceBrandListForTest actualResult = OBJECT_MAPPER.readValue(response.getContentAsString(), CategoryMinPriceBrandListForTest.class);
    assertEquals(actualResult.getTotalPrice(), expectResult.getTotalPrice());
    Assertions.assertThat(actualResult.getCategoryMinPriceBrands())
        .usingRecursiveFieldByFieldElementComparator()
        .containsExactlyInAnyOrderElementsOf(expectResult.getCategoryMinPriceBrands());
  }

  @Test
  @Order(2)
  public void getTotalPriceMinBrandTest() throws Exception {
    //given
    CheapestBrandProductListForTest expectResult = new CheapestBrandProductListForTest("D", Arrays.asList(
        new CategoryProductPrice("상의", BigDecimal.valueOf(10100)),
        new CategoryProductPrice("아우터", BigDecimal.valueOf(5100)),
        new CategoryProductPrice("바지", BigDecimal.valueOf(3000)),
        new CategoryProductPrice("스니커즈", BigDecimal.valueOf(9500)),
        new CategoryProductPrice("가방", BigDecimal.valueOf(2500)),
        new CategoryProductPrice("모자", BigDecimal.valueOf(1500)),
        new CategoryProductPrice("양말", BigDecimal.valueOf(2400)),
        new CategoryProductPrice("액세서리", BigDecimal.valueOf(2000))),
        BigDecimal.valueOf(36100)
    );

    //when
    MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/products/total-price/min/brand"))
        .andReturn()
        .getResponse();

    //then
    int actualStatusCode = response.getStatus();
    assertEquals(HttpStatus.OK.value(), actualStatusCode);

    CheapestBrandProductListForTest actualResult = OBJECT_MAPPER.readValue(response.getContentAsString(), CheapestBrandProductListForTest.class);
    assertEquals(actualResult.getCheapestBrandName(), expectResult.getCheapestBrandName());
    assertEquals(actualResult.getPriceTotal(), expectResult.getPriceTotal());
    Assertions.assertThat(actualResult.getCategoryProductPriceList())
        .usingRecursiveFieldByFieldElementComparator()
        .containsExactlyInAnyOrderElementsOf(expectResult.getCategoryProductPriceList());
  }

  private static Stream<Arguments> getCategoryPriceMinMaxBrandTestParams() {
    return Stream.of(
        Arguments.of("상의", new CategoryMinMaxPriceBrandForTest("상의", new BrandProductPrice("C", BigDecimal.valueOf(10000)), new BrandProductPrice("I", BigDecimal.valueOf(11400)))),
        Arguments.of("아우터", new CategoryMinMaxPriceBrandForTest("아우터", new BrandProductPrice("E", BigDecimal.valueOf(5000)), new BrandProductPrice("F", BigDecimal.valueOf(7200)))),
        Arguments.of("바지", new CategoryMinMaxPriceBrandForTest("바지", new BrandProductPrice("D", BigDecimal.valueOf(3000)), new BrandProductPrice("A", BigDecimal.valueOf(4200))))
    );
  }

  @Order(3)
  @ParameterizedTest
  @MethodSource("getCategoryPriceMinMaxBrandTestParams")
  public void getCategoryPriceMinMaxBrandTest(String category, CategoryMinMaxPriceBrandForTest expectResult) throws Exception {
    //when
    MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/products/{category}/price/min-max/brand", category))
        .andReturn()
        .getResponse();

    //then
    int actualStatusCode = response.getStatus();
    assertEquals(HttpStatus.OK.value(), actualStatusCode);

    CategoryMinMaxPriceBrandForTest actualResult = OBJECT_MAPPER.readValue(response.getContentAsString(), CategoryMinMaxPriceBrandForTest.class);
    Assertions.assertThat(actualResult).usingRecursiveComparison().isEqualTo(expectResult);
  }


  @Test
  @Order(4)
  public void getAllBrandsTest() throws Exception {
    //given
    List<String> expectResult = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I");

    //when
    MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/brands"))
        .andReturn()
        .getResponse();

    //then
    int actualStatusCode = response.getStatus();
    assertEquals(HttpStatus.OK.value(), actualStatusCode);

    List<String> actualResult = OBJECT_MAPPER.readValue(response.getContentAsString(), new TypeReference<List<String>>(){});
    Assertions.assertThat(actualResult).usingRecursiveFieldByFieldElementComparator().containsExactlyInAnyOrderElementsOf(expectResult);
  }

  @Test
  @Order(5)
  public void createBrandTest() throws Exception {
    //given
    List<String> expectResult = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J");
    String creatingBrandName = "J";

    //when
    MockHttpServletResponse response = mockMvc.perform(
        MockMvcRequestBuilders.post("/brands")
            .characterEncoding(StandardCharsets.UTF_8.name())
            .contentType(MediaType.APPLICATION_JSON)
            .content(creatingBrandName)
        ).andReturn()
        .getResponse();

    //then
    int actualStatusCode = response.getStatus();
    assertEquals(HttpStatus.OK.value(), actualStatusCode);

    String content = mockMvc.perform(MockMvcRequestBuilders.get("/brands")).andReturn().getResponse().getContentAsString();
    List<String> actualResult = OBJECT_MAPPER.readValue(content, new TypeReference<List<String>>() {});
    Assertions.assertThat(actualResult).usingRecursiveFieldByFieldElementComparator().containsExactlyInAnyOrderElementsOf(expectResult);
  }

  @Test
  @Order(6)
  public void updateBrandTest() throws Exception {
    //given
    List<String> expectResult = Arrays.asList("A", "B", "C", "Foo", "E", "F", "G", "H", "I");
    String brandName = "D";
    String newBrandName = "Foo";

    //when
    MockHttpServletResponse response = mockMvc.perform(
            MockMvcRequestBuilders.put("/brands/{brandName}", brandName)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .contentType(MediaType.APPLICATION_JSON)
                .content(newBrandName)
        ).andReturn()
        .getResponse();

    //then
    int actualStatusCode = response.getStatus();
    assertEquals(HttpStatus.OK.value(), actualStatusCode);

    //brand data verify
    String content = mockMvc.perform(MockMvcRequestBuilders.get("/brands")).andReturn().getResponse().getContentAsString();
    List<String> actualResult = OBJECT_MAPPER.readValue(content, new TypeReference<List<String>>() {});
    Assertions.assertThat(actualResult).usingRecursiveFieldByFieldElementComparator().containsExactlyInAnyOrderElementsOf(expectResult);

    //product data verify
    String responeBody = mockMvc.perform(MockMvcRequestBuilders.get("/products/total-price/min/brand"))
        .andReturn()
        .getResponse()
        .getContentAsString();
    CheapestBrandProductListForTest cheapestBrandProductList = OBJECT_MAPPER.readValue(responeBody, CheapestBrandProductListForTest.class);
    assertEquals(cheapestBrandProductList.getCheapestBrandName(), newBrandName);
  }

  @Test
  @Order(7)
  public void deleteBrandTest() throws Exception {
    //given
    List<String> expectResult = Arrays.asList("A", "B", "C", "E", "F", "G", "H", "I");
    String brandName = "D";

    //when
    MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.delete("/brands/{brandName}", brandName))
        .andReturn()
        .getResponse();

    //then
    int actualStatusCode = response.getStatus();
    assertEquals(HttpStatus.OK.value(), actualStatusCode);

    //brand data verify
    String content = mockMvc.perform(MockMvcRequestBuilders.get("/brands")).andReturn().getResponse().getContentAsString();
    List<String> actualResult = OBJECT_MAPPER.readValue(content, new TypeReference<List<String>>() {});
    Assertions.assertThat(actualResult).usingRecursiveFieldByFieldElementComparator().containsExactlyInAnyOrderElementsOf(expectResult);

    //product data verify
    String secondaryCheapestBrandName = "C";
    String responeBody = mockMvc.perform(MockMvcRequestBuilders.get("/products/total-price/min/brand"))
        .andReturn()
        .getResponse()
        .getContentAsString();
    CheapestBrandProductListForTest cheapestBrandProductList = OBJECT_MAPPER.readValue(responeBody, CheapestBrandProductListForTest.class);
    assertEquals(cheapestBrandProductList.getCheapestBrandName(), secondaryCheapestBrandName);
  }
}
