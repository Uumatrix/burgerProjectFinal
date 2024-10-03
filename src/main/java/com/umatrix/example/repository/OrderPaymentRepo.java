package com.umatrix.example.repository;

import com.umatrix.example.models.OrderPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderPaymentRepo extends JpaRepository<OrderPayment, Long> {

    OrderPayment findOrderPaymentByOrderId(Long orderId);

    void deleteOrderPaymentByOrderId(Long orderId);
}
