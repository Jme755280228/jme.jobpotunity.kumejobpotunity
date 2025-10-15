package jme.jobpotunity.kumejobpotunity.controller;

import jme.jobpotunity.kumejobpotunity.domain.user.ApplicantProfile;
import jme.jobpotunity.kumejobpotunity.domain.user.User;
import jme.jobpotunity.kumejobpotunity.service.ApplicantProfileService;
import jme.jobpotunity.kumejobpotunity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

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
     * Applicant Profile ကို ကြည့်ရန် သို့မဟုတ် ပြင်ဆင်ရန် Form ကို ပြသခြင်း
     */
    @GetMapping("/edit")
    public String showProfileForm(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalStateException("User not found."));

        ApplicantProfile profile = applicantProfileService.findByUser(user)
                .orElse(new ApplicantProfile(user)); // If no profile, create a new one

        model.addAttribute("profile", profile);
        return "applicant-profile-form";
    }

    /**
     * Applicant Profile ကို သိမ်းဆည်းခြင်း
     */
    @PostMapping("/save")
    public String saveProfile(@ModelAttribute ApplicantProfile profile, Principal principal, RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalStateException("User not found."));

        applicantProfileService.saveProfile(profile, user);
        redirectAttributes.addFlashAttribute("successMessage", "Profile has been updated successfully.");
        return "redirect:/profile/edit";
    }
}


