package jme.jobpotunity.kumejobpotunity.controller;

import jme.jobpotunity.kumejobpotunity.domain.job.JobPosting;
import jme.jobpotunity.kumejobpotunity.domain.user.User;
import jme.jobpotunity.kumejobpotunity.service.JobApplicationService;
import jme.jobpotunity.kumejobpotunity.service.JobPostingService;
import jme.jobpotunity.kumejobpotunity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/applicant")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;
    private final UserService userService;
    private final JobPostingService jobPostingService;

    @Autowired
    public JobApplicationController(JobApplicationService jobApplicationService, UserService userService, JobPostingService jobPostingService) {
        this.jobApplicationService = jobApplicationService;
        this.userService = userService;
        this.jobPostingService = jobPostingService;
    }

    /**
     * Job ကို လျှောက်ထားခြင်း
     */
    @PostMapping("/apply/{jobId}")
    public String applyForJob(@PathVariable Long jobId, Principal principal, RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login"; // Should be handled by SecurityConfig, but as a fallback
        }

        try {
            User user = userService.findByEmail(principal.getName())
                    .orElseThrow(() -> new IllegalStateException("Logged in user not found."));

            jobApplicationService.createApplication(jobId, user.getId());
            redirectAttributes.addFlashAttribute("successMessage", "လျှောက်လွှာကို အောင်မြင်စွာ တင်သွင်းပြီးပါပြီ။");

        } catch (IllegalStateException | IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/jobs/" + jobId;
    }
}


