//package com.backend.elearning.controller;
//
//import com.backend.elearning.domain.coupon.CouponController;
//import com.backend.elearning.domain.course.CourseController;
//import com.backend.elearning.security.JWTUtil;
//import com.backend.elearning.security.UserDetailsServiceImpl;
//import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
//import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//@WebMvcTest(
//        controllers = CourseController.class,
//        excludeAutoConfiguration = {
//                UserDetailsServiceAutoConfiguration.class, SecurityAutoConfiguration.class
//        }
//)
//public class CourseControllerTest {
//
//    @MockBean
//    private JWTUtil jwtUtil;
//
//    @MockBean
//    private UserDetailsServiceImpl userDetailsService;
//}
