package jme.jobpotunity.kumejobpotunity.controller;

import jme.jobpotunity.kumejobpotunity.entity.Company;
import jme.jobpotunity.kumejobpotunity.entity.JobPosting;
import jme.jobpotunity.kumejobpotunity.entity.User;
import jme.jobpotunity.kumejobpotunity.service.CompanyService;
import jme.jobpotunity.kumejobpotunity.service.JobApplicationService;
import jme.jobpotunity.kumejobpotunity.service.JobPostingService;
import jme.jobpotunity.kumejobpotunity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
public class JobController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private JobPostingService jobPostingService;

    @Autowired
    private JobApplicationService jobApplicationService;

    @Autowired
    private UserService userService;

    // ပင်မစာမျက်နှာနှင့် search functionality
    @GetMapping("/")
    public String viewHomePage(@RequestParam(value = "query", required = false) String query, Model model) {
        List<JobPosting> jobPostings;
        if (query != null && !query.isEmpty()) {
            jobPostings = jobPostingService.searchJobPostings(query);
        } else {
            jobPostings = jobPostingService.findAllJobPostings();
        }
        model.addAttribute("jobPostings", jobPostings);
        return "index";
    }

    // အလုပ်အသစ်တင်ရန် form ကို ပြသပေးမည့် method
    @GetMapping("/newJob")
    public String showNewJobForm(Model model) {
        JobPosting jobPosting = new JobPosting();
        model.addAttribute("jobPosting", jobPosting);
        List<Company> companies = companyService.findAllCompanies();
        model.addAttribute("companies", companies);
        return "new-job";
    }

    // Form မှ data များကို လက်ခံပြီး database ထဲသို့ သိမ်းဆည်းပေးမည့် method
    @PostMapping("/saveJob")
    public String saveJob(@ModelAttribute("jobPosting") JobPosting jobPosting) {
        jobPostingService.saveJobPosting(jobPosting);
        return "redirect:/";
    }

    // Job တစ်ခုချင်းစီ၏ အသေးစိတ်ကို ပြသပေးမည့် method
    @GetMapping("/job/{id}")
    public String showJobDetails(@PathVariable("id") Long id, Model model) {
        JobPosting job = jobPostingService.findById(id);
        model.addAttribute("job", job);
        return "job-details";
    }

    // Job ကို ပြင်ဆင်ရန် form ကို ပြသပေးမည့် method
    @GetMapping("/editJob/{id}")
    public String showEditJobForm(@PathVariable("id") Long id, Model model) {
        JobPosting jobPosting = jobPostingService.findById(id);
        model.addAttribute("jobPosting", jobPosting);
        List<Company> companies = companyService.findAllCompanies();
        model.addAttribute("companies", companies);
        return "edit-job";
    }

    // Job တစ်ခုကို ဖျက်ပစ်မည့် method
    @GetMapping("/deleteJob/{id}")
    public String deleteJob(@PathVariable("id") Long id) {
        jobPostingService.deleteJobPosting(id);
        return "redirect:/";
    }

    // ပြင်ဆင်လိုက်သော data များကို database ထဲသို့ သိမ်းဆည်းပေးမည့် method
    @PostMapping("/updateJob/{id}")
    public String updateJob(@PathVariable("id") Long id, @ModelAttribute("jobPosting") JobPosting jobPosting) {
        jobPosting.setId(id);
        jobPostingService.saveJobPosting(jobPosting);
        return "redirect:/job/" + id;
    }

    // Job လျှောက်ထားမှုကို လက်ခံမည့် method
    @PostMapping("/job/apply/{id}")
    public String applyForJob(@PathVariable("id") Long jobId, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        JobPosting job = jobPostingService.findById(jobId);
        jobApplicationService.applyForJob(job, user);

        return "redirect:/job/" + jobId + "?applied";
    }
}
