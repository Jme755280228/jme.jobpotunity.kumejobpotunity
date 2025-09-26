package jme.jobpotunity.kumejobpotunity.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class JobPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String location;
    private String salary;
    private String jobType;

    @Column(name = "is_cv_required")
    private Boolean isCvRequired;

    // Data Ownership Change: Job Posting တင်သူ (Employer Role ရှိသော User)
    @ManyToOne
    @JoinColumn(name = "employer_user_id", nullable = false)
    private User employerUser; // <<<<< Field အသစ်

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    // --- 1. Constructors ---
    public JobPosting() {
    }

    // --- 2. Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Boolean getIsCvRequired() {
        return isCvRequired;
    }

    public void setIsCvRequired(Boolean isCvRequired) {
        this.isCvRequired = isCvRequired;
    }

    // New Getter and Setter for employerUser
    public User getEmployerUser() {
        return employerUser;
    }

    public void setEmployerUser(User employerUser) {
        this.employerUser = employerUser;
    }
}
