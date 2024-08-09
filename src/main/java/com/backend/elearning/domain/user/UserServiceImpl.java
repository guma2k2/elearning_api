package com.backend.elearning.domain.user;

import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.course.CourseListGetVM;
import com.backend.elearning.domain.course.CourseService;
import com.backend.elearning.domain.learning.learningCourse.LearningCourseRepository;
import com.backend.elearning.domain.review.Review;
import com.backend.elearning.domain.review.ReviewService;
import com.backend.elearning.exception.BadRequestException;
import com.backend.elearning.exception.DuplicateException;
import com.backend.elearning.exception.NotFoundException;
import com.backend.elearning.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final ReviewService reviewService;
    private final PasswordEncoder passwordEncoder;
    private final LearningCourseRepository learningCourseRepository;
    private final String sortBy = "updatedAt";


    public UserServiceImpl(UserRepository userRepository, ReviewService reviewService, PasswordEncoder passwordEncoder, LearningCourseRepository learningCourseRepository) {
        this.userRepository = userRepository;
        this.reviewService = reviewService;
        this.passwordEncoder = passwordEncoder;
        this.learningCourseRepository = learningCourseRepository;
    }


    @Override
    public PageableData<UserVm> getUsers(int pageNum, int pageSize, String keyword) {
        Sort sort = Sort.by(sortBy);
        sort.descending();
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<User> userPage = userRepository.findAllCustom(pageable, keyword);
        List<User> users = userPage.getContent();
        List<UserVm> userVms = users.stream().map(UserVm::fromModel).toList();
        return new PageableData<>(
                pageNum,
                pageSize,
                (int) userPage.getTotalElements(),
                userPage.getTotalPages(),
                userVms
        );
    }

    @Override
    public UserGetDetailVm getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Constants.ERROR_CODE.USER_EMAIL_DUPLICATED, userId));
        return new UserGetDetailVm(
                userId,
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getGender().name(),
                user.isActive(),
                user.getPhoto(),
                user.getDateOfBirth().toString(),
                user.getRole().name()
        );
    }

    @Override
    @Transactional
    public UserVm create(UserPostVm userPostVm) {
        if (userRepository.countByExistedEmail(userPostVm.email(), null) > 0L) {
            throw new DuplicateException(Constants.ERROR_CODE.USER_EMAIL_DUPLICATED, userPostVm.email());
        }
        User user = User.builder()
                .email(userPostVm.email())
                .firstName(userPostVm.firstName())
                .lastName(userPostVm.lastName())
                .active(userPostVm.active())
                .password(passwordEncoder.encode(userPostVm.password()))
                .dateOfBirth(LocalDate.of(userPostVm.year(), userPostVm.month(), userPostVm.day()))
                .photo(userPostVm.photo())
                .role(ERole.valueOf(userPostVm.role()))
                .gender(EGender.valueOf(userPostVm.gender()))
                .build();
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        User savedUser = userRepository.saveAndFlush(user);
        return UserVm.fromModel(savedUser);
    }

    @Override
    @Transactional
    public UserVm update(UserPutVm userPutVm, Long userId) {
        if (userRepository.countByExistedEmail(userPutVm.email(), userId) > 0) {
            throw new DuplicateException(Constants.ERROR_CODE.USER_EMAIL_DUPLICATED, userPutVm.email());
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(Constants.ERROR_CODE.USER_NOT_FOUND, userId));
        user.setEmail(userPutVm.email());
        user.setFirstName(userPutVm.firstName());
        user.setLastName(userPutVm.lastName());
        user.setActive(userPutVm.active());
        user.setRole(ERole.valueOf(userPutVm.role()));
        user.setDateOfBirth(LocalDate.of(userPutVm.year(), userPutVm.month(), userPutVm.day()));
        user.setUpdatedAt(LocalDateTime.now());
        Optional.ofNullable(userPutVm.photo())
                .filter(photo -> !photo.isEmpty() && !photo.isBlank())
                .ifPresent(user::setPhoto);

        Optional.ofNullable(userPutVm.password())
                .filter(pass -> !pass.isEmpty() && !pass.isBlank())
                .ifPresent(pass -> user.setPassword(passwordEncoder.encode(pass)));
        userRepository.save(user);
        return UserVm.fromModel(user);
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        User user = userRepository.findByIdCustom(userId).orElseThrow( () -> new NotFoundException(Constants.ERROR_CODE.USER_NOT_FOUND, userId));
        if (user.getCourses().size() > 0) {
            throw new BadRequestException("User had course");
        }
        userRepository.delete(user);
    }

    @Override
    public UserProfileVM getById(Long userId) {
        User user = userRepository.findByIdCustom(userId).orElseThrow(() -> new NotFoundException(Constants.ERROR_CODE.USER_NOT_FOUND, userId));
        List<Course> coursesOfUser = user.getCourses();
        int numberOfCourses = coursesOfUser.size();
        AtomicInteger numberOfReviews = new AtomicInteger();
        AtomicReference<Double> numberOfRatingOfCourses = new AtomicReference<>(0.0);
        AtomicInteger numberOfStudent = new AtomicInteger();
        coursesOfUser.forEach(course -> {
            Long courseId = course.getId();
            List<Review> reviews = reviewService.findByCourseId(courseId);
            int ratingCount = reviews.size();
            numberOfReviews.addAndGet(ratingCount);
            Double averageRating = reviews.stream().map(review -> review.getRatingStar()).mapToDouble(Integer::doubleValue).average().orElse(0.0);
            numberOfRatingOfCourses.updateAndGet(v -> v + averageRating);
            Long numberOfStudentPerCourse = learningCourseRepository.countStudentByCourseId(courseId);
            numberOfStudent.addAndGet(Math.toIntExact(numberOfStudentPerCourse));
        });
        Double averageRating = numberOfRatingOfCourses.get() / (numberOfCourses * 1.0);

        return UserProfileVM.fromModel(user, averageRating, numberOfReviews.get(), numberOfStudent.get(), numberOfCourses, new ArrayList<>());
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    @Override
    public UserGetDetailVm getUserProfile(Long id) {
        User user = userRepository.findByIdCustom(id).orElseThrow();
        List<Course> coursesOfUser = user.getCourses();
        AtomicInteger numberOfReviews = new AtomicInteger();
        AtomicInteger numberOfStudent = new AtomicInteger();
        coursesOfUser.forEach(course -> {
            Long courseId = course.getId();
            List<Review> reviews = reviewService.findByCourseId(courseId);
            int ratingCount = reviews.size();
            numberOfReviews.addAndGet(ratingCount);
            Long numberOfStudentPerCourse = learningCourseRepository.countStudentByCourseId(courseId);
            numberOfStudent.addAndGet(Math.toIntExact(numberOfStudentPerCourse));
        });
        UserGetDetailVm userGetDetailVm = new UserGetDetailVm(user);
        userGetDetailVm.setNumberOfReview(numberOfReviews.get());
        userGetDetailVm.setNumberOfStudent(numberOfStudent.get());
        return userGetDetailVm;
    }

}
