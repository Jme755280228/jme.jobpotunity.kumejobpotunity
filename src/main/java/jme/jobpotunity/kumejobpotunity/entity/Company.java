package jme.jobpotunity.kumejobpotunity.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String industry;
    private String location;

    // A company can have MANY job postings.
    // 'mappedBy' attribute indicates that the 'company' field in the JobPosting class is the owner of this relationship.
    @OneToMany(mappedBy = "company")
    private List<JobPosting> jobPostings;

    public Company() {
    }

    public Company(String name, String industry, String location) {
        this.name = name;
        this.industry = industry;
        this.location = location;
    }

    // Getters and Setters
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
}

