package com.yourprojectname.model;

import jakarta.persistence.*;

@Entity
@Table(name = "applicant_education")
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many Education entries can belong to One ApplicantProfile
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_profile_id", nullable = false)
    private ApplicantProfile applicantProfile;
    
    // ရရှိသောဘွဲ့ သို့မဟုတ် အောင်လက်မှတ်အမည် (ဥပမာ: B.Sc. CS, Diploma in IT)
    @Column(nullable = false)
    private String degree;

    // ကျောင်း/တက္ကသိုလ်အမည်
    @Column(nullable = false)
    private String schoolName;
    
    // ဘာသာရပ် (ဥပမာ: Computer Science, Business Administration)
    private String major;
    
    // ဘွဲ့ရရှိသည့် ခုနှစ် သို့မဟုတ် ပြီးဆုံးသည့် ခုနှစ်
    private Integer graduationYear;
    
    // GPA သို့မဟုတ် ရမှတ် (Optional)
    private Double gpa; 

    // --- 1. Constructors ---

    // No-Args Constructor (JPA requirement)
    public Education() {
    }

    // All-Args Constructor 
    public Education(ApplicantProfile applicantProfile, String degree, String schoolName, String major, Integer graduationYear, Double gpa) {
        this.applicantProfile = applicantProfile;
        this.degree = degree;
        this.schoolName = schoolName;
        this.major = major;
        this.graduationYear = graduationYear;
        this.gpa = gpa;
    }

    // --- 2. Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApplicantProfile getApplicantProfile() {
        return applicantProfile;
    }

    public void setApplicantProfile(ApplicantProfile applicantProfile) {
        this.applicantProfile = applicantProfile;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public Integer getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(Integer graduationYear) {
        this.graduationYear = graduationYear;
    }

    public Double getGpa() {
        return gpa;
    }

    public void setGpa(Double gpa) {
        this.gpa = gpa;
    }
}
