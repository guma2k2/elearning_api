package com.backend.elearning.domain.statitic;


import com.backend.elearning.domain.course.CourseRepository;
import com.backend.elearning.domain.learning.learningCourse.LearningCourseRepository;
import com.backend.elearning.domain.order.OrderDetailRepository;
import com.backend.elearning.domain.order.OrderRepository;
import com.backend.elearning.domain.payment.PaymentRepository;
import com.backend.elearning.domain.review.ReviewRepository;
import com.backend.elearning.domain.student.StudentRepository;
import com.backend.elearning.domain.user.ERole;
import com.backend.elearning.domain.user.User;
import com.backend.elearning.domain.user.UserRepository;
import com.backend.elearning.utils.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class StatisticService {

    private final PaymentRepository paymentRepository;

    private static final String MONTH_PATTERN = "Th %d";

    private static final String DAY_PATTERN = "Day %d";

    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final LearningCourseRepository learningCourseRepository;
    private final UserRepository userRepository;
    public StatisticService(PaymentRepository paymentRepository, OrderRepository orderRepository, ReviewRepository reviewRepository, CourseRepository courseRepository, StudentRepository studentRepository, OrderDetailRepository orderDetailRepository, LearningCourseRepository learningCourseRepository, UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.reviewRepository = reviewRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.learningCourseRepository = learningCourseRepository;
        this.userRepository = userRepository;
    }


    public List<StatisticCourse> getByTime(String from, String to) {
        LocalDateTime fromTime = DateTimeUtils.convertStringToLocalDateTime(from, DateTimeUtils.NORMAL_TYPE);
        LocalDateTime toTime = DateTimeUtils.convertStringToLocalDateTime(to, DateTimeUtils.NORMAL_TYPE);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        if (user.getRole().equals(ERole.ROLE_ADMIN)){
            return orderDetailRepository.getStatisticByTime(fromTime, toTime, null);
        }
        return orderDetailRepository.getStatisticByTime(fromTime, toTime, email);
    }
    public Dashboard getDashboard() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();


        if (user.getRole().equals(ERole.ROLE_ADMIN)){
            long totalReviews = reviewRepository.findTotalReviews(null);
            long totalCourses = courseRepository.findTotalCourses(null);
            long totalOrders = orderRepository.findTotalOrders();
            long totalStudents = studentRepository.findTotalStudents();
            return new Dashboard(totalOrders, totalReviews, totalCourses, totalStudents);
        }
        Long totalReviews = reviewRepository.findTotalReviews(email);
        Long totalCourses = courseRepository.findTotalCourses(email);
        Long totalOrders = orderDetailRepository.countByInstructor(email);
        Long totalStudents = learningCourseRepository.countStudentByInstructorEmail(email);
        return new Dashboard(totalOrders, totalReviews, totalCourses, totalStudents);
    }

    public List<StatisticTime> findByYear(int year) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        if (user.getRole().equals(ERole.ROLE_ADMIN)) {
            email = null ;
        }
        int numberMonthOfYear = 12;
        List<StatisticTime> statisticTimes = new ArrayList<>();
        List<Statistic> statistics = email != null ? orderDetailRepository.findByYearAndEmail(year, email) :
                paymentRepository.findByYear(year);
        for (Statistic statistic: statistics) {
            String name = String.format(MONTH_PATTERN, statistic.getTime());
            Long total = statistic.getTotal() / 100;
            StatisticTime statisticTime = new StatisticTime(name, total);
            statisticTimes.add(statisticTime);
        }
        for (int month = 1 ; month <= numberMonthOfYear; month++) {
            String name = String.format(MONTH_PATTERN, month);
            StatisticTime statisticTime = new StatisticTime(name);
            if (!statisticTimes.contains(statisticTime)) {
                statisticTimes.add(new StatisticTime(name, 0L));
            }
        }
        statisticTimes.sort((o1, o2) -> {
            int number1 = extractNumber(o1.getName());
            int number2 = extractNumber(o2.getName());
            return Integer.compare(number1, number2);
        });
        return statisticTimes;
    }


    public List<StatisticTime> findByMonth(int month, int year) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        if (user.getRole().equals(ERole.ROLE_ADMIN)) {
            email = null ;
        }
        int numberOfDayInMonth = getNumberOfDaysInMonth(year, month);
        log.info(numberOfDayInMonth+"");
        List<StatisticTime> statisticTimes = new ArrayList<>();
        List<Statistic> statistics = email != null ? orderDetailRepository.findByMonthAndYearAndEmail(month, year, email) :
                paymentRepository.findByMonthAndYear(month, year);
        for (Statistic statistic: statistics) {
            String name = String.format(DAY_PATTERN, statistic.getTime());
            Long total = statistic.getTotal() / 100;
            StatisticTime statisticTime = new StatisticTime(name, total);
            statisticTimes.add(statisticTime);
        }
        for (int day = 1 ; day <= numberOfDayInMonth; day++) {
            String name = String.format(DAY_PATTERN, day);
            StatisticTime statisticTime = new StatisticTime(name);
            if (!statisticTimes.contains(statisticTime)) {
                statisticTimes.add(new StatisticTime(name, 0L));
            }
        }
        statisticTimes.sort((o1, o2) -> {
            int number1 = extractNumber(o1.getName());
            int number2 = extractNumber(o2.getName());
            return Integer.compare(number1, number2);
        });
        return statisticTimes;
    }

    public int getNumberOfDaysInMonth(int year, int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }
        YearMonth yearMonth = YearMonth.of(year, month);
        return yearMonth.lengthOfMonth();
    }

    private int extractNumber(String name) {
        String numberString = name.replaceAll("\\D+", ""); // Remove all non-digit characters
        return Integer.parseInt(numberString); // Convert the remaining string to an integer
    }

    public byte[] export(List<StatisticTime> datas) {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Statistics");

        // Create Header Row
        Row headerRow = sheet.createRow(0);
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        Cell headerCell = headerRow.createCell(0);
        headerCell.setCellValue("Stt");
        headerCell.setCellStyle(headerStyle);

        headerCell = headerRow.createCell(1);
        headerCell.setCellValue("Thời gian");
        headerCell.setCellStyle(headerStyle);

        headerCell = headerRow.createCell(2);
        headerCell.setCellValue("Tổng tiền (đồng)");
        headerCell.setCellStyle(headerStyle);

        // Populate Data Rows
        int rowIdx = 1;
        for (StatisticTime stat : datas) {
            Row row = sheet.createRow(rowIdx);
            row.createCell(0).setCellValue(rowIdx);
            row.createCell(1).setCellValue(stat.getName());
            row.createCell(2).setCellValue(stat.getTotal() / 100);
            rowIdx++;
        }

        // Auto-size Columns
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);

        // Write to ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            workbook.write(outputStream);
            workbook.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return outputStream.toByteArray();

    }

    public byte[] exportByCourse(List<StatisticCourse> datas) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Statistics");

        // Create Header Row
        Row headerRow = sheet.createRow(0);
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        Cell headerCell = headerRow.createCell(0);
        headerCell.setCellValue("Stt");
        headerCell.setCellStyle(headerStyle);

        headerCell = headerRow.createCell(1);
        headerCell.setCellValue("Tên khóa học");
        headerCell.setCellStyle(headerStyle);

        headerCell = headerRow.createCell(2);
        headerCell.setCellValue("Số lượng");
        headerCell.setCellStyle(headerStyle);

        headerCell = headerRow.createCell(3);
        headerCell.setCellValue("Tổng tiền (đồng)");
        headerCell.setCellStyle(headerStyle);

        // Populate Data Rows
        int rowIdx = 1;
        for (StatisticCourse stat : datas) {
            Row row = sheet.createRow(rowIdx);
            row.createCell(0).setCellValue(rowIdx);
            row.createCell(1).setCellValue(stat.getCourse());
            row.createCell(2).setCellValue(stat.getQuantity());
            row.createCell(3).setCellValue(stat.getPrice());
            rowIdx++;
        }

        // Auto-size Columns
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);

        // Write to ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            workbook.write(outputStream);
            workbook.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return outputStream.toByteArray();
    }
}
