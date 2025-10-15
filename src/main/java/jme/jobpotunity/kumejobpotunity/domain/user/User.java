package jme.jobpotunity.kumejobpotunity.domain.user; // <-- package အသစ်

import jakarta.persistence.*;
import jme.jobpotunity.kumejobpotunity.domain.base.BaseEntity;
import jme.jobpotunity.kumejobpotunity.domain.enums.UserRole; // <-- import အသစ်
import jme.jobpotunity.kumejobpotunity.domain.job.Company;   // <-- import path အသစ်

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
    @Enumerated(EnumType.STRING) // <-- Enum အတွက် Annotation ထပ်ထည့်
    @Column(name = "role")
    private Set<UserRole> roles = new HashSet<>(); // <-- String ကနေ UserRole enum သို့ပြောင်း

    private String fullName;
    private String phoneNumber;
    private String profileImage;

    // Relationships
    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Company company;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private ApplicantProfile applicantProfile;

    // Constructors
    public User() {}

    public User(String email, String password, Set<UserRole> roles) { // <-- Type ပြောင်း
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<UserRole> getRoles() { // <-- Return Type ပြောင်း
        return roles;
    }

    public void setRoles(Set<UserRole> roles) { // <-- Parameter Type ပြောင်း
        this.roles = roles;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public ApplicantProfile getApplicantProfile() {
        return applicantProfile;
    }

    public void setApplicantProfile(ApplicantProfile applicantProfile) {
        this.applicantProfile = applicantProfile;
    }
}

