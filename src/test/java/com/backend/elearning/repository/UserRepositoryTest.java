package com.backend.elearning.repository;

import com.backend.elearning.TestConfig;
import com.backend.elearning.domain.user.User;
import com.backend.elearning.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({TestConfig.class})
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;



    @Test
    void testCountByExistedEmail_WithExistingEmail() {
        // Given
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("user1@example.com");
        userRepository.saveAndFlush(user1);

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("user2@example.com");
        userRepository.saveAndFlush(user2);


        String email = "user1@example.com";
        Long id = null;

        // When
        Long count = userRepository.countByExistedEmail(email, id);

        // Then
        assertEquals(1L, count);  // Expect 1 since user1@example.com exists
    }

    @Test
    void testCountByExistedEmail_WithDifferentId() {
        // Given
        String email = "user1@example.com";

        // When
        Long count = userRepository.countByExistedEmail(email, 1L);

        // Then
        assertEquals(0L, count);  // Expect 0 since it's the same user ID
    }

    @Test
    void testFindAllCustom_WithKeyword() {
        // Given

        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("user1@example.com");
        userRepository.saveAndFlush(user1);

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("user2@example.com");
        userRepository.saveAndFlush(user2);

        String keyword = "user";
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<User> result = userRepository.findAllCustom(pageable, keyword);

        // Then
        assertEquals(2, result.getTotalElements());  // Expect 2 since both users contain "user" in their email
    }

    @Test
    void testFindAllCustom_WithNoMatchKeyword() {
        // Given
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("user1@example.com");
        userRepository.saveAndFlush(user1);

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("user2@example.com");
        userRepository.saveAndFlush(user2);
        String keyword = "nonexistent";
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<User> result = userRepository.findAllCustom(pageable, keyword);

        // Then
        assertEquals(0, result.getTotalElements());  // Expect 0 since no user email matches "nonexistent"
    }

    @Test
    @Transactional
    @DirtiesContext
    void testFindByIdCustom_WithExistingUser() {
        User user1 = new User();
        user1.setEmail("user1@example.com");
        User savedUser = userRepository.save(user1);  // Save and get the assigned ID
        Long id = savedUser.getId();

        // When
        Optional<User> result = userRepository.findByIdCustom(id);

        // Then
        assertTrue(result.isPresent(), "User should be present for the given ID");
    }

    @Test
    void testFindByIdCustom_WithNonExistingUser() {
        // Given

        Long id = 999L;

        // When
        Optional<User> result = userRepository.findByIdCustom(id);

        // Then
        assertFalse(result.isPresent());  // Expect false since no user with ID 999 exists
    }
}
