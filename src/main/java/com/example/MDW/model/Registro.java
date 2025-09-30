package com.example.MDW.model;

import java.time.LocalDate;

public class Registro {
    private Long id;
    private Long courseId;
    private String userId;
    private LocalDate registrationDate;

    public Registro() { }

    public Registro(Long id, Long courseId, String userId, LocalDate registrationDate) {
        this.id = id;
        this.courseId = courseId;
        this.userId = userId;
        this.registrationDate = registrationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }
}
