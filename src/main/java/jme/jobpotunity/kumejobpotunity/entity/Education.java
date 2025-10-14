package jme.jobpotunity.kumejobpotunity.entity;

import jakarta.persistence.*;

/**
 * JobExperience Entity
 * -------------------------
 * ✅ Belongs to ApplicantProfile (Many-to-One)
 * ✅ Inherits common fields from BaseEntity
 * ✅ Contains companyName, position, startYear, endYear
 */
@Entity
@Table(name = "job_experiences")
public class JobExperience extends BaseEntity {

    // ========================
    // 🔗 RELATIONSHIP
    // ========================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_profile_id", nullable = false)
    private ApplicantProfile applicantProfile;

    // ========================
    // Fields
    // ========================
    @Column(nullable = false)
    private String companyName;

    @Column(length = 100)
    private String position;

    @Column
    private int startYear;

    @Column
    private int endYear;

    // ========================
    // Constructors
    // ========================
    public JobExperience() {}

    public JobExperience(ApplicantProfile applicantProfile, String companyName, String position, int startYear, int endYear) {
        this.applicantProfile = applicantProfile;
        this.companyName = companyName;
        this.position = position;
        this.startYear = startYear;
        this.endYear = endYear;
    }

    // ========================
    // Getters & Setters
    // ========================
    public ApplicantProfile getApplicantProfile() {
        return applicantProfile;
    }

    public void setApplicantProfile(ApplicantProfile applicantProfile) {
        this.applicantProfile = applicantProfile;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }
}
