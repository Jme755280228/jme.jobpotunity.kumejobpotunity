package jme.jobpotunity.kumejobpotunity.controller;

import jme.jobpotunity.kumejobpotunity.entity.Company;
import jme.jobpotunity.kumejobpotunity.entity.JobPosting;
import jme.jobpotunity.kumejobpotunity.service.CompanyService;
import jme.jobpotunity.kumejobpotunity.service.JobPostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@Controller
public class JobController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private JobPostingService jobPostingService;

    // ပင်မစာမျက်နှာကို ပြသပေးမည့် method
    // URL: http://localhost:8080/
    @GetMapping("/")
    public String viewHomePage(Model model) {
        // Service မှတဆင့် database ထဲက Job Posting အားလုံးကို ရယူပါ
        List<JobPosting> jobPostings = jobPostingService.findAllJobPostings();
        
        // ရရှိလာသော data များကို HTML template အတွက် Model ထဲတွင် ထည့်သွင်းပါ
        model.addAttribute("jobPostings", jobPostings);
        
        return "index"; // 'index.html' ကို ပြန်ပို့ပေးပါ
    }

    // အလုပ်အသစ်တင်ရန် form ကို ပြသပေးမည့် method
    // URL: http://localhost:8080/newJob
    @GetMapping("/newJob")
    public String showNewJobForm(Model model) {
        // Form အတွက် JobPosting object အလွတ်တစ်ခုကို ဖန်တီးပါ
        JobPosting jobPosting = new JobPosting();
        model.addAttribute("jobPosting", jobPosting);
        
        // Form dropdown list အတွက် ကုမ္ပဏီစာရင်းအားလုံးကို database မှ ရယူပါ
        List<Company> companies = companyService.findAllCompanies();
        model.addAttribute("companies", companies);
        
        return "new-job"; // 'new-job.html' ကို ပြန်ပို့ပေးပါ
    }

   @GetMapping("/job/{id}")
   public String showJobDetails(@PathVariable("id") Long id, Model model) {
    // Service မှတဆင့် id ဖြင့် Job Posting ကို ရှာဖွေပါ
    JobPosting job = jobPostingService.findById(id);
    
    // ရရှိလာသော data ကို model ထဲတွင် ထည့်ပါ
    model.addAttribute("job", job);
    
    return "job-details"; // 'job-details.html' ကို ပြန်ပို့ပါ
}

    // Form မှ data များကို လက်ခံပြီး database ထဲသို့ သိမ်းဆည်းပေးမည့် method
    @PostMapping("/saveJob")
    public String saveJob(@ModelAttribute("jobPosting") JobPosting jobPosting) {
        // Service Layer မှတစ်ဆင့် database ထဲသို့ သိမ်းဆည်းပါ
        jobPostingService.saveJobPosting(jobPosting);
        
        // အလုပ်သိမ်းပြီးနောက် ပင်မစာမျက်နှာသို့ ပြန်ပို့ပေးပါ
        return "redirect:/";
    }
}
