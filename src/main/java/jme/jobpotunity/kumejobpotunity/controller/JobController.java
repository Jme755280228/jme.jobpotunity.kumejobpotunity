package jme.jobpotunity.kumejobpotunity.controller;

import jme.jobpotunity.kumejobpotunity.entity.ApplicantProfile;
import jme.jobpotunity.kumejobpotunity.entity.JobPosting;
import jme.jobpotunity.kumejobpotunity.entity.User;
import jme.jobpotunity.kumejobpotunity.service.JobApplicationService;
import jme.jobpotunity.kumejobpotunity.service.ApplicantProfileService;
import jme.jobpotunity.kumejobpotunity.service.UserService;
import jme.jobpotunity.kumejobpotunity.service.JobPostingService; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute; // Form တွေအတွက် လိုအပ်
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class JobController {

    private final JobApplicationService jobApplicationService;
    private final ApplicantProfileService applicantProfileService;
    private final UserService userService;
    private final JobPostingService jobPostingService; 

    @Autowired
    public JobController(JobApplicationService jobApplicationService, 
                         ApplicantProfileService applicantProfileService, 
                         UserService userService,
                         JobPostingService jobPostingService) {
        this.jobApplicationService = jobApplicationService;
        this.applicantProfileService = applicantProfileService;
        this.userService = userService;
        this.jobPostingService = jobPostingService;
    }

    // --- 1. Public Job Listing & Detail ---
    
    @GetMapping({"/", "/jobs"})
    public String listJobs(Model model) {
        List<JobPosting> jobs = jobPostingService.findAll();
        model.addAttribute("jobs", jobs);
        return "job-listing";
    }
    
    @GetMapping("/job/{id}")
    public String jobDetails(@PathVariable Long id, Model model) {
        Optional<JobPosting> jobOptional = jobPostingService.findById(id); 
        if (jobOptional.isEmpty()) {
            return "redirect:/jobs"; 
        }
        model.addAttribute("job", jobOptional.get());
        return "job-details";
    }


    // --- 2. JOB APPLY LOGIC (Data-Centric Flow) ---

    @PostMapping("/job/apply/{jobId}")
    public String applyForJob(@PathVariable Long jobId, 
                              Principal principal,
                              RedirectAttributes redirectAttributes) {
        
        User user = userService.findByUsername(principal.getName())
                        .orElseThrow(() -> new IllegalStateException("Login user not found."));

        JobPosting job = jobPostingService.findById(jobId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid job ID."));
        
        Optional<ApplicantProfile> profileOptional = applicantProfileService.getProfileByUser(user);

        if (profileOptional.isEmpty()) {
            // Profile မရှိပါက Form ဖြည့်ရန် /profile/edit သို့ Redirect လုပ်ခြင်း
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Your profile is incomplete. Please fill out your profile details before applying.");
            return "redirect:/profile/edit"; 
        }

        ApplicantProfile applicantProfile = profileOptional.get();
        try {
            jobApplicationService.applyForJob(job, user, applicantProfile);
            redirectAttributes.addFlashAttribute("successMessage", "Successfully applied for the job!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Application failed: " + e.getMessage());
        }

        return "redirect:/job/" + jobId; 
    }
    
    
    // --- 3. EMPLOYER/ADMIN JOB MANAGEMENT FUNCTIONS (မူရင်းကုဒ်များ) ---
    // Spring Security ဖြင့် ADMIN သို့မဟုတ် EMPLOYER Role များဖြင့် ကာကွယ်ထားသည်။

    /**
     * Job အသစ်တင်ရန် Form ကို ပြသခြင်း (ADMIN/EMPLOYER ဝင်ခွင့်ရှိရမည်)
     * URL: /newJob
     */
    @GetMapping("/newJob")
    public String showNewJobForm(Model model) {
        model.addAttribute("jobPosting", new JobPosting());
        // Company List ကိုလည်း Model ထဲ ထည့်ပေးရန် လိုအပ်နိုင်
        return "job-form"; // Job တင်ရန် Form Template
    }

    /**
     * Job အသစ်ကို Save လုပ်ခြင်း (ADMIN/EMPLOYER ဝင်ခွင့်ရှိရမည်)
     * URL: /saveJob
     */
    @PostMapping("/saveJob")
    public String saveJob(@ModelAttribute("jobPosting") JobPosting jobPosting, Principal principal, RedirectAttributes redirectAttributes) {
        // JobPosting Entity မှာ employerUser Field အသစ်ရှိနေပြီ
        User employer = userService.findByUsername(principal.getName())
                        .orElseThrow(() -> new IllegalStateException("Employer user not found."));
        
        jobPosting.setEmployerUser(employer); // Owner ကို သတ်မှတ်ခြင်း
        
        jobPostingService.save(jobPosting);
        redirectAttributes.addFlashAttribute("successMessage", "Job posted successfully!");
        return "redirect:/jobs";
    }

    /**
     * Job ကို ပြင်ဆင်ရန် Form ကို ပြသခြင်း (ADMIN/EMPLOYER ဝင်ခွင့်ရှိရမည်)
     * URL: /editJob/{id}
     */
    @GetMapping("/editJob/{id}")
    public String showEditJobForm(@PathVariable Long id, Model model) {
        JobPosting jobPosting = jobPostingService.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("Invalid job ID:" + id));
        model.addAttribute("jobPosting", jobPosting);
        return "job-form";
    }
    
    /**
     * Job ကို ပြင်ဆင်ပြီး Update လုပ်ခြင်း (ADMIN/EMPLOYER ဝင်ခွင့်ရှိရမည်)
     * URL: /updateJob/{id}
     */
    @PostMapping("/updateJob/{id}")
    public String updateJob(@PathVariable Long id, @ModelAttribute("jobPosting") JobPosting jobPosting, RedirectAttributes redirectAttributes) {
        // ID ကို Set ပေးရန်
        jobPosting.setId(id);
        jobPostingService.save(jobPosting); // Save method ကို update အတွက်လည်း သုံးနိုင်
        redirectAttributes.addFlashAttribute("successMessage", "Job updated successfully!");
        return "redirect:/job/" + id;
    }

    /**
     * Job ကို ဖျက်ခြင်း (ADMIN/EMPLOYER ဝင်ခွင့်ရှိရမည်)
     * URL: /deleteJob/{id}
     */
    @GetMapping("/deleteJob/{id}")
    public String deleteJob(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        jobPostingService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Job deleted successfully!");
        return "redirect:/jobs";
    }
}
