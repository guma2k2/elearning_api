package com.backend.elearning.domain.order;


import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.domain.course.CourseListGetVM;
import com.backend.elearning.domain.order.impl.OrderDetailServiceImpl;
import com.backend.elearning.domain.review.ReviewVM;
import com.backend.elearning.utils.Constants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class OrderController {

    private final OrderService orderService;

    private final OrderDetailServiceImpl orderDetailService;


    public OrderController(OrderService orderService, OrderDetailServiceImpl orderDetailService) {
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
    }


    @PostMapping("/orders")
    public ResponseEntity<Long> createOrder(@RequestBody OrderPostDto orderPostDto) {
        Long orderId = orderService.createOrder(orderPostDto);
        return ResponseEntity.ok().body(orderId);
    }

    @PutMapping("/orders/{orderId}/status/{orderStatus}")
    public ResponseEntity<Void> createOrder(
            @PathVariable("orderId") Long orderId,
            @PathVariable("orderStatus") String status
    ) {
        orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/orders/user")
    public ResponseEntity<List<OrderVM>> findAllByUserId() {
        List<OrderVM> orderVMS = orderService.findAllByUserId();
        return ResponseEntity.ok().body(orderVMS);
    }

    @GetMapping("/admin/orders/paging")
    public ResponseEntity<PageableData<OrderVM>> getPageableCategory (
            @RequestParam(value = "pageNum", defaultValue = Constants.PageableConstant.DEFAULT_PAGE_NUMBER) int pageNum,
            @RequestParam(value = "pageSize", defaultValue = Constants.PageableConstant.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "orderId", required = false) Long orderId
    ) {
        PageableData<OrderVM> pageableData = orderService.getPageableOrders(pageNum, pageSize, orderId);
        return ResponseEntity.ok().body(pageableData);
    }

    @GetMapping("/orders/beseller-courses")
    public ResponseEntity<List<CourseListGetVM>> get10BestSellerProducts() {
        List<CourseListGetVM> courseListGetVMS = orderDetailService.getTopCourseBestSeller(10);
        return ResponseEntity.ok().body(courseListGetVMS);
    }
}
