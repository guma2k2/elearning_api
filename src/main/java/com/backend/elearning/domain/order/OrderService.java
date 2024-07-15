package com.backend.elearning.domain.order;

import java.util.List;

public interface OrderService {
    Long createOrder(OrderPostDto orderPostDto);
    List<OrderDto> findAllByUserId();
    List<OrderGetListDto> findAll();
}
