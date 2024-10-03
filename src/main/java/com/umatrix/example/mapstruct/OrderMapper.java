package com.umatrix.example.mapstruct;

import com.umatrix.example.dto.OrderDto;
import com.umatrix.example.models.Order;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "address", source = "orderDto.address")
    Order toOrder(OrderDto orderDto);

    OrderDto toOrderDto(Order order);

}
