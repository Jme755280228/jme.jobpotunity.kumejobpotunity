package jme.jobpotunity.kumejobpotunity.controller;

import jme.jobpotunity.kumejobpotunity.entity.JobPosting;
import jme.jobpotunity.kumejobpotunity.entity.JobApplication;
import jme.jobpotunity.kumejobpotunity.entity.User;
import jme.jobpotunity.kumejobpotunity.entity.ApplicantProfile;
import jme.jobpotunity.kumejobpotunity.service.JobPostingService;
import jme.jobpotunity.kumejobpotunity.service.UserService;
import jme.jobpotunity.kumejobpotunity.service.JobApplicationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.access.AccessDeniedException; // Security Error အတွက်

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/employer") 
// Spring Security Config တွင် /employer/** ကို hasRole("EMPLOYER") ဖြင့် ကာကွယ်ထားပြီးဖြစ်သည်။
public class EmployerController {

    private final JobPostingService jobPostingService;
    private final UserService userService;
    private final JobApplicationService jobApplicationService;

    @Autowired
    public EmployerController(JobPostingService jobPostingService, 
                              UserService userService, 
                              JobApplicationService jobApplicationService) {
        this.jobPostingService = jobPostingService;
        this.userService = userService;
        this.jobApplicationService = jobApplicationService;
    }

    /**
     * Employer Dashboard: မိမိတင်ထားသော Job များစာရင်းကို ပြသခြင်း
     * URL: /employer/jobs
     */
    @GetMapping("/jobs")
    public String showEmployerJobs(Model model, Principal principal) {
        // 1. Current Employer ကို ရယူခြင်း
        User employer = userService.findByUsername(principal.getName())
                        .orElseThrow(() -> new IllegalStateException("Employer user not found."));

        // 2. ၎င်း Employer တင်ထားသော Job များကို ရယူခြင်း
        // Note: JobPostingService တွင် findByEmployerUser(User employer) method ရှိရန် လိုအပ်ပါသည်။
        List<JobPosting> jobs = jobPostingService.findByEmployerUser(employer);

        model.addAttribute("jobs", jobs);
        model.addAttribute("employerName", employer.getUsername());
        return "employer-jobs-list"; // Template name
    }

    /**
     * Job တစ်ခုအတွက် လျှောက်ထားသူများစာရင်းကို ပြသခြင်း
     * URL: /employer/jobs/{jobId}/applicants
     */
    @GetMapping("/jobs/{jobId}/applicants")
    public String showApplicants(@PathVariable Long jobId, Model model, Principal principal) {
        // 1. Job Posting ကို ရယူခြင်း
        JobPosting job = jobPostingService.findById(jobId)
                            .orElseThrow(() -> new IllegalArgumentException("Invalid job ID."));

        // 2. Ownership စစ်ဆေးခြင်း: Employer က ဒီ Job ကို တင်ထားသူ ဖြစ်ရမည် (သို့မဟုတ် Admin)
        User employer = userService.findByUsername(principal.getName())
                        .orElseThrow(() -> new IllegalStateException("Employer user not found."));
                        
        // Security Two-Step Protection (Object Level Check)
        if (!job.getEmployerUser().getId().equals(employer.getId()) && !employer.getRole().equals("ADMIN")) {
             throw new AccessDeniedException("You are not authorized to view applicants for this job posting.");
        }

        // 3. လျှောက်ထားသူများစာရင်းကို ရယူခြင်း
        List<JobApplication> applications = jobApplicationService.findApplicationsByJob(job);

        model.addAttribute("job", job);
        model.addAttribute("applications", applications);
        return "applicant-list"; // Template name
    }
    
    /**
     * လျှောက်ထားသူတစ်ဦး၏ Structured Profile Data ကို ပြသခြင်း
     * URL: /employer/applications/{applicationId}/profile
     */
    @GetMapping("/applications/{applicationId}/profile")
    public String showApplicantProfile(@PathVariable Long applicationId, Model model, Principal principal) {
        
        // 1. Job Application ကို ID ဖြင့် ရယူခြင်း
        JobApplication application = jobApplicationService.findById(applicationId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid application ID."));

        // 2. Ownership စစ်ဆေးခြင်း: ဒီလျှောက်လွှာတင်ထားတဲ့ Job ရဲ့ Owner ဟုတ်၊ မဟုတ်
        User employer = userService.findByUsername(principal.getName())
                        .orElseThrow(() -> new IllegalStateException("Employer user not found."));
                        
        JobPosting job = application.getJob();

        // 3. Security Check: Employer က ဒီ Job ကို တင်ထားသူ ဖြစ်ရမည် (သို့မဟုတ် Admin)
        if (!job.getEmployerUser().getId().equals(employer.getId()) && !employer.getRole().equals("ADMIN")) {
             throw new AccessDeniedException("You are not authorized to view this application's profile.");
        }

        // 4. Structured Profile Data ကို ရယူခြင်း (Data-Centric Core)
        ApplicantProfile profile = application.getApplicantProfile();
        
        if (profile == null) {
             model.addAttribute("errorMessage", "Applicant has not created a complete structured profile yet.");
             return "error"; // သို့မဟုတ် Job Application Page ကို ပြန်ပို့
        }

        model.addAttribute("application", application);
        model.addAttribute("profile", profile);
        return "applicant-profile-view"; // Template name
    }
}
