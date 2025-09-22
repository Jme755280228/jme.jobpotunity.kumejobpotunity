// src/main/java/jme/jobpotunity/kumejobpotunity/controller/JobController.java (Update)

package jme.jobpotunity.kumejobpotunity.controller;

import jme.jobpotunity.kumejobpotunity.entity.JobApplication;
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
import org.springframework.web.multipart.MultipartFile; // Add this
import java.io.IOException; // Add this
import java.nio.file.Files; // Add this
import java.nio.file.Path; // Add this
import java.nio.file.Paths; // Add this
import java.util.List;
import java.util.UUID; // Add this

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

    // We'll create a new directory to store uploaded CVs
    private static String UPLOADED_FOLDER = "uploads/cvs/";

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

    // All jobs for admin dashboard
    @GetMapping("/admin/jobs")
    public String getAllJobs(Model model) {
        List<JobPosting> jobPostings = jobPostingService.findAllJobPostings();
        model.addAttribute("jobPostings", jobPostings);
        return "admin-jobs";
    }

    
// View all applicants for a specific job (for admin)
@GetMapping("/admin/jobs/{jobId}/applicants")
public String getApplicantsByJob(@PathVariable("jobId") Long jobId, Model model) {
    JobPosting job = jobPostingService.findById(jobId);
    if (job == null) {
        return "redirect:/admin/jobs?error=jobNotFound";
    }

    List<JobApplication> applications = jobApplicationService.findApplicationsByJob(job);
    model.addAttribute("job", job);
    model.addAttribute("applications", applications);
    return "job-applicants";
}


    // ပြင်ဆင်လိုက်သော data များကို database ထဲသို့ သိမ်းဆည်းပေးမည့် method
    @PostMapping("/updateJob/{id}")
    public String updateJob(@PathVariable("id") Long id, @ModelAttribute("jobPosting") JobPosting jobPosting) {
        jobPosting.setId(id);
        jobPostingService.saveJobPosting(jobPosting);
        return "redirect:/job/" + id;
    }

    // Job လျှောက်ထားမှုကို လက်ခံမည့် method (Updated)
   @PostMapping("/job/apply/{jobId}")
    public String applyForJob(@PathVariable("jobId") Long jobId,
                              @RequestParam("applicantName") String applicantName,
                              @RequestParam("applicantEmail") String applicantEmail,
                              @RequestParam("applicantPhone") String applicantPhone,
                              @RequestParam(value = "cvFile", required = false) MultipartFile cvFile,
                              Authentication authentication) {
        
        try {
            // Step 1: လက်ရှိ login ဝင်ထားသော user ကို ရယူသည်
            String username = authentication.getName();
            User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            
            // Step 2: လျှောက်ထားသော job ကို database မှ ရယူသည်
            JobPosting job = jobPostingService.findById(jobId);
            
            // CV လိုအပ် မလိုအပ် စစ်ဆေးသည်
            String cvFilePath = null; // CV file path ကို default အဖြစ် null သတ်မှတ်ထားသည်

            if (job.getIsCvRequired() != null && job.getIsCvRequired()) {
                // CV လိုအပ်လျှင်
                if (cvFile == null || cvFile.isEmpty()) {
                    // CV file မပါခဲ့ရင် error ပြန်ပို့မယ်
                    return "redirect:/job/" + jobId + "?error=cvRequired";
                }
                
                // CV file ကို server မှာ သိမ်းဆည်းသည်
                String originalFileName = cvFile.getOriginalFilename();
                String fileExtension = "";
                if (originalFileName != null && originalFileName.lastIndexOf(".") != -1) {
                    fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
                }
                String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
                
                Path path = Paths.get(UPLOADED_FOLDER + uniqueFileName);
                
                // Ensure the directory exists
                Files.createDirectories(path.getParent());
                Files.write(path, cvFile.getBytes());
                
                cvFilePath = path.toString(); // file path ကို သတ်မှတ်သည်
            }
            
            // Step 3: service ကို ခေါ်ပြီး application ကို သိမ်းဆည်းသည်
            // CV file မလိုရင် cvFilePath ဟာ null ဖြစ်နေမှာပါ
            jobApplicationService.applyForJob(job, user, applicantName, applicantEmail, applicantPhone, cvFilePath);

            // လျှောက်လွှာတင်ပြီးပါက job details page ကို ပြန်ပို့သည်
            return "redirect:/job/" + jobId + "?applied";

        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/job/" + jobId + "?error";
        }
    }
}
