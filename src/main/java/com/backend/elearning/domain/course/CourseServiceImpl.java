package com.backend.elearning.domain.course;

import com.backend.elearning.domain.cart.Cart;
import com.backend.elearning.domain.cart.CartRepository;
import com.backend.elearning.domain.category.Category;
import com.backend.elearning.domain.category.CategoryRepository;
import com.backend.elearning.domain.common.ECurriculumType;
import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.domain.learning.learningCourse.LearningCourse;
import com.backend.elearning.domain.learning.learningCourse.LearningCourseRepository;
import com.backend.elearning.domain.learning.learningLecture.LearningLecture;
import com.backend.elearning.domain.learning.learningLecture.LearningLectureRepository;
import com.backend.elearning.domain.learning.learningQuiz.LearningQuiz;
import com.backend.elearning.domain.learning.learningQuiz.LearningQuizRepository;
import com.backend.elearning.domain.lecture.Lecture;
import com.backend.elearning.domain.lecture.LectureRepository;
import com.backend.elearning.domain.order.OrderDetail;
import com.backend.elearning.domain.order.OrderDetailRepository;
import com.backend.elearning.domain.promotion.Promotion;
import com.backend.elearning.domain.quiz.Quiz;
import com.backend.elearning.domain.quiz.QuizRepository;
import com.backend.elearning.domain.review.Review;
import com.backend.elearning.domain.review.ReviewService;
import com.backend.elearning.domain.section.Section;
import com.backend.elearning.domain.section.SectionService;
import com.backend.elearning.domain.section.SectionVM;
import com.backend.elearning.domain.topic.Topic;
import com.backend.elearning.domain.topic.TopicRepository;
import com.backend.elearning.domain.user.*;
import com.backend.elearning.exception.BadRequestException;
import com.backend.elearning.exception.DuplicateException;
import com.backend.elearning.exception.NotFoundException;
import com.backend.elearning.utils.Constants;
import com.backend.elearning.utils.ConvertTitleToSlug;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class CourseServiceImpl implements CourseService{

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final TopicRepository topicRepository;
    private final SectionService sectionService;
    private final QuizRepository quizRepository;
    private final LectureRepository lectureRepository ;
    private final ReviewService reviewService;
    private final LearningLectureRepository learningLectureRepository;
    private final LearningQuizRepository learningQuizRepository;
    private final LearningCourseRepository learningCourseRepository;
    private final OrderDetailRepository orderDetailRepository;

    private final CourseRepositoryCustomImpl courseRepositoryCustom;
    private final CartRepository cartRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private static final  String LECTURE_TYPE = "lecture";
    private static final String QUIZ_TYPE = "quiz";
    private static final String sortBy = "updatedAt";

    public CourseServiceImpl(CourseRepository courseRepository, CategoryRepository categoryRepository, TopicRepository topicRepository, SectionService sectionService, QuizRepository quizRepository, LectureRepository lectureRepository, ReviewService reviewService, LearningLectureRepository learningLectureRepository, LearningQuizRepository learningQuizRepository, LearningCourseRepository learningCourseRepository, OrderDetailRepository orderDetailRepository, CourseRepositoryCustomImpl courseRepositoryCustom, CartRepository cartRepository, UserService userService, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
        this.topicRepository = topicRepository;
        this.sectionService = sectionService;
        this.quizRepository = quizRepository;
        this.lectureRepository = lectureRepository;
        this.reviewService = reviewService;
        this.learningLectureRepository = learningLectureRepository;
        this.learningQuizRepository = learningQuizRepository;
        this.learningCourseRepository = learningCourseRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.courseRepositoryCustom = courseRepositoryCustom;
        this.cartRepository = cartRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }


    @Override
    public PageableData<CourseVM> getPageableCourses(int pageNum, int pageSize, String keyword, CourseStatus status) {
        log.info("received pageNum: {}, pageSize: {}, keyword: {}, status: {}", pageNum, pageSize, keyword, status);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Sort sort = Sort.by(sortBy);
        sort.descending();
        if (email != null) {
            log.info("email from token: {}", email);
            User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(Constants.ERROR_CODE.USER_NOT_FOUND));
            if (user.getRole().equals(ERole.ROLE_ADMIN)) {
                email = null;
            }
        }
        List<CourseVM> courseVMS = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Course> coursePage = null;
        if (status != null) {
            coursePage = courseRepository.findAllCustomByRoleAndStatus(pageable, email, keyword, status);
        }else {
            coursePage = courseRepository.findAllCustomByRole(pageable, email, keyword);
        }
        List<Course> courses = coursePage.getContent();
        for (Course course : courses) {
            courseVMS.add(CourseVM.fromModel(course ,new ArrayList<>(),0, 0.0,0,"" , null, false, 0L));

        }
        return new PageableData(
                pageNum,
                pageSize,
                (int) coursePage.getTotalElements(),
                coursePage.getTotalPages(),
                courseVMS
        );
    }

    @Override
    public CourseVM create(CoursePostVM coursePostVM) {
        log.info("received coursePostVM: {}", coursePostVM);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (courseRepository.countExistByTitle(coursePostVM.title(), null) > 0) {
            throw new DuplicateException(Constants.ERROR_CODE.COURSE_TITLE_DUPLICATED);
        }
        Category category = categoryRepository.findById(coursePostVM.categoryId()).orElseThrow(
                () -> new NotFoundException(Constants.ERROR_CODE.CATEGORY_NOT_FOUND)
        );
        Topic topic = topicRepository.findById(coursePostVM.topicId()).orElseThrow(
                () -> new NotFoundException(Constants.ERROR_CODE.TOPIC_NOT_FOUND)
        );
        User user = userService.getByEmail(email);
        String slug = ConvertTitleToSlug.convertTitleToSlug(coursePostVM.title());
        Course course = Course.builder()
                .title(coursePostVM.title())
                .category(category)
                .topic(topic)
                .user(user)
                .free(true)
                .status(CourseStatus.UNPUBLISHED)
                .slug(slug)
                .build();
        if (!course.isFree()) {
            course.setPrice(coursePostVM.price());
        }
        course.setCreatedAt(LocalDateTime.now());
        course.setUpdatedAt(LocalDateTime.now());
        return CourseVM.fromModel(courseRepository.save(course), new ArrayList<>(),0, 0.0,0,"", null, false, 0L);
    }

    @Override
    public CourseVM update(CoursePostVM coursePutVM, Long userId, Long courseId) {
        log.info("received coursePutVM: {}", coursePutVM);
        Course oldCourse = courseRepository.findByIdReturnSections(courseId).orElseThrow(() -> new NotFoundException(
                Constants.ERROR_CODE.COURSE_NOT_FOUND, courseId
        ));

        if (courseRepository.countExistByTitle(coursePutVM.title(), courseId) > 0) {
            throw new DuplicateException(Constants.ERROR_CODE.COURSE_TITLE_DUPLICATED);
        }
        if (!Objects.equals(oldCourse.getCategory().getId(), coursePutVM.categoryId())) {
            Category category = categoryRepository.findById(coursePutVM.categoryId()).orElseThrow();
            oldCourse.setCategory(category);
        }

        if (!Objects.equals(oldCourse.getTopic().getId(), coursePutVM.topicId())) {
            Topic topic = topicRepository.findById(coursePutVM.topicId()).orElseThrow();
            oldCourse.setTopic(topic);
        }
        oldCourse.setTitle(coursePutVM.title());
        oldCourse.setHeadline(coursePutVM.headline());
        oldCourse.setObjectives(coursePutVM.objectives());
        oldCourse.setRequirements(coursePutVM.requirements());
        oldCourse.setDescription(coursePutVM.description());
        oldCourse.setTargetAudiences(coursePutVM.targetAudiences());
        oldCourse.setFree(coursePutVM.free());
        oldCourse.setStatus(CourseStatus.UNPUBLISHED);
        oldCourse.setUpdatedAt(LocalDateTime.now());
        if (!coursePutVM.free()) {
            oldCourse.setPrice(coursePutVM.price());
        }
        if (!Objects.equals(coursePutVM.image(), "")) {
            oldCourse.setImageId(coursePutVM.image());
        }
        if (coursePutVM.level() != null) {
            oldCourse.setLevel(coursePutVM.level());
        }
        return CourseVM.fromModel(courseRepository.save(oldCourse), new ArrayList<>(),0, 0.0,0,"", null , false, 0L);
    }

    @Override
    public CourseVM getCourseById(Long id) {
        Course course = courseRepository.findByIdReturnSections(id).orElseThrow(() -> new NotFoundException(Constants.ERROR_CODE.COURSE_NOT_FOUND, id));
        course = courseRepository.findByIdWithPromotions(course).orElseThrow(() -> new NotFoundException(Constants.ERROR_CODE.COURSE_NOT_FOUND, id));
        Long courseId = course.getId();
        List<Review> reviews = reviewService.findByCourseId(courseId);
        int ratingCount = reviews.size();
        Double averageRating = reviews.stream().map(review -> review.getRatingStar()).mapToDouble(Integer::doubleValue).average().orElse(0.0);

        double roundedAverageRating = Math.round(averageRating * 10) / 10.0;

        AtomicInteger totalCurriculumCourse = new AtomicInteger();
        AtomicInteger totalDurationCourse = new AtomicInteger();
        course.getSections().forEach(section -> {
            Long sectionId = section.getId();
            List<Lecture> lectures = lectureRepository.findBySectionId(sectionId);
            int totalSeconds = lectures.stream()
                    .mapToInt(lecture -> lecture.getDuration())
                    .sum();
            totalDurationCourse.addAndGet(totalSeconds);

        });
        String formattedHours = convertSeconds(totalDurationCourse.get());


        List<SectionVM> sections = new ArrayList<>(course.getSections()
                .stream().map(section -> sectionService.getById(section.getId(), null)).toList());


        sections.forEach(sectionVM -> {
            int countCurriculumPerSection = sectionVM.curriculums().size();
            totalCurriculumCourse.addAndGet(countCurriculumPerSection);
        });
        sections.sort(Comparator.comparing(SectionVM::number));

        Long userId = course.getUser().getId();
        UserProfileVM userProfileVM = userService.getById(userId);

        boolean learning = true;
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (email != null && !learningCourseRepository.findByStudentAndCourse(email, courseId).isPresent()) {
            learning = false;
        }

        Set<Promotion> promotions = course.getPromotions();
        Long discountedPrice = getDiscountedPriceByCourse(promotions, course);

        return CourseVM.fromModel(course, sections,ratingCount, roundedAverageRating, totalCurriculumCourse.get(),formattedHours, userProfileVM, learning, discountedPrice);
    }

    private Long getDiscountedPriceByCourse(Set<Promotion> promotions, Course course)   {
        LocalDateTime now = LocalDateTime.now();
        Optional<Promotion> currentPromotion = promotions.stream()
                .filter(promotion -> promotion.getStartTime().isBefore(now) && promotion.getEndTime().isAfter(now))
                .findFirst();
        if (course.isFree()) {
            return null;
        }
        Long discountedPrice = currentPromotion.isPresent() ? course.getPrice() - (currentPromotion.get().getDiscountPercent()*course.getPrice()/100) : course.getPrice();
        return discountedPrice;
    }

    @Override
    public CourseListGetVM getCourseListGetVMById(Long id) {
        log.info("received id: {}", id);
        Course course = courseRepository.findByIdReturnSections(id).orElseThrow(() -> new NotFoundException(Constants.ERROR_CODE.COURSE_NOT_FOUND, id));
        course = courseRepository.findByIdWithPromotions(course).orElseThrow(() -> new NotFoundException(Constants.ERROR_CODE.COURSE_NOT_FOUND, id));
        Long courseId = course.getId();
        List<Review> reviews = reviewService.findByCourseId(courseId);
        int ratingCount = reviews.size();
        Double averageRating = reviews.stream().map(review -> review.getRatingStar()).mapToDouble(Integer::doubleValue).average().orElse(0.0);
        double roundedAverageRating = Math.round(averageRating * 10) / 10.0;

        // get totalCurriculum, total duration of course by section
        AtomicInteger totalCurriculumCourse = new AtomicInteger();
        AtomicInteger totalDurationCourse = new AtomicInteger();


        course.getSections().forEach(section -> {
            Long sectionId = section.getId();
            List<Lecture> lectures = lectureRepository.findBySectionId(sectionId);
            List<Quiz> quizzes = quizRepository.findBySectionId(sectionId   );
            totalCurriculumCourse.addAndGet(lectures.size());
            totalCurriculumCourse.addAndGet(quizzes.size());
            int totalSeconds = lectures.stream()
                    .mapToInt(lecture -> lecture.getDuration())
                    .sum();
            totalDurationCourse.addAndGet(totalSeconds);
        });
        String formattedHours = convertSeconds(totalDurationCourse.get());

        Set<Promotion> promotions = course.getPromotions();
        Long discountedPrice = getDiscountedPriceByCourse(promotions, course);

        return CourseListGetVM.fromModel(course, formattedHours, totalCurriculumCourse.get(), roundedAverageRating, ratingCount, discountedPrice);
    }

    @Override
    public CourseLearningVm getCourseBySlug(String slug) {
        log.info("Received slug: {}", slug);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Course course = courseRepository.findBySlugReturnSections(slug).orElseThrow(
                () -> new NotFoundException(Constants.ERROR_CODE.COURSE_NOT_FOUND, slug)
        );

        AtomicInteger totalCurriculumCourse = new AtomicInteger();
        List<SectionVM> sections = new ArrayList<>(course.getSections()
                .stream().map(section -> sectionService.getById(section.getId(), email)).toList());
        sections.forEach(sectionVM -> {
            int countCurriculumPerSection = sectionVM.curriculums().size();
            totalCurriculumCourse.addAndGet(countCurriculumPerSection);
        });
        sections.sort(Comparator.comparing(SectionVM::number));

        CourseVM courseVM = CourseVM.fromModel(course, sections ,0, 0.0, totalCurriculumCourse.get(),"", null, true, 0L );

        Optional<LearningLecture> maxAccessTimeByEmailAndCourseSlugLecture = learningLectureRepository.findMaxAccessTimeByEmailAndCourseSlug(email, slug);
        Optional<LearningQuiz> maxAccessTimeByEmailAndCourseSlugQuiz = learningQuizRepository.findMaxAccessTimeByEmailAndCourseSlug(email, slug);

        if (maxAccessTimeByEmailAndCourseSlugLecture.isPresent() && maxAccessTimeByEmailAndCourseSlugQuiz.isPresent()) {
            var lecture = maxAccessTimeByEmailAndCourseSlugLecture.get();
            var quiz = maxAccessTimeByEmailAndCourseSlugQuiz.get();
            if (lecture.getAccessTime().isAfter(quiz.getAccessTime())) {
                return new CourseLearningVm(courseVM, lecture.getLecture().getSection().getId(), lecture.getLecture().getId(), lecture.getWatchingSecond(), LECTURE_TYPE);
            } else {
                return new CourseLearningVm(courseVM, quiz.getQuiz().getSection().getId(), quiz.getQuiz().getId(), null, QUIZ_TYPE);
            }
        } else if (maxAccessTimeByEmailAndCourseSlugLecture.isPresent()) {
            var lecture = maxAccessTimeByEmailAndCourseSlugLecture.get();
            return new CourseLearningVm(courseVM, lecture.getLecture().getSection().getId(), lecture.getLecture().getId(), lecture.getWatchingSecond(), LECTURE_TYPE);
        } else if (maxAccessTimeByEmailAndCourseSlugQuiz.isPresent()) {
            var quiz = maxAccessTimeByEmailAndCourseSlugQuiz.get();
            return new CourseLearningVm(courseVM, quiz.getQuiz().getSection().getId(), quiz.getQuiz().getId(), null, QUIZ_TYPE);
        }
//        if (sections.size() == 0) {
//            throw new BadRequestException("course dont have lecture");
//        }
        Long sectionId = sections.get(0).id();
        Long curriculumId = sections.get(0).curriculums().get(0).getId();
        if (sections.get(0).curriculums().get(0).getType().equals(ECurriculumType.lecture)){
            return new CourseLearningVm(courseVM, sectionId ,curriculumId, 0, LECTURE_TYPE);
        }
        return new CourseLearningVm(courseVM, sectionId ,curriculumId, 0, QUIZ_TYPE);
    }

    @Override
    public List<CourseListGetVM> getByUserId(Long userId) {
        log.info("received userId: {}", userId);
        List<Course> courses = courseRepository.findByUserIdReturnSections(userId);
        List<CourseListGetVM> courseListGetVMS = courses.stream().map(course -> getCourseListGetVMById(course.getId())).toList();
        return courseListGetVMS;
    }

    @Override
    public PageableData<CourseListGetVM> getCoursesByMultiQuery(int pageNum,
                                                         int pageSize,
                                                         String title,
                                                         Float rating,
                                                         String[] level,
                                                         Boolean[] free,
                                                         String categoryName, Integer topicId
    ) {

        log.info("received pageNum: {}, pageSize: {}, title: {}, rating: {}, level: {}, free: {}, categoryName: {}, " +
                        "topicId: {}", pageNum, pageSize, title, rating, level, free, categoryName, topicId);
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Course> coursePage = courseRepositoryCustom.findByMultiFilter(title, rating, level, free, categoryName, topicId, pageable);
        List<Course> courses = coursePage.getContent();
        List<CourseListGetVM> courseListGetVMS = courses.stream().map(course -> {
            course = courseRepository.findByIdWithPromotions(course).orElseThrow(() -> new NotFoundException(Constants.ERROR_CODE.COURSE_NOT_FOUND));
            List<Review> reviews = course.getReviews();
            int ratingCount = reviews.size();
            Double averageRating = reviews.stream().map(review -> review.getRatingStar()).mapToDouble(Integer::doubleValue).average().orElse(0.0);
            double roundedAverageRating = Math.round(averageRating * 10) / 10.0;
            AtomicInteger totalCurriculumCourse = new AtomicInteger();
            AtomicInteger totalDurationCourse = new AtomicInteger();
            List<Section> sections = sectionService.findByCourseId(course.getId());
            sections.forEach(section -> {
                List<Lecture> lectures = section.getLectures();
                List<Quiz> quizzes = section.getQuizzes();
                totalCurriculumCourse.addAndGet(lectures.size());
                totalCurriculumCourse.addAndGet(quizzes.size());
                int totalSeconds = lectures.stream()
                        .mapToInt(lecture -> lecture.getDuration())
                        .sum();
                totalDurationCourse.addAndGet(totalSeconds);
            });
            String formattedHours = convertSeconds(totalDurationCourse.get());


            Set<Promotion> promotions = course.getPromotions();
            Long discountedPrice = getDiscountedPriceByCourse(promotions, course);
            return CourseListGetVM.fromModel(course, formattedHours, totalCurriculumCourse.get(), roundedAverageRating, ratingCount, discountedPrice);
        }).toList();
        return new PageableData(
                pageNum,
                pageSize,
                coursePage.getTotalElements(),coursePage.getTotalPages()
                ,
                courseListGetVMS
        );
    }

    @Override
    public List<CourseListGetVM> getCoursesByMultiQueryReturnList(int pageNum, int pageSize, String title, Float rating, String[] level, Boolean[] free, String categoryName, Integer topicId) {
        log.info("received pageNum: {}, pageSize: {}, title: {}, rating: {}, level: {}, free: {}, categoryName: {}, " +
                "topicId: {}", pageNum, pageSize, title, rating, level, free, categoryName, topicId);
        List<Course> courses = title != null ? courseRepository.findByMultiQueryWithKeyword(title, rating, level, free, categoryName, topicId) :
            courseRepository.findByMultiQuery(rating, level, free, categoryName, topicId);
        List<CourseListGetVM> courseListGetVMS = courses.stream().map(course -> {
            course = courseRepository.findByIdWithPromotions(course).orElseThrow(() -> new NotFoundException(Constants.ERROR_CODE.COURSE_NOT_FOUND));
            List<Review> reviews = course.getReviews();
            int ratingCount = reviews.size();
            Double averageRating = reviews.stream().map(review -> review.getRatingStar()).mapToDouble(Integer::doubleValue).average().orElse(0.0);
            double roundedAverageRating = Math.round(averageRating * 10) / 10.0;
            AtomicInteger totalCurriculumCourse = new AtomicInteger();
            AtomicInteger totalDurationCourse = new AtomicInteger();
            List<Section> sections = sectionService.findByCourseId(course.getId());
            sections.forEach(section -> {
                List<Lecture> lectures = section.getLectures();
                List<Quiz> quizzes = section.getQuizzes();
                totalCurriculumCourse.addAndGet(lectures.size());
                totalCurriculumCourse.addAndGet(quizzes.size());
                int totalSeconds = lectures.stream()
                        .mapToInt(lecture -> lecture.getDuration())
                        .sum();
                totalDurationCourse.addAndGet(totalSeconds);

            });
            Set<Promotion> promotions = course.getPromotions();
            Long discountedPrice = getDiscountedPriceByCourse(promotions, course);
        String formattedHours = convertSeconds(totalDurationCourse.get());
        return CourseListGetVM.fromModel(course, formattedHours, totalCurriculumCourse.get(), roundedAverageRating, ratingCount, discountedPrice);
        }).toList();
        return courseListGetVMS;
    }

    @Override
    public List<CourseListGetVM> getCoursesByCategoryId(Integer categoryId) {
        log.info("received categoryId: {}", categoryId);
        List<Course> courses = courseRepository.findByCategoryIdWithStatus(categoryId);
        List<CourseListGetVM> courseListGetVMS = courses.stream().map(course -> getCourseListGetVMById(course.getId())).toList();
        return courseListGetVMS;
    }

    @Override
    public void delete(Long id) {
        log.info("received courseId: {}", id);
        Course course = courseRepository.findByIdReturnSections(id).orElseThrow(() -> new NotFoundException(Constants.ERROR_CODE.COURSE_NOT_FOUND, id));
        if (course.getSections().size() > 0 ) {
            throw new BadRequestException(Constants.ERROR_CODE.COURSE_HAD_SECTION, id);
        }

        List<LearningCourse> learningCourses = learningCourseRepository.findByCourseId(id);


        if (learningCourses.size() > 0) {
            throw new BadRequestException(Constants.ERROR_CODE.COURSE_HAD_BEEN_BOUGHT, id);
        }

        List<Cart> carts = cartRepository.findByCourseId(id);
        if (carts.size() > 0) {
            throw new BadRequestException(Constants.ERROR_CODE.COURSE_HAD_CART, id);

        }

        List<OrderDetail> orderDetails = orderDetailRepository.findByCourseId(id);
        if (orderDetails.size() > 0) {
            throw new BadRequestException(Constants.ERROR_CODE.COURSE_HAD_BEEN_BOUGHT, id);
        }
        courseRepository.delete(course);
    }

    @Override
    public void updateStatusCourse(CourseStatusPostVM courseStatusPostVM, Long courseId) {
        log.info("received status: {}, courseId: {}", courseStatusPostVM, courseId);
        Course course = courseRepository.findById(courseId).orElseThrow();
        if (courseStatusPostVM.status().equals(CourseStatus.UNPUBLISHED) && courseStatusPostVM.reason() != null) {
            course.setReasonRefused(courseStatusPostVM.reason());
        }
        course.setStatus(courseStatusPostVM.status());
        courseRepository.saveAndFlush(course);
    }

    @Override
    public List<CourseAssignPromotion> getByPromotionId(Long promotionId) {
        List<Course> courses = courseRepository.findCoursesNotInPromotionToday(promotionId);
        List<CourseAssignPromotion> courseAssignPromotions = courses.stream().map(course -> {
            Set<Promotion> promotions = course.getPromotions();
            boolean isNotInPromotions = promotions.stream()
                    .noneMatch(promotion -> promotion.getId().equals(promotionId));

            return CourseAssignPromotion.fromModel(course, !isNotInPromotions);
        }).toList();
        return courseAssignPromotions;
    }

    private String convertSeconds(int seconds) {
        if (seconds < 3600) {
            int minutes = seconds / 60;
            int remainingSeconds = seconds % 60;
            return minutes + " phút " + remainingSeconds + " giây";
        } else {
            int hours = seconds / 3600;
            int minutes = (seconds % 3600) / 60;
            return hours + " giờ " + minutes + " phút";
        }
    }


}
