package jme.jobpotunity.kumejobpotunity.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "job_applications")
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // JobPosting (Job Owner Company က တင်ထားတဲ့ အလုပ်)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private JobPosting job;

    // User (လျှောက်ထားသူရဲ့ User Account)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Data-Centric Change: Applicant's Structured Profile (Profile Data ကို တိုက်ရိုက်ချိတ်ဆက်)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_profile_id", nullable = false)
    private ApplicantProfile applicantProfile; 

    private LocalDate applicationDate;

    // Application ၏ အခြေအနေ (Employer များ စီမံခန့်ခွဲရန်အတွက်)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status; 

    // --- 1. Constructors ---

    public JobApplication() {
        this.applicationDate = LocalDate.now();
        this.status = ApplicationStatus.APPLIED; // Default status
    }

    // --- 2. Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JobPosting getJob() {
        return job;
    }

    public void setJob(JobPosting job) {
        this.job = job;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ApplicantProfile getApplicantProfile() {
        return applicantProfile;
    }

    public void setApplicantProfile(ApplicantProfile applicantProfile) {
        this.applicantProfile = applicantProfile;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
}
