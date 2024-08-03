package com.backend.elearning.domain.user;

import com.backend.elearning.domain.course.CourseListGetVM;

import java.util.ArrayList;
import java.util.List;

public class UserGetDetailVm
{
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private String headline;

    private String gender;
    private boolean active;
    private String photo;
    private String dateOfBirth;
    private String role;
    private int numberOfReview;
    private int numberOfStudent;
    private List<CourseListGetVM> courses = new ArrayList<>();

    public UserGetDetailVm() {
    }

    public UserGetDetailVm(Long id, String email, String firstName, String lastName, String gender, boolean active, String photoURL, String dateOfBirth, String role) {
        this.id = id;
        this.email = email;
        this.fullName = firstName.concat(" ").concat(lastName);
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.active = active;
        this.photo = photoURL;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
    }

    public UserGetDetailVm(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.fullName = firstName.concat(" ").concat(lastName);
        this.headline = user.getHeadline() != null ? user.getHeadline() :  "";
        this.gender = user.getGender().name();
        this.active = user.isActive();
        this.photo = user.getPhoto();
        String dateOfBirth = user.getDateOfBirth() != null ? user.getDateOfBirth().toString() : "";
        this.dateOfBirth = dateOfBirth;
        this.role = user.getRole().name();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public int getNumberOfReview() {
        return numberOfReview;
    }

    public void setNumberOfReview(int numberOfReview) {
        this.numberOfReview = numberOfReview;
    }

    public int getNumberOfStudent() {
        return numberOfStudent;
    }

    public void setNumberOfStudent(int numberOfStudent) {
        this.numberOfStudent = numberOfStudent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<CourseListGetVM> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseListGetVM> courses) {
        this.courses = courses;
    }
}
