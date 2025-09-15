package jme.jobpotunity.kumejobpotunity.controller;

import jme.jobpotunity.kumejobpotunity.entity.Company;
import jme.jobpotunity.kumejobpotunity.entity.JobPosting;
import jme.jobpotunity.kumejobpotunity.service.CompanyService;
import jme.jobpotunity.kumejobpotunity.service.JobPostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.ArrayList;
import java.util.List;

@Controller
public class JobController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private JobPostingService jobPostingService;

    // အလုပ်ခေါ်စာစာရင်းကို ပြသပေးမည့် ပင်မစာမျက်နှာ
    @GetMapping("/")
    public String viewHomePage(Model model) {
        
        // **Dummy Data ဖန်တီးခြင်း**
        // ဤနေရာတွင် database မှ အမှန်တကယ် မခေါ်ယူသေးဘဲ၊ 
        // HTML templates ကို စမ်းသပ်ရန်အတွက်သာ ဖြစ်သည်။
        
        Company dummyCompany1 = new Company("Tech Solutions Myanmar", "IT", "ရန်ကုန်မြို့");
        Company dummyCompany2 = new Company("ABC Corporation", "Marketing", "မန္တလေးမြို့");
        
        JobPosting dummyJob1 = new JobPosting(
            "Backend Developer", 
            "We are looking for a skilled Frontend Developer...", 
            "ရန်ကုန်မြို့", 
            "၁၅၀၀၀၀၀ ကျပ်", 
            "အချိန်ပြည့်", 
            dummyCompany1
        );
        
        JobPosting dummyJob2 = new JobPosting(
            "Manager", 
            "We are seeking a talented Manager...", 
            "မန္တလေးမြို့", 
            "၂၀၀၀၀၀၀ ကျပ်", 
            "အချိန်ပြည့်", 
            dummyCompany2
        );
        
        JobPosting dummyJob3 = new JobPosting(
            "Teacher", 
            "We are in need of a passionate Teacher...", 
            "နေပြည်တော်", 
            "၁၂၀၀၀၀၀ ကျပ်", 
            "အချိန်ပြည့်", 
            dummyCompany1 // Company တစ်ခုတည်းက အလုပ်တွေ များစွာ တင်နိုင်တာကို ပြသဖို့
        );
        
        List<JobPosting> jobPostings = new ArrayList<>();
        jobPostings.add(dummyJob1);
        jobPostings.add(dummyJob2);
        jobPostings.add(dummyJob3);

        // Model ထဲသို့ data များထည့်သွင်းခြင်း
        model.addAttribute("jobPostings", jobPostings);
        
        return "index";
    }
}

