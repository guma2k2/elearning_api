package com.backend.elearning.repository;

import com.backend.elearning.TestConfig;
import com.backend.elearning.domain.cart.Cart;
import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.course.CourseRepository;
import com.backend.elearning.domain.student.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({TestConfig.class})
public class CourseRepositoryTest {

    @Autowired
    private CourseRepository underTest ;

    @Autowired
    private TestEntityManager entityManager;
//    @Test
//    @Transactional
//    @DirtiesContext
//        // Reload context to ensure a clean state
//    void canUpdateStatusCourseById() {
//        // given
//        Long courseId = 1L;
//        boolean expected = true;
//
//        Course course = Course.builder().id(courseId).title("Course title").publish(false).build();
//
//        underTest.saveAndFlush(course);
//
//        // when
//        underTest.updateStatusCourse(expected, course.getId());
//        underTest.flush();
//
//        // then
//        Optional<Course> courseOptional = underTest.findById(course.getId());
//        assertThat(courseOptional)
//                .isPresent()
//                .hasValueSatisfying(c -> {
//                    entityManager.refresh(c);
//                    assertThat(c.isPublish()).isEqualTo(expected);
//                });
//    }
}
