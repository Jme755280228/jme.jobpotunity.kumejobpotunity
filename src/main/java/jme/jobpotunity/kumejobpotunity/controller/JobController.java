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
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    private static String UPLOADED_FOLDER = "uploads/cvs/";

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

    @GetMapping("/newJob")
    public String showNewJobForm(Model model) {
        JobPosting jobPosting = new JobPosting();
        model.addAttribute("jobPosting", jobPosting);
        // Companies list is no longer needed for a dropdown
        return "new-job";
    }

    // New POST method to handle job creation with a text input for company name
    @PostMapping("/saveJob")
    public String saveJob(@ModelAttribute("jobPosting") JobPosting jobPosting,
                          @RequestParam("companyName") String companyName) {
        
        // 1. Check if the company already exists by name
        Optional<Company> existingCompany = companyService.findByName(companyName);
        Company company;
        
        if (existingCompany.isPresent()) {
            // 2. If it exists, use the existing company
            company = existingCompany.get();
        } else {
            // 3. If it doesn't exist, create a new company entity and save it
            company = new Company();
            company.setName(companyName);
            company = companyService.save(company);
        }
        
        // 4. Link the job posting to the company
        jobPosting.setCompany(company);
        
        // 5. Save the final job posting
        jobPostingService.saveJobPosting(jobPosting);
        
        return "redirect:/";
    }

    @GetMapping("/job/{id}")
    public String showJobDetails(@PathVariable("id") Long id, Model model) {
        JobPosting job = jobPostingService.findById(id);
        model.addAttribute("job", job);
        return "job-details";
    }

    @GetMapping("/editJob/{id}")
    public String showEditJobForm(@PathVariable("id") Long id, Model model) {
        JobPosting jobPosting = jobPostingService.findById(id);
        model.addAttribute("jobPosting", jobPosting);
        // You may want to add companies here if you want a dropdown for editing
        return "edit-job";
    }

    @GetMapping("/deleteJob/{id}")
    public String deleteJob(@PathVariable("id") Long id) {
        jobPostingService.deleteJobPosting(id);
        return "redirect:/";
    }

    @GetMapping("/admin/jobs")
    public String getAllJobs(Model model) {
        List<JobPosting> jobPostings = jobPostingService.findAllJobPostings();
        model.addAttribute("jobPostings", jobPostings);
        return "admin-jobs";
    }

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

    @PostMapping("/updateJob/{id}")
    public String updateJob(@PathVariable("id") Long id, @ModelAttribute("jobPosting") JobPosting jobPosting) {
        jobPosting.setId(id);
        jobPostingService.saveJobPosting(jobPosting);
        return "redirect:/job/" + id;
    }

    @PostMapping("/job/apply/{jobId}")
    public String applyForJob(@PathVariable("jobId") Long jobId,
                              @RequestParam("applicantName") String applicantName,
                              @RequestParam("applicantEmail") String applicantEmail,
                              @RequestParam("applicantPhone") String applicantPhone,
                              @RequestParam(value = "cvFile", required = false) MultipartFile cvFile,
                              Authentication authentication) {
        
        try {
            String username = authentication.getName();
            User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            
            JobPosting job = jobPostingService.findById(jobId);
            
            String cvFilePath = null;
            if (job.getIsCvRequired() != null && job.getIsCvRequired()) {
                if (cvFile == null || cvFile.isEmpty()) {
                    return "redirect:/job/" + jobId + "?error=cvRequired";
                }
                
                String originalFileName = cvFile.getOriginalFilename();
                String fileExtension = "";
                if (originalFileName != null && originalFileName.lastIndexOf(".") != -1) {
                    fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
                }
                String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
                
                Path path = Paths.get(UPLOADED_FOLDER + uniqueFileName);
                
                Files.createDirectories(path.getParent());
                Files.write(path, cvFile.getBytes());
                
                cvFilePath = path.toString();
            }
            
            jobApplicationService.applyForJob(job, user, applicantName, applicantEmail, applicantPhone, cvFilePath);

            return "redirect:/job/" + jobId + "?applied";

        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/job/" + jobId + "?error";
        }
    }
}
