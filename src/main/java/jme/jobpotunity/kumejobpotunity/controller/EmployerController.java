package jme.jobpotunity.kumejobpotunity.controller;

import jme.jobpotunity.kumejobpotunity.entity.ApplicationField;
import jme.jobpotunity.kumejobpotunity.entity.JobPosting;
import jme.jobpotunity.kumejobpotunity.entity.User;
import jme.jobpotunity.kumejobpotunity.service.ApplicationFieldService;
import jme.jobpotunity.kumejobpotunity.service.JobPostingService;
import jme.jobpotunity.kumejobpotunity.service.UserService;
import jme.jobpotunity.kumejobpotunity.service.JobApplicationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

import java.security.Principal;
import java.time.LocalDateTime; // LocalDateTime ကို သုံးရန်
import java.util.ArrayList;
import java.util.Comparator; 
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/employer/jobs") 
public class EmployerController {

    private final JobPostingService jobPostingService;
    private final UserService userService;
    private final ApplicationFieldService applicationFieldService;
    private final JobApplicationService jobApplicationService;

    @Autowired
    public EmployerController(JobPostingService jobPostingService, UserService userService,
                              ApplicationFieldService applicationFieldService, JobApplicationService jobApplicationService) {
        this.jobPostingService = jobPostingService;
        this.userService = userService;
        this.applicationFieldService = applicationFieldService;
        this.jobApplicationService = jobApplicationService;
    }

    /**
     * Employer Dashboard (သူတင်ထားသော Job များကိုသာ ပြသည်)
     * URL: /employer/jobs
     */
    @GetMapping
    public String showEmployerJobs(Model model, Principal principal) {
        User employer = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalStateException("Employer user not found."));

        List<JobPosting> jobs = jobPostingService.findByEmployerUser(employer);
        
        model.addAttribute("jobs", jobs);
        model.addAttribute("employerName", principal.getName());
        return "employer-jobs"; // employer-jobs.html ကို ပြန်ပို့သည်
    }

    @GetMapping("/new")
    public String showNewJobForm(Model model) {
        JobPosting jobPosting = new JobPosting();
        jobPosting.setRequiredFields(new ArrayList<>());
        model.addAttribute("jobPosting", jobPosting);
        model.addAttribute("isNew", true);
        return "job-form";
    }

    @GetMapping("/edit/{id}")
    public String showEditJobForm(@PathVariable Long id, Model model, Principal principal, RedirectAttributes redirectAttributes) {
        JobPosting jobPosting = jobPostingService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid job ID: " + id));

        // Security Check
        if (!jobPosting.getEmployerUser().getUsername().equals(principal.getName())) {
            redirectAttributes.addFlashAttribute("errorMessage", "သင့် Job မဟုတ်၍ ပြင်ဆင်ခွင့် မရှိပါ။");
            return "redirect:/employer/jobs";
        }

        List<ApplicationField> sortedFields = jobPosting.getRequiredFields().stream()
                .sorted(Comparator.comparing(ApplicationField::getId))
                .collect(Collectors.toList());

        jobPosting.setRequiredFields(sortedFields);

        model.addAttribute("jobPosting", jobPosting);
        model.addAttribute("isNew", false);
        return "job-form";
    }


    /**
     * Job အသစ်/Job ပြင်ဆင်ချက်များကို သိမ်းဆည်းခြင်း (Post/Put Logic)
     * URL: /employer/jobs/save
     */
    @PostMapping("/save")
    public String saveJob(@ModelAttribute @Valid JobPosting jobPosting, BindingResult result,
                          @RequestParam(value = "fieldId", required = false) List<Long> fieldIds,
                          @RequestParam(value = "fieldName", required = false) List<String> fieldNames,
                          @RequestParam(value = "fieldType", required = false) List<String> fieldTypes,
                          @RequestParam(value = "isRequired", required = false) List<Boolean> isRequiredList,
                          Model model, // Model ကို ထည့်သွင်းထားသည်
                          Principal principal, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            boolean isNew = jobPosting.getId() == null;
            model.addAttribute("isNew", isNew);
            return "job-form"; 
        }

        User employer = userService.findByUsername(principal.getName())
                        .orElseThrow(() -> new IllegalStateException("Employer user not found."));

        jobPosting.setEmployerUser(employer);

        // --- Approval Status Handling (Job တွင် setApproved() method ကို အသုံးပြုသည်) ---
        if (jobPosting.getId() == null) {
            jobPosting.setApproved(false); 
            // Date ကို Service မှာ auto-set လုပ်ပေးသည်
            redirectAttributes.addFlashAttribute("successMessage", "အလုပ်အသစ်ကို အောင်မြင်စွာ တင်ပြီးပါပြီ။ Admin ၏ အတည်ပြုချက်ကို စောင့်ဆိုင်းပေးပါ။");
        } else {
            // Job Update
            JobPosting existingJob = jobPostingService.findById(jobPosting.getId())
                                            .orElseThrow(() -> new IllegalArgumentException("Invalid job ID for update."));
            
            // Approval status နဲ့ Posted Date ကို မပြောင်းလဲစေရန် ထိန်းသိမ်းထားသည်
            jobPosting.setApproved(existingJob.isApproved());
            jobPosting.setPostedDate(existingJob.getPostedDate());
            redirectAttributes.addFlashAttribute("successMessage", "အလုပ်အချက်အလက်များကို အောင်မြင်စွာ ပြင်ဆင်ပြီးပါပြီ။");
        }

        // --- Handle Custom Fields (Set to List Conversion Error ဖြေရှင်းရန်) ---
        List<ApplicationField> requiredFields = new ArrayList<>();
        if (fieldNames != null) {
            for (int i = 0; i < fieldNames.size(); i++) {
                if (fieldNames.get(i) != null && !fieldNames.get(i).trim().isEmpty()) {
                    ApplicationField field = new ApplicationField();
                    // Field ID ရှိမှသာ Existing Field ဖြစ်သည် (update လုပ်သည်)
                    if (fieldIds != null && i < fieldIds.size() && fieldIds.get(i) != null) {
                        field.setId(fieldIds.get(i));
                    }
                    field.setFieldName(fieldNames.get(i));
                    field.setFieldType(fieldTypes.get(i));
                    // isRequiredList က null မဟုတ်/size ရှိ/value ရှိရင် true, မဟုတ်ရင် false
                    field.setRequired(isRequiredList != null && i < isRequiredList.size() && isRequiredList.get(i) != null && isRequiredList.get(i)); 
                    field.setJobPosting(jobPosting);
                    requiredFields.add(field);
                }
            }
        }
        
        // setRequiredFields သည် List<ApplicationField> ကို လက်ခံသည်
        jobPosting.setRequiredFields(requiredFields);
        jobPostingService.save(jobPosting);

        return "redirect:/employer/jobs";
    }

    // [deleteJob, showApplicants, showApplicationDetail methods များသည် ယခင်အတိုင်း ဆက်လက်ပါဝင်သည်]

    @GetMapping("/delete/{id}")
    public String deleteJob(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        JobPosting jobPosting = jobPostingService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid job ID: " + id));

        if (!jobPosting.getEmployerUser().getUsername().equals(principal.getName())) {
            redirectAttributes.addFlashAttribute("errorMessage", "သင့် Job မဟုတ်၍ ဖျက်ပစ်ခွင့် မရှိပါ။");
            return "redirect:/employer/jobs";
        }

        jobPostingService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Job Posting ကို အောင်မြင်စွာ ဖျက်ပစ်ပြီးပါပြီ။");
        return "redirect:/employer/jobs";
    }

    @GetMapping("/{id}/applicants")
    public String showApplicants(@PathVariable Long id, Model model, Principal principal, RedirectAttributes redirectAttributes) {
        //... (Applicant logic)
        // Ensure this method's body is complete based on your original code
        return "job-applicants";
    }
    
    @GetMapping("/{jobId}/application/{applicationId}")
    public String showApplicationDetail(@PathVariable Long jobId, @PathVariable Long applicationId, Model model, Principal principal, RedirectAttributes redirectAttributes) {
        //... (Application detail logic)
        // Ensure this method's body is complete based on your original code
        return "application-detail";
    }
}


