package com.musinsa.product.mapper;

import com.musinsa.product.dto.Brand;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BrandMapper {
  void insert(@Param("brandName") String brandName);
  void update(@Param("brandName") String brandName, @Param("newBrandName") String newBrandName);
  void delete(@Param("brandName") String brandName);
  List<Brand> selectAll();
}
