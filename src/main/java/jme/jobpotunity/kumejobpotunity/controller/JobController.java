package jme.jobpotunity.kumejobpotunity.controller;

import jme.jobpotunity.kumejobpotunity.domain.job.JobPosting;
import jme.jobpotunity.kumejobpotunity.domain.user.User;
import jme.jobpotunity.kumejobpotunity.service.JobApplicationService;
import jme.jobpotunity.kumejobpotunity.service.JobPostingService;
import jme.jobpotunity.kumejobpotunity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/jobs")
public class JobController {

    private final JobPostingService jobPostingService;
    private final UserService userService;
    private final JobApplicationService jobApplicationService;

    @Autowired
    public JobController(JobPostingService jobPostingService, UserService userService, JobApplicationService jobApplicationService) {
        this.jobPostingService = jobPostingService;
        this.userService = userService;
        this.jobApplicationService = jobApplicationService;
    }

    /**
     * အတည်ပြုပြီးသား Job Posting စာရင်းကို ပြသခြင်း
     */
    @GetMapping
    public String showApprovedJobList(@RequestParam(required = false) String keyword, Model model, Pageable pageable) {
        Page<JobPosting> jobsPage;
        if (keyword != null && !keyword.isBlank()) {
            jobsPage = jobPostingService.searchApprovedJobs(keyword, pageable);
            model.addAttribute("keyword", keyword);
        } else {
            jobsPage = jobPostingService.findByApprovalStatus(true, pageable);
        }
        model.addAttribute("jobsPage", jobsPage);
        return "job-listings";
    }

    /**
     * Job Posting အသေးစိတ်ကို ပြသခြင်း
     */
    @GetMapping("/{id}")
    public String showJobDetail(@PathVariable Long id, Model model, Principal principal) {
        Optional<JobPosting> jobOptional = jobPostingService.findById(id);

        if (jobOptional.isEmpty() || !jobOptional.get().isApproved()) {
            // Optional: Or redirect to a 404 page
            throw new IllegalArgumentException("Invalid job ID or job is not yet approved.");
        }
        model.addAttribute("job", jobOptional.get());

        // Check if the current user has already applied
        boolean hasApplied = false;
        if (principal != null) {
            userService.findByEmail(principal.getName()).ifPresent(user -> {
                boolean applied = jobApplicationService.hasUserApplied(user, jobOptional.get());
                model.addAttribute("hasApplied", applied);
            });
        }
        return "job-details";
    }
}


