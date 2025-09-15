package jme.jobpotunity.kumejobpotunity.controller;

import jme.jobpotunity.kumejobpotunity.entity.JobPosting;
import jme.jobpotunity.kumejobpotunity.service.JobPostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class JobController {

    @Autowired
    private JobPostingService jobPostingService;

    // '/' URL ကို လာတဲ့ request ကို လက်ခံပြီး home page ကို ပြသပေးမည်
    @GetMapping("/")
    public String viewHomePage(Model model) {
        
        // database ထဲက job posting အားလုံးကို Service မှတဆင့် ရယူမည်
        List<JobPosting> jobPostings = jobPostingService.findAllJobPostings();
        
        // ရရှိလာတဲ့ data တွေကို Thymeleaf templates အတွက် Model ထဲမှာ ထည့်ပေးမည်
        model.addAttribute("jobPostings", jobPostings);
        
        return "index"; // 'index.html' ကို return လုပ်မည်
    }
}
