package jme.jobpotunity.kumejobpotunity.controller;

import jme.jobpotunity.kumejobpotunity.domain.job.JobPosting;
import jme.jobpotunity.kumejobpotunity.service.JobPostingService;
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

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final JobPostingService jobPostingService;

    @Autowired
    public AdminController(JobPostingService jobPostingService) {
        this.jobPostingService = jobPostingService;
    }

    /**
     * အတည်ပြုရန်လိုအပ်သော Job များစာရင်းကို ပြသရန်
     */
    @GetMapping("/jobs/pending")
    public String listPendingJobs(Model model, Pageable pageable) {
        Page<JobPosting> pendingJobs = jobPostingService.findByApprovalStatus(false, pageable);
        model.addAttribute("jobsPage", pendingJobs);
        model.addAttribute("pageTitle", "Pending Job Approvals");
        return "admin-jobs"; // A view to list jobs for admin
    }

    /**
     * Job Posting ကို အတည်ပြုခြင်း
     */
    @PostMapping("/jobs/{id}/approve")
    public String approveJob(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        jobPostingService.approveJob(id, true);
        redirectAttributes.addFlashAttribute("successMessage", "Job has been approved successfully.");
        return "redirect:/admin/jobs/pending";
    }

    /**
     * Job Posting ကို ပယ်ချခြင်း
     */
    @PostMapping("/jobs/{id}/reject")
    public String rejectJob(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        // Here, rejecting might mean deleting it or setting a 'REJECTED' status
        jobPostingService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Job has been rejected and removed.");
        return "redirect:/admin/jobs/pending";
    }
}


