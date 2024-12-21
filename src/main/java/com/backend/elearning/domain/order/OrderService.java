package com.backend.elearning.domain.order;

import com.backend.elearning.domain.common.PageableData;

import java.util.List;

public interface OrderService {
    Long createOrder(OrderPostDto orderPostDto);
    List<OrderVM> findAllByUserId();

    List<OrderVM> findAllByUserIdAndStatus(EOrderStatus status);

    void updateOrderStatus(Long orderId, OrderStatusPostVM orderStatusPostVM);
    PageableData<OrderVM> getPageableOrders(int pageNum, int pageSize, Long orderId, EOrderStatus status);
}
