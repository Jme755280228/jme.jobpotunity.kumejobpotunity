package jme.jobpotunity.kumejobpotunity.controller;

import jme.jobpotunity.kumejobpotunity.entity.JobPosting;
import jme.jobpotunity.kumejobpotunity.service.JobPostingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final JobPostingService jobPostingService;

    @Autowired
    public AdminController(JobPostingService jobPostingService) {
        this.jobPostingService = jobPostingService;
    }

    /**
     * Admin Dashboard တွင် Approve မလုပ်ရသေးသော Job များကို စာရင်းပြုစုပြသခြင်း။
     */
    @GetMapping("/jobs")
    // 🎯 FIX 1.1: Service နှင့် ကိုက်ညီရန် Pageable parameter ကို လက်ခံပါ။
    public String showAdminDashboard(Model model, Principal principal, Pageable pageable) {

        // 🎯 FIX 1.2: Service မှ findPendingJobs(pageable) ကို ခေါ်ပြီး Page object ဖြင့် လက်ခံပါ။
        Page<JobPosting> jobsPage = jobPostingService.findPendingJobs(pageable);

        // 🎯 FIX 1.3: Model သို့ Page object ကို ထည့်ပါ။ (Template တွင် jobsPage.content ဖြင့် သုံးရမည်)
        model.addAttribute("jobsPage", jobsPage);
        model.addAttribute("adminName", principal.getName());
        model.addAttribute("viewMode", "Pending Jobs"); 

        return "admin-jobs";
    }

    /**
     * Job Posting တစ်ခုကို Admin မှ အတည်ပြုပေးခြင်း
     */
    @GetMapping("/jobs/approve/{id}")
    public String approveJob(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        // 🎯 FIX 2.1: Service မှ Optional<JobPosting> ကို လက်ခံပါ။
        Optional<JobPosting> approvedJob = jobPostingService.approveJob(id);

        // 🎯 FIX 2.2: .isPresent() ကို သုံးပြီး အောင်မြင်ခြင်း 여부 ကို စစ်ဆေးပါ။
        if (approvedJob.isPresent()) {
            redirectAttributes.addFlashAttribute("successMessage", "Job Posting ID " + id + " ကို အောင်မြင်စွာ အတည်ပြုပြီးပါပြီ။");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Job Posting ID " + id + " ကို ရှာမတွေ့ပါသဖြင့် အတည်ပြု၍ မရပါ။");
        }

        return "redirect:/admin/jobs";
    }

    /**
     * အတည်ပြုပြီးသား Job များ သို့မဟုတ် အားလုံးကို ကြည့်ရှုရန်
     */
    @GetMapping("/jobs/all")
    public String showAllJobs(Model model, Principal principal, Pageable pageable) {
        // 🎯 FIX 3.1: Consistent ဖြစ်စေရန် findAll() ကိုလည်း Pagination ဖြင့် ခေါ်ဆိုပါ။
        Page<JobPosting> jobsPage = jobPostingService.findAll(pageable);
        model.addAttribute("jobsPage", jobsPage);
        model.addAttribute("adminName", principal.getName());
        model.addAttribute("viewMode", "All Jobs");
        return "admin-jobs";
    }
}
