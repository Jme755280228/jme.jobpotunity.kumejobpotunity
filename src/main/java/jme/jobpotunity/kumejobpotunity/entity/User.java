package jme.jobpotunity.kumejobpotunity.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();

    private String fullName;
    private String phoneNumber;
    private String profileImage;

    // Relationships
    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL)
    private Company company;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private ApplicantProfile applicantProfile;

    // Constructors
    public User() {}

    public User(String email, String password, Set<String> roles) {
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    // Getters / Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }

    public ApplicantProfile getApplicantProfile() { return applicantProfile; }
    public void setApplicantProfile(ApplicantProfile applicantProfile) { this.applicantProfile = applicantProfile; }
}
