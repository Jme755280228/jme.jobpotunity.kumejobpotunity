package jme.jobpotunity.kumejobpotunity.controller;

import jme.jobpotunity.kumejobpotunity.entity.ApplicantProfile;
import jme.jobpotunity.kumejobpotunity.entity.User;
import jme.jobpotunity.kumejobpotunity.service.ApplicantProfileService;
import jme.jobpotunity.kumejobpotunity.service.UserService; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult; // Validation အတွက် လိုအပ်
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//import javax.validation.Valid; // Validation အတွက် လိုအပ်
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.Optional;

// /profile/** လမ်းကြောင်းအားလုံးကို ကိုင်တွယ်မည်
@Controller
@RequestMapping("/profile") 
public class ApplicantProfileController {

    private final UserService userService;
    private final ApplicantProfileService applicantProfileService;

    @Autowired
    public ApplicantProfileController(UserService userService, ApplicantProfileService applicantProfileService) {
        this.userService = userService;
        this.applicantProfileService = applicantProfileService;
    }
    
    /**
     * Profile ဖြည့်သွင်းဖို့ Form ကို ပြသခြင်း (/profile/edit)
     */
    @GetMapping("/edit")
    public String showProfileForm(Model model, Principal principal) {
        // 1. Current Login User ကို ရယူခြင်း
        User user = userService.findByUsername(principal.getName())
                        .orElseThrow(() -> new IllegalStateException("Login user not found."));

        // 2. Profile ရှိ/မရှိ စစ်ဆေး
        Optional<ApplicantProfile> existingProfile = applicantProfileService.getProfileByUser(user);
        
        ApplicantProfile profileToDisplay;
        if (existingProfile.isPresent()) {
            profileToDisplay = existingProfile.get();
        } else {
            // Profile အသစ်အတွက် Basic Data ဖြည့်
            ApplicantProfile newProfile = new ApplicantProfile();
            newProfile.setFullName(user.getUsername()); 
            newProfile.setEmail(user.getUsername()); 
            newProfile.setUser(user);
            profileToDisplay = newProfile;
        }

        model.addAttribute("profile", profileToDisplay);
        return "applicant-profile-form"; // Template name
    }

    /**
     * Profile Form ကနေ Data ယူပြီး Save/Update လုပ်ခြင်း (/profile/save)
     * Data-Centric ATS ၏ အဓိက Input Point ဖြစ်သည်။
     */
    @PostMapping("/save")
    public String saveProfile(@ModelAttribute("profile") @Valid ApplicantProfile profile, // << Validation စစ်ဆေးရန်
                              BindingResult result, // << Error များ လက်ခံရန်
                              Principal principal) {
        
        // 1. Validation Error ရှိမရှိ စစ်ဆေးခြင်း
        if (result.hasErrors()) {
            // Validation Failed: Error Message ဖြင့် Form ကို ပြန်ပြမည်
            // Nested List တွေမှာ Error ရှိရင် ဒီနည်းနဲ့ ပြန်ပြရပါတယ်။
            return "applicant-profile-form"; 
        }

        // 2. Security & User ကို ရယူခြင်း
        User user = userService.findByUsername(principal.getName())
                        .orElseThrow(() -> new IllegalStateException("Login user not found."));

        // 3. User Entity နဲ့ ချိတ်ဆက်ပြီး Relationship Consistency ကို ထိန်းသိမ်းခြင်း
        profile.setUser(user);
        user.setApplicantProfile(profile); 
        
        // 4. Data သိမ်းဆည်းခြင်း
        applicantProfileService.saveOrUpdateProfile(profile);
        
        // Success Message ထည့်သွင်းရန် လိုအပ်နိုင်သည်
        
        return "redirect:/"; // Homepage သို့မဟုတ် Job Listing သို့ ပြန်ပို့
    }
}
