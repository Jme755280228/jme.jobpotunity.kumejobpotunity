package jme.jobpotunity.kumejobpotunity.controller;

import jme.jobpotunity.kumejobpotunity.entity.ApplicantProfile;
import jme.jobpotunity.kumejobpotunity.entity.JobPosting;
import jme.jobpotunity.kumejobpotunity.entity.User;
import jme.jobpotunity.kumejobpotunity.service.ApplicantProfileService;
import jme.jobpotunity.kumejobpotunity.service.JobApplicationService;
import jme.jobpotunity.kumejobpotunity.service.JobPostingService;
import jme.jobpotunity.kumejobpotunity.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class JobController { // Public Job Controller

    private final JobPostingService jobPostingService;
    private final UserService userService;
    private final ApplicantProfileService applicantProfileService;
    private final JobApplicationService jobApplicationService;

    @Autowired
    public JobController(JobPostingService jobPostingService, UserService userService, 
                         ApplicantProfileService applicantProfileService, JobApplicationService jobApplicationService) {
        this.jobPostingService = jobPostingService;
        this.userService = userService;
        this.applicantProfileService = applicantProfileService;
        this.jobApplicationService = jobApplicationService;
    }

    /**
     * Public Job Listing Page ကို ပြသခြင်း (Home Page သို့မဟုတ် /jobs)
     */
    @GetMapping({"/", "/jobs"})
    public String showJobListings(Model model) {
        // Active ဖြစ်နေသော Job များကိုသာ ပြသ
        List<JobPosting> jobs = jobPostingService.findAllActive(); 
        model.addAttribute("jobs", jobs);
        return "job-listings"; // Template name
    }

    /**
     * Job Detail Page ကို ပြသခြင်း
     */
    @GetMapping("/jobs/{jobId}")
    public String showJobDetails(@PathVariable Long jobId, Model model) {
        JobPosting job = jobPostingService.findById(jobId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid job ID: " + jobId));
        
        model.addAttribute("job", job);
        return "job-detail"; // Template name
    }

    /**
     * Job အတွက် လျှောက်လွှာတင်ခြင်း (Data-Centric Flow)
     * URL: /jobs/{jobId}/apply
     */
    @PostMapping("/jobs/{jobId}/apply")
    public String applyForJob(@PathVariable Long jobId, 
                              Principal principal, 
                              RedirectAttributes redirectAttributes) {
        
        // 1. Job, User, Profile Entity များကို ရယူခြင်း
        JobPosting job = jobPostingService.findById(jobId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid job ID."));

        User user = userService.findByUsername(principal.getName())
            .orElseThrow(() -> new IllegalStateException("Applicant user not found."));

        ApplicantProfile profile = applicantProfileService.getProfileByUser(user)
            .orElse(null); // Profile မရှိရင် null ဖြစ်နိုင်သည်
            
        // 2. Business Rule: Profile အပြည့်အစုံ ရှိမရှိ စစ်ဆေးခြင်း
        if (profile == null || !profile.isProfileComplete()) { 
             // Note: ApplicantProfile Entity မှာ isProfileComplete() method (ဥပမာ- ၅၀% ဖြည့်ပြီး) ထည့်သွင်းရန် လိုအပ်သည်
             redirectAttributes.addFlashAttribute("errorMessage", "လျှောက်လွှာမတင်မီ သင်၏ Profile အချက်အလက်များကို အပြည့်အစုံ ဖြည့်စွက်ရန် လိုအပ်ပါသည်။");
             return "redirect:/profile/edit";
        }
        
        try {
            // 3. Job Application Service ကို ခေါ်ပြီး လျှောက်လွှာတင်ခြင်း
            jobApplicationService.applyForJob(job, user, profile);

            redirectAttributes.addFlashAttribute("successMessage", "လျှောက်လွှာတင်ခြင်း အောင်မြင်ပါသည်။");
            return "redirect:/jobs/" + jobId;
            
        } catch (IllegalStateException e) {
            // 4. Job ကို ထပ်မံလျှောက်ထားခြင်း ရှိ/မရှိ စစ်ဆေးခြင်း
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/jobs/" + jobId;
        }
    }
}
