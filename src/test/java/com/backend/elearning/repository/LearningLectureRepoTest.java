package com.backend.elearning.repository;

import com.backend.elearning.TestConfig;
import com.backend.elearning.domain.learning.learningLecture.LearningLecture;
import com.backend.elearning.domain.learning.learningLecture.LearningLectureRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
@Import({TestConfig.class})
public class LearningLectureRepoTest {

    @Autowired
    private LearningLectureRepository underTest;

    @Test
    public void canFindById () {
        // given
        Long id = 1L;

        LearningLecture learningLecture = LearningLecture.builder().id(id).build();
        underTest.save(learningLecture);
        // when
        var result = underTest.findById(id);
        // then
        assertThat(result).isPresent();
    }
}
