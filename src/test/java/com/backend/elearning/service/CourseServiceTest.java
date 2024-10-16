package com.backend.elearning.service;

import com.backend.elearning.domain.auth.AuthenticationService;
import com.backend.elearning.domain.cart.Cart;
import com.backend.elearning.domain.cart.CartRepository;
import com.backend.elearning.domain.category.Category;
import com.backend.elearning.domain.category.CategoryRepository;
import com.backend.elearning.domain.course.*;
import com.backend.elearning.domain.learning.learningCourse.LearningCourse;
import com.backend.elearning.domain.learning.learningCourse.LearningCourseRepository;
import com.backend.elearning.domain.learning.learningLecture.LearningLectureRepository;
import com.backend.elearning.domain.learning.learningQuiz.LearningQuizRepository;
import com.backend.elearning.domain.lecture.LectureRepository;
import com.backend.elearning.domain.order.OrderDetail;
import com.backend.elearning.domain.order.OrderDetailRepository;
import com.backend.elearning.domain.quiz.QuizRepository;
import com.backend.elearning.domain.review.ReviewService;
import com.backend.elearning.domain.section.Section;
import com.backend.elearning.domain.section.SectionService;
import com.backend.elearning.domain.topic.Topic;
import com.backend.elearning.domain.topic.TopicRepository;
import com.backend.elearning.domain.user.User;
import com.backend.elearning.domain.user.UserRepository;
import com.backend.elearning.domain.user.UserService;
import com.backend.elearning.domain.user.UserVm;
import com.backend.elearning.exception.BadRequestException;
import com.backend.elearning.exception.DuplicateException;
import com.backend.elearning.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private TopicRepository topicRepository;
    @Mock
    private SectionService sectionService;
    @Mock
    private QuizRepository quizRepository;
    @Mock
    private LectureRepository lectureRepository ;
    @Mock
    private ReviewService reviewService;
    @Mock
    private LearningLectureRepository learningLectureRepository;
    @Mock
    private LearningQuizRepository learningQuizRepository;
    @Mock
    private LearningCourseRepository learningCourseRepository;
    @Mock
    private OrderDetailRepository orderDetailRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    private CourseService courseService ;

   @Mock
   private SecurityContext securityContext;

   @Mock
   private Authentication authentication;
    @BeforeEach
    void beforeEach() {
        courseService = new CourseServiceImpl(courseRepository, categoryRepository, topicRepository, sectionService,
                quizRepository, lectureRepository, reviewService, learningLectureRepository, learningQuizRepository, learningCourseRepository, orderDetailRepository, cartRepository
        ,userService, userRepository);
       SecurityContextHolder.setContext(securityContext);

    }

   @Test
   void givenValidCoursePostVM_whenCreateCourse_thenCourseShouldBeCreated() {
      // Given
      CoursePostVM coursePostVM = new CoursePostVM(
              null, "Test Course", "Course Headline",
              new String[]{"Objective 1", "Objective 2"},
              new String[]{"Requirement 1"},
              new String[]{"Target Audience 1"},
              "Course Description", ELevel.AllLevel.name(),
              100000L, "image.png", false,
              1, 1
      );
      String email = "test@example.com";
      User user = User.builder()
              .firstName("first name")
              .lastName("last name")
              .email(email)
              .build();
      Topic topic = Topic.builder().id(1).name("topic").build();
      Category category = Category.builder().id(1).name("cat name").parent(null).build();


      Course course = Course.builder()
              .id(1L)
              .title("Test Course")
              .slug("java-programming-basics")
              .headline("Course Headline")
              .objectives(new String[]{"Objective 1", "Objective 2"})
              .requirements(new String[]{"Requirement 1"})
              .targetAudiences(new String[]{"Target Audience 1"})
              .description(coursePostVM.description())
              .imageId(coursePostVM.image())
              .level(ELevel.Beginner)
              .free(true)
              .status(CourseStatus.PUBLISHED)
              .price(100000L)
              .user(user)  // Assuming `user` is a pre-existing User object
              .topic(topic)  // Assuming `topic` is a pre-existing Topic object
              .category(category)  // Assuming `category` is a pre-existing Category object
              .build();

      when(securityContext.getAuthentication()).thenReturn(authentication);
      when(authentication.getName()).thenReturn(email);
      given(courseRepository.countExistByTitle(coursePostVM.title(), null)).willReturn(0L);
      given(categoryRepository.findById(coursePostVM.categoryId())).willReturn(Optional.of(new Category()));
      given(topicRepository.findById(coursePostVM.topicId())).willReturn(Optional.of(new Topic()));
      given(userService.getByEmail(anyString())).willReturn(user);
      given(courseRepository.save(Mockito.any(Course.class))).willReturn(course);

      // When
      CourseVM courseVM = courseService.create(coursePostVM);

      // Then
      assertNotNull(courseVM);
      assertEquals("Test Course", courseVM.title());
   }

   @Test
   void givenCoursePostVMWithExistingTitle_whenCreateCourse_thenThrowDuplicateException() {
      // Given
      CoursePostVM coursePostVM = new CoursePostVM(
              null, "Existing Course", "Course Headline",
              new String[]{"Objective 1", "Objective 2"},
              new String[]{"Requirement 1"},
              new String[]{"Target Audience 1"},
              "Course Description", "Beginner",
              100L, "image.png", false,
              1, 1
      );
      String email = "test@example.com";
      when(securityContext.getAuthentication()).thenReturn(authentication);
      when(authentication.getName()).thenReturn(email);
      given(courseRepository.countExistByTitle(coursePostVM.title(), null)).willReturn(1L);

      // When/Then
      assertThrows(DuplicateException.class, () -> courseService.create(coursePostVM));
   }

    @Test
    void testUpdateCourse_Success() {
        // Given

        Long userId = 1L;
        String email = "test@example.com";
        User user = User.builder()
                .id(userId)
                .firstName("first name")
                .lastName("last name")
                .email(email)
                .build();
        Long courseId = 1L;

        Category category = new Category();
        category.setId(2);
        category.setName("cat name");
        category.setParent(null);

        Topic topic = new Topic();
        topic.setId(3);
        topic.setName("topic name");

        Course oldCourse = new Course();
        oldCourse.setId(courseId);
        oldCourse.setTitle("Old Title");
        oldCourse.setCategory(new Category());
        oldCourse.getCategory().setId(1);
        oldCourse.setTopic(new Topic());
        oldCourse.getTopic().setId(2);
        oldCourse.setFree(false);
        oldCourse.setPrice(200L);
        oldCourse.setUser(user);
        oldCourse.setStatus(CourseStatus.PUBLISHED);

        CoursePostVM coursePutVM = new CoursePostVM(
                courseId,
                "New Title",
                "New Headline",
                new String[]{"Objective 1", "Objective 2"},
                new String[]{"Requirement 1"},
                new String[]{"Target Audience 1"},
                "New Description",
                ELevel.AllLevel.name(),
                300L,
                "new-image.png",
                false,
                2,
                3
        );

        // When
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        when(authentication.getName()).thenReturn(email);
        when(courseRepository.findByIdReturnSections(courseId)).thenReturn(Optional.of(oldCourse));
        when(courseRepository.countExistByTitle(coursePutVM.title(), courseId)).thenReturn(0L);
        when(categoryRepository.findById(coursePutVM.categoryId())).thenReturn(Optional.of(category));
        when(topicRepository.findById(coursePutVM.topicId())).thenReturn(Optional.of(topic));
        when(courseRepository.save(oldCourse)).thenReturn(oldCourse);

        CourseVM result = courseService.update(coursePutVM, userId, courseId);

        // Then
        assertNotNull(result);
        assertEquals(coursePutVM.title(), result.title());
        assertEquals(coursePutVM.headline(), result.headline());
        assertArrayEquals(coursePutVM.objectives(), result.objectives());
        assertArrayEquals(coursePutVM.requirements(), result.requirements());
        assertArrayEquals(coursePutVM.targetAudiences(), result.targetAudiences());
        assertEquals(coursePutVM.description(), result.description());
        assertEquals(coursePutVM.price(), result.price());
        assertEquals(coursePutVM.image(), result.image());

        verify(courseRepository).findByIdReturnSections(courseId);
        verify(courseRepository).countExistByTitle(coursePutVM.title(), courseId);
        verify(categoryRepository).findById(coursePutVM.categoryId());
        verify(topicRepository).findById(coursePutVM.topicId());
        verify(courseRepository).save(oldCourse);
    }


    @Test
    void delete_shouldThrowBadRequestException_whenCourseHasSections() {
        // Given
        Course course = new Course();
        course.setId(1L);
        course.setSections(Collections.singletonList(new Section())); // Course has a section
        when(courseRepository.findByIdReturnSections(anyLong())).thenReturn(Optional.of(course));

        // When & Then
        assertThrows(BadRequestException.class, () -> courseService.delete(1L));
    }

    @Test
    void delete_shouldThrowBadRequestException_whenCourseHasLearningCourses() {
        // Given
        Course course = new Course();
        course.setId(1L);
        course.setSections(Collections.emptyList()); // No sections
        when(courseRepository.findByIdReturnSections(anyLong())).thenReturn(Optional.of(course));
        when(learningCourseRepository.findByCourseId(anyLong())).thenReturn(Collections.singletonList(new LearningCourse()));

        // When & Then
        assertThrows(BadRequestException.class, () -> courseService.delete(1L));
    }

    @Test
    void delete_shouldThrowBadRequestException_whenCourseIsInCarts() {
        // Given
        Course course = new Course();
        course.setId(1L);
        course.setSections(Collections.emptyList()); // No sections
        when(courseRepository.findByIdReturnSections(anyLong())).thenReturn(Optional.of(course));
        when(learningCourseRepository.findByCourseId(anyLong())).thenReturn(Collections.emptyList());
        when(cartRepository.findByCourseId(anyLong())).thenReturn(Collections.singletonList(new Cart()));

        // When & Then
        assertThrows(BadRequestException.class, () -> courseService.delete(1L));
    }

    @Test
    void delete_shouldThrowBadRequestException_whenCourseHasOrderDetails() {
        // Given
        Course course = new Course();
        course.setId(1L);
        course.setSections(Collections.emptyList()); // No sections
        when(courseRepository.findByIdReturnSections(anyLong())).thenReturn(Optional.of(course));
        when(learningCourseRepository.findByCourseId(anyLong())).thenReturn(Collections.emptyList());
        when(cartRepository.findByCourseId(anyLong())).thenReturn(Collections.emptyList());
        when(orderDetailRepository.findByCourseId(anyLong())).thenReturn(Collections.singletonList(new OrderDetail()));

        // When & Then
        assertThrows(BadRequestException.class, () -> courseService.delete(1L));
    }

    @Test
    void delete_shouldDeleteCourse_whenAllConditionsAreMet() {
        // Given
        Course course = new Course();
        course.setId(1L);
        course.setSections(Collections.emptyList()); // No sections
        when(courseRepository.findByIdReturnSections(anyLong())).thenReturn(Optional.of(course));
        when(learningCourseRepository.findByCourseId(anyLong())).thenReturn(Collections.emptyList());
        when(cartRepository.findByCourseId(anyLong())).thenReturn(Collections.emptyList());
        when(orderDetailRepository.findByCourseId(anyLong())).thenReturn(Collections.emptyList());

        // When
        courseService.delete(1L);

        // Then
        verify(courseRepository).delete(course);
    }
}
