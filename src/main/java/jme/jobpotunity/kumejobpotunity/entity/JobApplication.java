package jme.jobpotunity.kumejobpotunity.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "job_applications")
public class JobApplication extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_profile_id", nullable = false)
    private ApplicantProfile applicantProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_posting_id", nullable = false)
    private JobPosting jobPosting;

    @OneToMany(mappedBy = "jobApplication", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApplicationField> fields = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.PENDING;

    @Column(nullable = false)
    private LocalDateTime appliedAt = LocalDateTime.now();

    // Constructors
    public JobApplication() {}

    public JobApplication(ApplicantProfile applicantProfile, JobPosting jobPosting) {
        this.applicantProfile = applicantProfile;
        this.jobPosting = jobPosting;
        this.appliedAt = LocalDateTime.now();
    }

    // Getters / Setters
    public ApplicantProfile getApplicantProfile() { return applicantProfile; }
    public void setApplicantProfile(ApplicantProfile applicantProfile) { this.applicantProfile = applicantProfile; }

    public JobPosting getJobPosting() { return jobPosting; }
    public void setJobPosting(JobPosting jobPosting) { this.jobPosting = jobPosting; }

    public List<ApplicationField> getFields() { return fields; }
    public void setFields(List<ApplicationField> fields) { this.fields = fields; }

    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }

    public LocalDateTime getAppliedAt() { return appliedAt; }
    public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; }
}
