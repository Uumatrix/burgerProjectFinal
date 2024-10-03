package com.umatrix.example.mapstruct;

import org.mapstruct.Mapper;
import com.umatrix.example.dto.FoodDto;
import com.umatrix.example.models.Food;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper
public interface FoodMapper {

    FoodMapper INSTANCE = Mappers.getMapper(FoodMapper.class);

    @Mapping(target = "name", source = "foodDto.name")
    @Mapping(target = "description", source = "foodDto.description")
    @Mapping(target = "price", source = "foodDto.price")
    @Mapping(target = "quantity", source = "foodDto.quantity")
    @Mapping(target = "weight", source = "foodDto.weight")
    @Mapping(target = "composition", source = "foodDto.composition")
    Food toFood(FoodDto foodDto);

    FoodDto toFoodDto(Food food);

}

