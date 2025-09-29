// src/main/java/.../controller/EmployerController.java

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
import org.springframework.security.access.AccessDeniedException;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/employer")
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
     */
    @GetMapping("/jobs")
    public String showEmployerJobs(Model model, Principal principal) {
        User employer = userService.findByUsername(principal.getName())
                        .orElseThrow(() -> new IllegalStateException("Employer user not found."));
        List<JobPosting> jobs = jobPostingService.findByEmployerUser(employer);
        model.addAttribute("jobs", jobs);
        model.addAttribute("employerName", employer.getUsername());
        return "employer-jobs-list";
    }

    /**
     * Job တစ်ခုအတွက် လျှောက်ထားသူများစာရင်းကို ပြသခြင်း
     */
    @GetMapping("/jobs/{jobId}/applicants")
    public String showApplicants(@PathVariable Long jobId, Model model, Principal principal) {
        JobPosting job = jobPostingService.findById(jobId)
                            .orElseThrow(() -> new IllegalArgumentException("Invalid job ID."));

        User employer = userService.findByUsername(principal.getName())
                        .orElseThrow(() -> new IllegalStateException("Employer user not found."));

        if (!job.getEmployerUser().getId().equals(employer.getId()) && !employer.getRole().equals("ADMIN")) {
             throw new AccessDeniedException("You are not authorized to view applicants for this job posting.");
        }

        List<JobApplication> applications = jobApplicationService.findApplicationsByJob(job);

        model.addAttribute("job", job);
        model.addAttribute("applications", applications);
        return "applicant-list";
    }

    /**
     * လျှောက်ထားသူတစ်ဦး၏ Structured Profile Data ကို ပြသခြင်း
     */
    @GetMapping("/applications/{applicationId}/profile")
    public String showApplicantProfile(@PathVariable Long applicationId, Model model, Principal principal) {

        // 💡 FIX: findById() အစား findByIdWithProfile() ကို အသုံးပြု၍ Lazy Loading Error ဖြေရှင်းခြင်း
        JobApplication application = jobApplicationService.findByIdWithProfile(applicationId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid application ID."));

        User employer = userService.findByUsername(principal.getName())
                        .orElseThrow(() -> new IllegalStateException("Employer user not found."));

        JobPosting job = application.getJob();

        if (!job.getEmployerUser().getId().equals(employer.getId()) && !employer.getRole().equals("ADMIN")) {
             throw new AccessDeniedException("You are not authorized to view this application's profile.");
        }

        // Profile Data ကို Eager Load လုပ်ပြီးသား ဖြစ်၍ Error မရှိတော့ပါ
        ApplicantProfile profile = application.getApplicantProfile();

        if (profile == null) {
             model.addAttribute("errorMessage", "Applicant has not created a complete structured profile yet.");
             return "error";
        }

        model.addAttribute("application", application);
        model.addAttribute("profile", profile);
        return "applicant-profile-view";
    }
}

