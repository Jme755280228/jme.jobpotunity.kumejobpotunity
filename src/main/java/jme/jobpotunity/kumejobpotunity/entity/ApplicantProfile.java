package jme.jobpotunity.kumejobpotunity.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "applicant_profiles")
public class ApplicantProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One-to-One: Applicant Profile တစ်ခုသည် User Account တစ်ခုနှင့်သာ သက်ဆိုင်သည်
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    // ပင်မ ကိုယ်ရေးအချက်အလက်များ
    @Column(nullable = false)
    private String fullName;
    private String email;
    private String phone;
    private String currentTitle;

    // အတွေ့အကြုံ စုစုပေါင်း နှစ် (Filtering အတွက် အရေးကြီး)
    private Integer totalExperienceYears = 0;

    // လျှောက်ထားသူရဲ့ ကိုယ်ရေးအကျဉ်းချုပ် (Summary)
    @Lob // Large Object for long text
    private String professionalSummary;

    // ကျွမ်းကျင်မှုများ (Skills) ကို Database မှာ String Array သို့မဟုတ် Comma-separated String အဖြစ် သိမ်းဆည်းနိုင်သည်
    @Lob
    private String skills;

    private String linkedInUrl;
    private String portfolioUrl;

    // One-to-Many: အလုပ်အတွေ့အကြုံများ (Cascade.ALL ထားခြင်းဖြင့် Profile ဖျက်ပါက ၎င်း၏ Experiences များလည်း ပျက်မည်)
    @OneToMany(mappedBy = "applicantProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobExperience> experiences = new ArrayList<>();

    // One-to-Many: ပညာအရည်အချင်းများ
    @OneToMany(mappedBy = "applicantProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Education> education = new ArrayList<>();


    // --- 1. Constructors ---
    public ApplicantProfile() {
    }

    // --- 2. Getters and Setters ---
    // (Omitted for brevity, but all original getters/setters are assumed to be here)

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getCurrentTitle() { return currentTitle; }
    public void setCurrentTitle(String currentTitle) { this.currentTitle = currentTitle; }
    public Integer getTotalExperienceYears() { return totalExperienceYears; }
    public void setTotalExperienceYears(Integer totalExperienceYears) { this.totalExperienceYears = totalExperienceYears; }
    public String getProfessionalSummary() { return professionalSummary; }
    public void setProfessionalSummary(String professionalSummary) { this.professionalSummary = professionalSummary; }
    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }
    public String getLinkedInUrl() { return linkedInUrl; }
    public void setLinkedInUrl(String linkedInUrl) { this.linkedInUrl = linkedInUrl; }
    public String getPortfolioUrl() { return portfolioUrl; }
    public void setPortfolioUrl(String portfolioUrl) { this.portfolioUrl = portfolioUrl; }
    public List<JobExperience> getExperiences() { return experiences; }
    public void setExperiences(List<JobExperience> experiences) { this.experiences = experiences; }
    public List<Education> getEducation() { return education; }
    public void setEducation(List<Education> education) { this.education = education; }

    // Helper method for Experiences
    public void addExperience(JobExperience experience) {
        this.experiences.add(experience);
        experience.setApplicantProfile(this);
    }

    public void removeExperience(JobExperience experience) {
        this.experiences.remove(experience);
        experience.setApplicantProfile(null);
    }

    // Helper method for Education
    public void addEducation(Education edu) {
        this.education.add(edu);
        edu.setApplicantProfile(this);
    }

    public void removeEducation(Education edu) {
        this.education.remove(edu);
        edu.setApplicantProfile(null);
    }

    /**
     * Business Rule: လျှောက်လွှာတင်ဖို့ Profile ပြည့်စုံမှု ရှိ/မရှိ စစ်ဆေးခြင်း။
     * စည်းကမ်းချက်: အမည်၊ လက်ရှိရာထူး ဖြည့်ထားရမည် AND (အလုပ်အတွေ့အကြုံ သို့မဟုတ် ပညာရေး တစ်ခုခု) ရှိရမည်။
     */
    public boolean isProfileComplete() {
        // 1. Basic Info (FullName, CurrentTitle)
        boolean hasBasicInfo = this.fullName != null && !this.fullName.trim().isEmpty() &&
                               this.currentTitle != null && !this.currentTitle.trim().isEmpty();
        
        // 2. Core Data (Experiences OR Education)
        // 💡 Note: FetchType.LAZY ဖြစ်နေရင် Controller က Service ထဲမှာ Fetch Join မလုပ်ရင် ဒီ List တွေက null ဖြစ်နေနိုင်ပါတယ်။
        // သို့သော် DataSeeder ထဲမှာ Data ထည့်ထားရင် အနည်းဆုံး တစ်ခုခု ရှိနေရပါမယ်။
        boolean hasCoreData = (this.experiences != null && !this.experiences.isEmpty()) ||
                              (this.education != null && !this.education.isEmpty());

        return hasBasicInfo && hasCoreData;
    }

}

