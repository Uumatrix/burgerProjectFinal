package com.umatrix.example.mapstruct;


import com.umatrix.example.dto.OrderItemDto;
import com.umatrix.example.models.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);

    //@Mapping(target = "quantity", source = "orderItemDto.quantity")
    OrderItem toOrderItem(OrderItemDto orderItemDto);

    OrderItemDto toOrderItemDto(OrderItem orderItem);
}
