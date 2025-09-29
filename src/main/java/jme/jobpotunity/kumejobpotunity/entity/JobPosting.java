// src/main/java/.../entity/JobPosting.java (Final Code)

package jme.jobpotunity.kumejobpotunity.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Lob; 
import jakarta.persistence.Table; 

import java.time.LocalDate;

@Entity
@Table(name = "job_postings")
public class JobPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    // Description ကို @Lob အဖြစ်ထားခြင်းက long text အတွက် ပိုသင့်လျော်သည်
    @Lob
    private String description;

    private String location;
    
    // salaryRange အစား salary field ကိုသာ အသုံးပြုထားသည်
    private String salary; 
    
    private String jobType; // Full-time, Part-time, Contract

    // 💡 NEW FIELD: Filtering အတွက် Category ထပ်ထည့်ခြင်း
    private String category; // e.g., "Software Development", "Marketing", "HR"

    // Job Posting တင်သည့် နေ့စွဲ
    @Column(nullable = false)
    private LocalDate postedDate; 
    
    // Job ၏ Active Status (Public Listing အတွက်)
    @Column(nullable = false)
    private Boolean isActive = true; 

    // CV File တင်ဖို့ လိုအပ်လား/မလိုအပ်ဘူးလား (Profile-based Application အတွက် သုံးနိုင်)
    @Column(name = "is_cv_required")
    private Boolean isCvRequired = false; // Default: false (Structured Profile ကို အားပေးရန်)

    // Data Ownership: Job Posting တင်သူ (Employer Role ရှိသော User)
    @ManyToOne
    @JoinColumn(name = "employer_user_id", nullable = false)
    private User employerUser;

    // Job Posting ၏ Company
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    // --- 1. Constructors ---
    public JobPosting() {
    }

    // --- 2. Getters and Setters ---

    //... (Existing Getters and Setters for id, title, description, location, salary, jobType) ...
    
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

    // 💡 NEW Getter/Setter for Category
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    
    //... (Existing Getters and Setters for company, isCvRequired, employerUser, postedDate, isActive) ...
    
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

    public User getEmployerUser() {
        return employerUser;
    }

    public void setEmployerUser(User employerUser) {
        this.employerUser = employerUser;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}

