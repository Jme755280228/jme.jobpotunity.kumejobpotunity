package jme.jobpotunity.kumejobpotunity.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import java.util.List;
import java.util.ArrayList;

@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String industry;
    private String location;

    // 🎯 FIX 2.1: DataSeeder မှ လိုအပ်သော description field ကို ထည့်သွင်းခြင်း
    private String description;

    // 🎯 FIX 2.2: DataSeeder မှ လိုအပ်သော contactEmail field ကို ထည့်သွင်းခြင်း
    private String contactEmail;

    @OneToMany(mappedBy = "company")
    private List<JobPosting> jobPostings = new ArrayList<>();

    // --- Constructors ---
    public Company() {
    }

    public Company(String name, String industry, String location) {
        this.name = name;
        this.industry = industry;
        this.location = location;
    }

    // --- Getters and Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // 🎯 FIX 2.3: description အတွက် Getter နှင့် Setter
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // 🎯 FIX 2.4: contactEmail အတွက် Getter နှင့် Setter
    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    // jobPostings အတွက် Getter နှင့် Setter
    public List<JobPosting> getJobPostings() {
        return jobPostings;
    }

    public void setJobPostings(List<JobPosting> jobPostings) {
        this.jobPostings = jobPostings;
    }
}
