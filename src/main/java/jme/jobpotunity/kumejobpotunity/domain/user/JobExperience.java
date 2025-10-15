package jme.jobpotunity.kumejobpotunity.domain.user; // <-- package အသစ်

import jakarta.persistence.*;
import jme.jobpotunity.kumejobpotunity.domain.base.BaseEntity;

@Entity
@Table(name = "job_experiences")
public class JobExperience extends BaseEntity {

    private String jobTitle;
    private String companyName;
    private String startDate;
    private String endDate;
    @Column(length = 2000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_profile_id")
    private ApplicantProfile applicantProfile;

    // Constructors
    public JobExperience() {}

    // Getters and Setters
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ApplicantProfile getApplicantProfile() {
        return applicantProfile;
    }

    public void setApplicantProfile(ApplicantProfile applicantProfile) {
        this.applicantProfile = applicantProfile;
    }
}

