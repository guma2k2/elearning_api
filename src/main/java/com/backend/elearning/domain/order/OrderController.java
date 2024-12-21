package com.backend.elearning.domain.order;


import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.domain.course.CourseListGetVM;
import com.backend.elearning.domain.course.CourseStatus;
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

    private final OrderDetailService orderDetailService;


    public OrderController(OrderService orderService, OrderDetailService orderDetailService) {
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
    }


    @PostMapping("/orders")
    public ResponseEntity<Long> createOrder(@RequestBody OrderPostDto orderPostDto) {
        Long orderId = orderService.createOrder(orderPostDto);
        return ResponseEntity.ok().body(orderId);
    }

    @PutMapping("/orders/{orderId}/status")
    public ResponseEntity<Void> createOrder(
            @PathVariable("orderId") Long orderId,
            @PathVariable("orderStatus") OrderStatusPostVM status
    ) {
        orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin/orders/paging")
    public ResponseEntity<PageableData<OrderVM>> getPageableCategory (
            @RequestParam(value = "pageNum", defaultValue = Constants.PageableConstant.DEFAULT_PAGE_NUMBER) int pageNum,
            @RequestParam(value = "pageSize", defaultValue = Constants.PageableConstant.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "orderId", required = false) Long orderId,
            @RequestParam(value = "status", required = false) EOrderStatus status

            ) {
        PageableData<OrderVM> pageableData = orderService.getPageableOrders(pageNum, pageSize, orderId, status);
        return ResponseEntity.ok().body(pageableData);
    }
    @GetMapping("/orders/user")
    public ResponseEntity<List<OrderVM>> findAllByUserId() {
        List<OrderVM> orderVMS = orderService.findAllByUserId();
        return ResponseEntity.ok().body(orderVMS);
    }


    @GetMapping("/orders/user/status/{status}")
    public ResponseEntity<List<OrderVM>> findAllByUserId(@PathVariable("status") EOrderStatus status) {
        List<OrderVM> orders = orderService.findAllByUserIdAndStatus(status);
        return ResponseEntity.ok().body(orders);
    }



    @GetMapping("/orders/beseller-courses")
    public ResponseEntity<List<CourseListGetVM>> get10BestSellerProducts() {
        List<CourseListGetVM> courseListGetVMS = orderDetailService.getTopCourseBestSeller(10);
        return ResponseEntity.ok().body(courseListGetVMS);
    }
}
