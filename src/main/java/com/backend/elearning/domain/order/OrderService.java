package com.backend.elearning.domain.order;

import com.backend.elearning.domain.common.PageableData;

import java.util.List;

public interface OrderService {
    Long createOrder(OrderPostDto orderPostDto);
    List<OrderVM> findAllByUserId();
    void updateOrderStatus(Long orderId, String orderStatus);
    List<OrderGetListDto> findAll();

    PageableData<OrderVM> getPageableOrders(int pageNum, int pageSize);
}
