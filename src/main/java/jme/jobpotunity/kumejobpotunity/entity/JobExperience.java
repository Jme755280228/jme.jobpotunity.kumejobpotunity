package jme.jobpotunity.kumejobpotunity.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "job_experiences")
public class JobExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many JobExperiences can belong to One ApplicantProfile
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_profile_id", nullable = false)
    private ApplicantProfile applicantProfile;
    
    @Column(nullable = false)
    private String jobTitle;

    @Column(nullable = false)
    private String companyName;
    
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    @Lob // For long text/paragraph
    private String description;

    // --- 1. Constructors ---

    // No-Args Constructor (JPA requirement)
    public JobExperience() {
    }

    // All-Args Constructor (Optional, but helpful)
    public JobExperience(ApplicantProfile applicantProfile, String jobTitle, String companyName, LocalDate startDate, LocalDate endDate, String description) {
        this.applicantProfile = applicantProfile;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
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

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
