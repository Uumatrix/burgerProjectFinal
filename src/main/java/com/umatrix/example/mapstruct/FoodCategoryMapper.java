package com.umatrix.example.mapstruct;


import com.umatrix.example.dto.FoodCategoryDto;
import com.umatrix.example.models.FoodCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FoodCategoryMapper {

    FoodCategoryMapper INSTANCE =  Mappers.getMapper(FoodCategoryMapper.class);

    @Mapping(target = "name", source = "foodCategoryDto.name")
    FoodCategory toFoodCategory(FoodCategoryDto foodCategoryDto);


    FoodCategoryDto toFoodCategoryDto(FoodCategory foodCategory);
}
