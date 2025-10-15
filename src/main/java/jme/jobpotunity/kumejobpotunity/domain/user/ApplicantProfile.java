package jme.jobpotunity.kumejobpotunity.domain.user; // <-- package အသစ်

import jakarta.persistence.*;
import jme.jobpotunity.kumejobpotunity.domain.base.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "applicant_profiles")
public class ApplicantProfile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private String headline;
    private String summary;
    private String resumePath; // CV file path

    @OneToMany(mappedBy = "applicantProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Education> educations = new ArrayList<>();

    @OneToMany(mappedBy = "applicantProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobExperience> jobExperiences = new ArrayList<>();

    // Constructors
    public ApplicantProfile() {}

    public ApplicantProfile(User user) {
        this.user = user;
    }

    // Getters and Setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getResumePath() {
        return resumePath;
    }

    public void setResumePath(String resumePath) {
        this.resumePath = resumePath;
    }

    public List<Education> getEducations() {
        return educations;
    }

    public void setEducations(List<Education> educations) {
        this.educations = educations;
    }

    public List<JobExperience> getJobExperiences() {
        return jobExperiences;
    }

    public void setJobExperiences(List<JobExperience> jobExperiences) {
        this.jobExperiences = jobExperiences;
    }
}

