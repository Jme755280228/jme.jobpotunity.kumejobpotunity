package jme.jobpotunity.kumejobpotunity.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "applicant_profiles")
public class ApplicantProfile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @OneToMany(mappedBy = "applicantProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Education> educations = new ArrayList<>();

    @OneToMany(mappedBy = "applicantProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobExperience> jobExperiences = new ArrayList<>();

    @Column(nullable = false)
    private String fullName;

    @Column(length = 20)
    private String phone;

    @Column(length = 250)
    private String address;

    @Column(length = 500)
    private String summary;

    // Constructors
    public ApplicantProfile() {}

    public ApplicantProfile(User user, String fullName) {
        this.user = user;
        this.fullName = fullName;
    }

    // Getters / Setters
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public List<Education> getEducations() { return educations; }
    public void setEducations(List<Education> educations) { this.educations = educations; }

    public List<JobExperience> getJobExperiences() { return jobExperiences; }
    public void setJobExperiences(List<JobExperience> jobExperiences) { this.jobExperiences = jobExperiences; }
}
