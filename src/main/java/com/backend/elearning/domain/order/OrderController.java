package com.backend.elearning.domain.order;


import com.backend.elearning.domain.course.CourseListGetVM;
import com.backend.elearning.domain.order.impl.OrderDetailServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    private final OrderDetailServiceImpl orderDetailService;


    public OrderController(OrderService orderService, OrderDetailServiceImpl orderDetailService) {
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
    }


    @PostMapping
    public ResponseEntity<Long> createOrder(@RequestBody OrderPostDto orderPostDto) {
        Long orderId = orderService.createOrder(orderPostDto);
        return ResponseEntity.ok().body(orderId);
    }

    @PutMapping("/{orderId}/status/{orderStatus}")
    public ResponseEntity<Void> createOrder(
            @PathVariable("orderId") Long orderId,
            @PathVariable("orderStatus") String status
    ) {
        orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/user")
    public ResponseEntity<List<OrderDto>> findAllByUserId() {
        List<OrderDto> orderDtos = orderService.findAllByUserId();
        return ResponseEntity.ok().body(orderDtos);
    }

    @GetMapping("/beseller-courses")
    public ResponseEntity<List<CourseListGetVM>> get10BestSellerProducts() {
        List<CourseListGetVM> courseListGetVMS = orderDetailService.getTopCourseBestSeller(10);
        return ResponseEntity.ok().body(courseListGetVMS);
    }
}
