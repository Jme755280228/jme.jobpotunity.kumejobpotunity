// src/main/java/.../controller/JobController.java (Final Code)

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
public class JobController {

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
     * 💡 FIX 1: Home Page ကို ပြသခြင်း (index.html ကို သုံးပြီး Featured Job များကို ပြသသည်)
     * URL: /
     */
    @GetMapping("/")
    public String viewHomePage(Model model) {
        // JobPostingService တွင် findTop3Active() method ကို ထည့်ရန် လိုအပ်သည် (သို့မဟုတ် Repository မှ တိုက်ရိုက်ခေါ်ပါ)
        
        // ယာယီအားဖြင့် JobPostingService ကို ပြင်ဆင်ခြင်းမပြုရသေးပါက၊ Repository မှ တိုက်ရိုက်ခေါ်ယူရန် ဥပမာ:
        // List<JobPosting> featuredJobs = jobPostingRepository.findTop3ByIsActiveTrueOrderByPostedDateDesc();
        
        // 💡 JobPostingService တွင် findAllActive() ကိုသာ အသုံးပြုထားသည်ဟု ယူဆပြီး ယာယီရေးသားခြင်း (Service ကို ပြင်ပြီးရင် အစားထိုးပါ)
        List<JobPosting> allActiveJobs = jobPostingService.findAllActive();
        
        // Home Page ၏ index.html template မှ 'jobPostings' နာမည်ဖြင့် Data ကို ခေါ်ယူနေသည်
        model.addAttribute("jobPostings", allActiveJobs.subList(0, Math.min(allActiveJobs.size(), 3))); 
        
        // Job Count Test အတွက်
        model.addAttribute("jobCount", allActiveJobs.size());

        return "index"; // 💡 index.html ကို ပြန်ပို့သည်
    }

    /**
     * Public Job Listing Page ကို ပြသခြင်း (အလုပ်အားလုံးကို ပြသရန်)
     * URL: /jobs
     */
    @GetMapping("/jobs")
    public String showJobListings(Model model) {
        // Active ဖြစ်နေသော Job များကိုသာ ပြသ
        List<JobPosting> jobs = jobPostingService.findAllActive();
        model.addAttribute("jobs", jobs);
        return "job-listings"; // job-listings.html template ကို သုံးသည်
    }

    /**
     * Job Detail Page ကို ပြသခြင်း
     */
    @GetMapping("/jobs/{jobId}")
    public String showJobDetails(@PathVariable Long jobId, Model model) {
        JobPosting job = jobPostingService.findById(jobId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid job ID: " + jobId));

        model.addAttribute("job", job);
        return "job-details"; // job-details.html ကို ပြန်ပို့သည် (Note: job-detail မဟုတ်)
    }

    // ... (ကျန်သည့် applyForJob method သည် မူရင်းအတိုင်း ဆက်ရှိနေပါမည်)
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
            .orElse(null);

        // 2. Business Rule: Profile အပြည့်အစုံ ရှိမရှိ စစ်ဆေးခြင်း
        if (profile == null || !profile.isProfileComplete()) {
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

