package jme.jobpotunity.kumejobpotunity.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email; 
    
    // 🎯 FIX 1.1: 'role' ကို Set<String> 'roles' သို့ ပြောင်းလဲခြင်း
    // User တစ်ယောက်တွင် Role တစ်ခုထက် ပိုရှိနိုင်သောကြောင့် (e.g., ADMIN, EMPLOYER)
    @ElementCollection(fetch = FetchType.EAGER) // User ကို ခေါ်တိုင်း Role တွေပါလာအောင် EAGER သုံးပါ
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    private Set<String> roles = new HashSet<>(); 

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ApplicantProfile profile;

    // --- Constructors ---
    public User() {
    }

    // --- Getters and Setters ---
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // 🎯 FIX 1.2: DataSeeder မှ ခေါ်ဆိုသော setRoles() method ကို ထည့်သွင်းခြင်း
    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public ApplicantProfile getProfile() {
        return profile;
    }

    public void setProfile(ApplicantProfile profile) {
        this.profile = profile;
    }
}
