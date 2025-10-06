package jme.jobpotunity.kumejobpotunity.controller;

import jme.jobpotunity.kumejobpotunity.entity.ApplicantProfile;
import jme.jobpotunity.kumejobpotunity.entity.JobPosting;
import jme.jobpotunity.kumejobpotunity.entity.User;
import jme.jobpotunity.kumejobpotunity.service.JobPostingService;
import jme.jobpotunity.kumejobpotunity.service.UserService;
import jme.jobpotunity.kumejobpotunity.service.JobApplicationService;
import jme.jobpotunity.kumejobpotunity.service.ApplicationFieldService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Collections;
import java.util.Optional;

@Controller
@RequestMapping("/jobs")
public class JobController {

    private final JobPostingService jobPostingService;
    private final UserService userService;
    private final JobApplicationService jobApplicationService;
    private final ApplicationFieldService applicationFieldService;

    @Autowired
    public JobController(JobPostingService jobPostingService,
                         UserService userService,
                         JobApplicationService jobApplicationService,
                         ApplicationFieldService applicationFieldService) {
        this.jobPostingService = jobPostingService;
        this.userService = userService;
        this.jobApplicationService = jobApplicationService;
        this.applicationFieldService = applicationFieldService;
    }

    /**
     * Job Posting စာရင်းကို ပြသခြင်း
     */
    @GetMapping
    public String showJobList(Model model, Pageable pageable) {
        Page<JobPosting> jobsPage = jobPostingService.findApprovedJobs(pageable);
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
            throw new IllegalArgumentException("Invalid job ID or job is not yet visible.");
        }

        JobPosting job = jobOptional.get();

        boolean hasApplied = false;
        if (principal != null) {
            User user = userService.findByUsername(principal.getName()).orElse(null);
            if (user != null) {
                hasApplied = jobApplicationService.findByJobAndUser(job, user).isPresent();
            }
        }

        boolean requiresCustomFields = job.getRequiredFields() != null && !job.getRequiredFields().isEmpty();

        model.addAttribute("job", job);
        model.addAttribute("hasApplied", hasApplied);
        model.addAttribute("requiresCustomFields", requiresCustomFields);
        return "job-details";
    }

    /**
     * Job ကို လျှောက်ထားခြင်း (Simple Apply - No Custom Fields)
     */
    @PostMapping("/{id}/apply-simple")
    public String submitSimpleApplication(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        if (principal == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "လျှောက်ထားရန်အတွက် အကောင့်ဝင်ရန် လိုအပ်ပါသည်။");
            return "redirect:/login";
        }

        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalStateException("User not found."));

        JobPosting job = jobPostingService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid job ID."));

        if (!job.isApproved()) {
            redirectAttributes.addFlashAttribute("errorMessage", "ဤအလုပ်လျှောက်လွှာကို လက်ရှိတွင် ပိတ်ထားပါသည်။");
            return "redirect:/jobs/" + id;
        }

        ApplicantProfile profile = user.getProfile();
        if (profile == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "လျှောက်ထားခြင်းမပြုမီ သင်၏ Profile အချက်အလက်များကို ဖြည့်သွင်းပေးပါ။");
            return "redirect:/profile/edit";
        }

        if (jobApplicationService.findByJobAndUser(job, user).isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "ဤအလုပ်ကို သင်လျှောက်ထားပြီး ဖြစ်ပါသည်။");
            return "redirect:/jobs/" + id;
        }

        jobApplicationService.createApplicationWithResponses(job, user, profile, Collections.emptyList(), null);

        redirectAttributes.addFlashAttribute("successMessage", "လျှောက်လွှာကို အောင်မြင်စွာ တင်သွင်းပြီးပါပြီ။");
        return "redirect:/jobs/" + id;
    }
}