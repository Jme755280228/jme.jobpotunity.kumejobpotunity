package jme.jobpotunity.kumejobpotunity.controller;

import jme.jobpotunity.kumejobpotunity.domain.job.JobPosting;
import jme.jobpotunity.kumejobpotunity.domain.user.User;
import jme.jobpotunity.kumejobpotunity.service.JobPostingService;
import jme.jobpotunity.kumejobpotunity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/employer")
public class EmployerController {

    private final JobPostingService jobPostingService;
    private final UserService userService;

    @Autowired
    public EmployerController(JobPostingService jobPostingService, UserService userService) {
        this.jobPostingService = jobPostingService;
        this.userService = userService;
    }

    /**
     * Employer တင်ထားသော Job များစာရင်းကို ပြသရန်
     */
    @GetMapping("/jobs")
    public String listEmployerJobs(Model model, Principal principal) {
        User employer = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalStateException("Employer not found."));
        List<JobPosting> jobs = jobPostingService.findByEmployer(employer);
        model.addAttribute("jobs", jobs);
        return "employer-jobs-list";
    }

    /**
     * Job အသစ်တင်ရန် Form ကို ပြသရန်
     */
    @GetMapping("/jobs/new")
    public String showNewJobForm(Model model) {
        model.addAttribute("job", new JobPosting());
        return "job-form";
    }

    /**
     * Job အသစ်ကို သိမ်းဆည်းရန်
     */
    @PostMapping("/jobs/save")
    public String saveJob(@ModelAttribute JobPosting jobPosting, Principal principal, RedirectAttributes redirectAttributes) {
        User employer = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalStateException("Employer not found."));
        jobPosting.setEmployer(employer);
        jobPosting.setApproved(false); // New jobs need admin approval
        jobPostingService.save(jobPosting);
        redirectAttributes.addFlashAttribute("successMessage", "Job posted successfully. It will be visible after admin approval.");
        return "redirect:/employer/jobs";
    }
}


