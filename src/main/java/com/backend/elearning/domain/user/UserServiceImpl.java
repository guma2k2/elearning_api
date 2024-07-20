package com.backend.elearning.domain.user;

import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.learning.learningCourse.LearningCourseRepository;
import com.backend.elearning.domain.learning.learningCourse.LearningCourseService;
import com.backend.elearning.domain.review.Review;
import com.backend.elearning.domain.review.ReviewService;
import com.backend.elearning.exception.DuplicateException;
import com.backend.elearning.exception.NotFoundException;
import com.backend.elearning.utils.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final ReviewService reviewService;

    private final PasswordEncoder passwordEncoder;
    private final LearningCourseRepository learningCourseRepository;


    public UserServiceImpl(UserRepository userRepository, ReviewService reviewService, PasswordEncoder passwordEncoder, LearningCourseRepository learningCourseRepository) {
        this.userRepository = userRepository;
        this.reviewService = reviewService;
        this.passwordEncoder = passwordEncoder;
        this.learningCourseRepository = learningCourseRepository;
    }


    @Override
    public PageableData<UserVm> getUsers(int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<User> userPage = userRepository.findAll(pageable);
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
        if (userRepository.countByExistedEmail(userPostVm.email(), null) > 0) {
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
        User savedUser = userRepository.saveAndFlush(user);
        return UserVm.fromModel(user);
    }

    @Override
    @Transactional
    public void update(UserPutVm userPutVm, Long userId) {
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
        if (!userPutVm.photoId().isEmpty() && !userPutVm.photoId().isBlank()) {
            user.setPhoto(userPutVm.photoId());
        }
        if (!userPutVm.password().isEmpty() && !userPutVm.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(userPutVm.password()));
        }
        // update
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(Long userId) {

    }

    @Override
    public UserProfileVM getById(Long userId) {
        User user = userRepository.findByIdCustom(userId).orElseThrow();
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

        return UserProfileVM.fromModel(user, averageRating, numberOfReviews.get(), numberOfStudent.get(), numberOfCourses);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

}
