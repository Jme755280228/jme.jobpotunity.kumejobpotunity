package jme.jobpotunity.kumejobpotunity.service;

import jme.jobpotunity.kumejobpotunity.entity.ApplicantProfile;
import jme.jobpotunity.kumejobpotunity.entity.User;
import jme.jobpotunity.kumejobpotunity.repository.ApplicantProfileRepository;
import jme.jobpotunity.kumejobpotunity.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ApplicantProfileService {

    private final ApplicantProfileRepository applicantProfileRepository;
    private final UserRepository userRepository; 
    // JobExperienceRepository, EducationRepository တွေကို Profile save မှာ Cascade သုံးထားလို့ တိုက်ရိုက် Inject လုပ်စရာမလို။

    @Autowired
    public ApplicantProfileService(ApplicantProfileRepository applicantProfileRepository, UserRepository userRepository) {
        this.applicantProfileRepository = applicantProfileRepository;
        this.userRepository = userRepository;
    }

    /**
     * User Entity ကို ပေးပို့ပြီး ၎င်းနှင့်ချိတ်ဆက်ထားသည့် ApplicantProfile ကို ရယူခြင်း
     */
    public Optional<ApplicantProfile> getProfileByUser(User user) {
        // User Entity မှာ OneToOne relationship ချိတ်ထားသောကြောင့် profile ကို တိုက်ရိုက်ရယူနိုင်
        return Optional.ofNullable(user.getApplicantProfile());
    }
    
    /**
     * Profile Data ကို Database မှာ သိမ်းဆည်း/အသစ်တင်ခြင်း (CV Form ဖြည့်ပြီးတဲ့အခါ ခေါ်သုံးရန်)
     */
    @Transactional
    public ApplicantProfile saveOrUpdateProfile(ApplicantProfile profile) {
        // CascadeType.ALL ကြောင့် experiences နှင့် education data များ အလိုအလျောက် သိမ်းဆည်းသွားမည်။
        return applicantProfileRepository.save(profile);
    }
    
    /**
     * Profile ရှိ/မရှိ စစ်ဆေးခြင်း
     */
     public boolean hasProfile(User user) {
         return user.getApplicantProfile() != null;
     }

    // နောက်ပိုင်း Controller ကနေ အလွယ်တကူ ခေါ်သုံးနိုင်မယ့် Logic တွေ ဒီနေရာမှာ ထပ်ထည့်ပါမယ်။
}
