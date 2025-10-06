package jme.jobpotunity.kumejobpotunity.controller;

import jme.jobpotunity.kumejobpotunity.entity.ApplicantProfile;
import jme.jobpotunity.kumejobpotunity.entity.ApplicationField;
import jme.jobpotunity.kumejobpotunity.entity.JobApplicantResponse;
import jme.jobpotunity.kumejobpotunity.entity.JobPosting;
import jme.jobpotunity.kumejobpotunity.entity.User;
import jme.jobpotunity.kumejobpotunity.service.JobPostingService;
import jme.jobpotunity.kumejobpotunity.service.UserService;
import jme.jobpotunity.kumejobpotunity.service.JobApplicationService;
import jme.jobpotunity.kumejobpotunity.util.FileStorageUtil; // File Storage Utility လိုအပ်ပါသည်

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;

@Controller
@RequestMapping("/applicant") // 💡 FIX 1: Base URL ကို /applicant သို့ ပြောင်းလဲ (SecurityConfig နှင့် ကိုက်ညီရန်)
public class JobApplicationController {

    private final JobPostingService jobPostingService;
    private final UserService userService;
    private final JobApplicationService jobApplicationService;
    private final FileStorageUtil fileStorageUtil;

    @Autowired
    public JobApplicationController(JobPostingService jobPostingService,
                                    UserService userService,
                                    JobApplicationService jobApplicationService,
                                    FileStorageUtil fileStorageUtil) {
        this.jobPostingService = jobPostingService;
        this.userService = userService;
        this.jobApplicationService = jobApplicationService;
        this.fileStorageUtil = fileStorageUtil;
    }

    /**
     * 1. Job ၏ Custom Application Form ကို ပြသခြင်း
     * URL: /applicant/apply/{jobId}
     */
    @GetMapping("/apply/{jobId}") // 💡 FIX 2: Mapping ကို /apply/{jobId} သို့ ချိန်ညှိ
    public String showApplicationForm(@PathVariable Long jobId, Model model, Principal principal, RedirectAttributes redirectAttributes) {
        if (principal == null) {
             redirectAttributes.addFlashAttribute("errorMessage", "လျှောက်ထားရန်အတွက် အကောင့်ဝင်ရန် လိုအပ်ပါသည်။");
             return "redirect:/login";
        }

        User user = userService.findByUsername(principal.getName())
                        .orElseThrow(() -> new IllegalStateException("User not found."));

        JobPosting job = jobPostingService.findById(jobId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid job ID."));

        // 🎯 FIX 1.1: Job Approval စစ်ဆေးခြင်း
        if (!job.isApproved()) {
             redirectAttributes.addFlashAttribute("errorMessage", "ဤအလုပ်သည် လျှောက်ထားရန်အတွက် အတည်ပြုထားခြင်းမရှိသေးပါ။");
             return "redirect:/jobs/" + jobId;
        }

        // Applicant Profile ရှိမရှိ စစ်ဆေးခြင်း
        ApplicantProfile profile = user.getProfile();
        if (profile == null) {
             redirectAttributes.addFlashAttribute("errorMessage", "လျှောက်ထားခြင်းမပြုမီ သင်၏ Profile အချက်အလက်များကို ဖြည့်သွင်းပေးပါ။");
             return "redirect:/profile/edit";
        }

        // လျှောက်ထားပြီးသားဟုတ်မဟုတ် စစ်ဆေးခြင်း
        if (jobApplicationService.findByJobAndUser(job, user).isPresent()) {
             redirectAttributes.addFlashAttribute("errorMessage", "ဤအလုပ်ကို သင်လျှောက်ထားပြီး ဖြစ်ပါသည်။");
             return "redirect:/jobs/" + jobId;
        }

        // Custom Fields များကို ID အလိုက် စီပြီး Model ထဲသို့ ပို့ပေးသည်
        List<ApplicationField> requiredFields = job.getRequiredFields().stream()
                .sorted(Comparator.comparing(ApplicationField::getId))
                .collect(Collectors.toList());

        model.addAttribute("job", job);
        // Note: Template က customResponses လို့ မျှော်လင့်ပေမယ့် requiredFields ကိုပဲ ပို့ပါတယ်။ Template က ဒီနာမည်ကို ပြောင်းသုံးရင် ပိုကောင်းပါတယ်။
        model.addAttribute("customResponses", requiredFields); // Template ၏ customResponses ကို ထောက်ပံ့ရန်
        return "job-application-form";
    }

    /**
     * 2. Job ၏ Custom Application Form ကို တင်သွင်းခြင်း
     * URL: /applicant/apply/{jobId}
     */
    @PostMapping("/apply/{jobId}") // 💡 FIX 3: Mapping ကို /apply/{jobId} သို့ ချိန်ညှိ
    public String submitApplicationWithResponses(@PathVariable Long jobId,
                                               @RequestParam(value = "cvFile", required = false) MultipartFile cvFile, // CV file ကို optional အနေနဲ့ ထားရှိ
                                               HttpServletRequest request,
                                               Principal principal,
                                               RedirectAttributes redirectAttributes) { // Model parameter ကို ဖယ်ရှားလိုက်သည် (မလိုအပ်သောကြောင့်)

        if (principal == null) {
             redirectAttributes.addFlashAttribute("errorMessage", "လျှောက်ထားရန်အတွက် အကောင့်ဝင်ရန် လိုအပ်ပါသည်။");
             return "redirect:/login";
        }

        User user = userService.findByUsername(principal.getName())
                        .orElseThrow(() -> new IllegalStateException("User not found."));

        JobPosting job = jobPostingService.findById(jobId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid job ID."));

        // 🎯 FIX 2.1: Job Approval စစ်ဆေးခြင်း
        if (!job.isApproved()) {
             redirectAttributes.addFlashAttribute("errorMessage", "ဤအလုပ်သည် လျှောက်ထားရန်အတွက် အတည်ပြုထားခြင်းမရှိသေးပါ။");
             return "redirect:/jobs/" + jobId;
        }

        ApplicantProfile profile = user.getProfile();
        if (profile == null) {
             redirectAttributes.addFlashAttribute("errorMessage", "လျှောက်ထားခြင်းမပြုမီ သင်၏ Profile အချက်အလက်များကို ဖြည့်သွင်းပေးပါ။");
             return "redirect:/profile/edit";
        }

        if (jobApplicationService.findByJobAndUser(job, user).isPresent()) {
             redirectAttributes.addFlashAttribute("errorMessage", "ဤအလုပ်ကို သင်လျှောက်ထားပြီး ဖြစ်ပါသည်။");
             return "redirect:/jobs/" + jobId;
        }

        // Custom Fields များကို ရယူခြင်း
        List<ApplicationField> requiredFields = job.getRequiredFields().stream()
                .sorted(Comparator.comparing(ApplicationField::getId))
                .collect(Collectors.toList());

        List<JobApplicantResponse> responses = new ArrayList<>();
        boolean hasErrors = false;
        String cvPath = null;

        // --- Custom Responses Validation and Collection ---
        for (ApplicationField field : requiredFields) {
            // 💡 FIX 4: answer_ID မှ response_ID သို့ ပြောင်းလိုက်သည် (Template နှင့် ကိုက်ညီစေရန်)
            String answerKey = "response_" + field.getId();
            String answerValue = request.getParameter(answerKey);

            // 1. Required Field Check
            if (field.isRequired() && (answerValue == null || answerValue.trim().isEmpty())) {
                 redirectAttributes.addFlashAttribute("errorMessage",
                    "'" + field.getFieldName() + "' သည် မဖြစ်မနေ ဖြည့်သွင်းရမည့် အချက်အလက် ဖြစ်ပါသည်။");
                 hasErrors = true;
            } else if (!field.isRequired() && (answerValue == null || answerValue.trim().isEmpty())) {
                // If not required and empty, skip processing the response object
                continue;
            }

            if (hasErrors) continue; // Skip response creation if validation failed

            // 2. Create Response Entity
            JobApplicantResponse response = new JobApplicantResponse();
            response.setApplicationField(field);
            response.setAnswerValue(answerValue);
            responses.add(response);
        }

        // --- CV File Handling ---
        if (cvFile != null && !cvFile.isEmpty()) {
            try {
                // FileStorageUtil သည် 'files/cvs/' ကဲ့သို့သော လမ်းကြောင်းတွင်သိမ်းဆည်းရန်လိုအပ်သည်
                cvPath = fileStorageUtil.storeFile(cvFile, "cvs");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "CV ဖိုင်သိမ်းဆည်းရာတွင် အမှားဖြစ်ပွားပါသည်။");
                hasErrors = true;
            }
        }

        if (hasErrors) {
            // Error ရှိပါက Form ကို ပြန်ပို့သည်
            return "redirect:/applicant/apply/" + jobId; // ပြုပြင်ထားသော လမ်းကြောင်းသို့ ပြန်ပို့
        }

        // --- Final Submission ---
        jobApplicationService.createApplicationWithResponses(job, user, profile, responses, cvPath);

        redirectAttributes.addFlashAttribute("successMessage", "လျှောက်လွှာကို အောင်မြင်စွာ တင်သွင်းပြီးပါပြီ။");
        return "redirect:/jobs/" + jobId;
    }
}


