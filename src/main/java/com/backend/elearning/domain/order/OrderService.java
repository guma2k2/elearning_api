package com.backend.elearning.domain.order;

import java.util.List;

public interface OrderService {
    Long createOrder(OrderPostDto orderPostDto);
    List<OrderDto> findAllByUserId();
    void updateOrderStatus(Long orderId, String orderStatus);
    List<OrderGetListDto> findAll();
}
