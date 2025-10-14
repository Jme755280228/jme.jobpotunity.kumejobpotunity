package jme.jobpotunity.kumejobpotunity.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "companies")
public class Company extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String contactEmail;

    @OneToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JobPosting> jobPostings = new HashSet<>();

    // Constructors
    public Company() {}

    public Company(String name, String description, String contactEmail, User owner) {
        this.name = name;
        this.description = description;
        this.contactEmail = contactEmail;
        this.owner = owner;
    }

    // Getters / Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }

    public Set<JobPosting> getJobPostings() { return jobPostings; }
    public void setJobPostings(Set<JobPosting> jobPostings) { this.jobPostings = jobPostings; }
}
