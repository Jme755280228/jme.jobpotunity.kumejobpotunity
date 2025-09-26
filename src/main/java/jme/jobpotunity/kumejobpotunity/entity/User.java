package jme.jobpotunity.kumejobpotunity.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    // Role ကို "ADMIN", "USER", "EMPLOYER" တွေအတွက် လက်ခံနိုင်ရန်
    @Column(nullable = false)
    private String role; 

    // Data-Centric Change: User Account တစ်ခုသည် Applicant Profile တစ်ခုနှင့်သာ သက်ဆိုင်သည်
    // mappedBy = "user" သည် ApplicantProfile Entity မှ ချိတ်ဆက်မှုကို ပြန်ညွှန်းသည်
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ApplicantProfile applicantProfile; // <<<<< Field အသစ်

    // --- 1. Constructors ---
    public User() {
    }

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // --- 2. Getters and Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // New Getter and Setter for ApplicantProfile
    public ApplicantProfile getApplicantProfile() {
        return applicantProfile;
    }

    public void setApplicantProfile(ApplicantProfile applicantProfile) {
        this.applicantProfile = applicantProfile;
    }
}
