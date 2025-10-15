package jme.jobpotunity.kumejobpotunity.domain.job; // <-- package အသစ်

import jakarta.persistence.*;
import jme.jobpotunity.kumejobpotunity.domain.application.JobApplication; // <-- import path အသစ်
import jme.jobpotunity.kumejobpotunity.domain.base.BaseEntity;
import jme.jobpotunity.kumejobpotunity.domain.user.User; // <-- import path အသစ်

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "job_postings")
public class JobPosting extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(length = 5000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id")
    private User employer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "jobPosting", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JobApplication> applications = new HashSet<>();

    @Column(nullable = false)
    private boolean isApproved = false;

    @Column(nullable = false)
    private LocalDateTime postedDate = LocalDateTime.now();

    // Constructors
    public JobPosting() {}

    public JobPosting(String title, String description, User employer, Company company) {
        this.title = title;
        this.description = description;
        this.employer = employer;
        this.company = company;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getEmployer() {
        return employer;
    }

    public void setEmployer(User employer) {
        this.employer = employer;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Set<JobApplication> getApplications() {
        return applications;
    }

    public void setApplications(Set<JobApplication> applications) {
        this.applications = applications;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public LocalDateTime getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDateTime postedDate) {
        this.postedDate = postedDate;
    }
}

