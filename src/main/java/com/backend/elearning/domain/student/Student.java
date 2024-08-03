package com.backend.elearning.domain.student;

import com.backend.elearning.domain.common.AbstractAuditEntity;
import com.backend.elearning.domain.common.CustomAuditingEntityListener;
import com.backend.elearning.domain.learning.learningCourse.LearningCourse;
import com.backend.elearning.domain.user.EGender;
import com.backend.elearning.domain.user.ERole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "student")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EntityListeners(value = CustomAuditingEntityListener.class)
public class Student extends AbstractAuditEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 50)
    private String email;

    private String password;

    @Column(length = 20)
    private String firstName;

    @Column(length = 20)
    private String lastName;

    @Enumerated(EnumType.STRING)
    private EGender gender;
    private boolean active;

    private String photo;

    private LocalDate dateOfBirth;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<LearningCourse> learningCourses = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority(ERole.ROLE_STUDENT.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
