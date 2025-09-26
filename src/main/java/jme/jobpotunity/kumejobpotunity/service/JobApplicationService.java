package jme.jobpotunity.kumejobpotunity.service;

import jme.jobpotunity.kumejobpotunity.entity.ApplicantProfile;
import jme.jobpotunity.kumejobpotunity.entity.ApplicationStatus; // ApplicationStatus Enum ကို Import လုပ်ပါ
import jme.jobpotunity.kumejobpotunity.entity.JobApplication;
import jme.jobpotunity.kumejobpotunity.entity.JobPosting;
import jme.jobpotunity.kumejobpotunity.entity.User;
import jme.jobpotunity.kumejobpotunity.repository.JobApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    
    // ApplicantProfileService ကို Inject လုပ်ခြင်း (နောက်ပိုင်း Profile ရှိမရှိ စစ်ရန် လိုအပ်နိုင်)
    // private final ApplicantProfileService applicantProfileService;

    @Autowired
    public JobApplicationService(JobApplicationRepository jobApplicationRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
        // this.applicantProfileService = applicantProfileService; // လိုအပ်ရင် Constructor မှာ ထည့်ပါ
    }

    /**
     * အလုပ်လျှောက်လွှာ တင်ခြင်း (Data-Centric Version)
     * CV File Path များအစား ApplicantProfile ကို အသုံးပြုပါမည်။
     */
    public JobApplication applyForJob(JobPosting job, User user, ApplicantProfile applicantProfile) {
        // 1. Profile ရှိမရှိ စစ်ဆေးခြင်း (Controller မှာ အရင်လုပ်ရမည်)
        if (applicantProfile == null) {
             throw new IllegalArgumentException("Cannot apply without a complete Applicant Profile.");
        }
        
        // 2. Job အတွက် Profile ဖြင့် လျှောက်ထားပြီးသားလား စစ်ဆေးနိုင်သည် (Optional)
        // if (jobApplicationRepository.existsByJobAndApplicantProfile(job, applicantProfile)) {
        //     throw new IllegalStateException("Already applied for this job.");
        // }

        // 3. New JobApplication Object ဖန်တီးခြင်း
        JobApplication jobApplication = new JobApplication();
        jobApplication.setJob(job);
        jobApplication.setUser(user);
        jobApplication.setApplicantProfile(applicantProfile); // Profile ကို ချိတ်ဆက်လိုက်ခြင်း
        jobApplication.setApplicationDate(LocalDate.now());
        jobApplication.setStatus(ApplicationStatus.APPLIED); // Status ကို Default အနေဖြင့် သတ်မှတ်ခြင်း

        // OLD FIELDS (applicantName, cvFilePath) များကို ဖယ်ရှားလိုက်ပြီ။

        return jobApplicationRepository.save(jobApplication);
    }
    
     
    // Find applications for a specific job (No Change Needed)
    public List<JobApplication> findApplicationsByJob(JobPosting job) {
        return jobApplicationRepository.findByJob(job);
    }
    
    // JobApplication ကို Id နဲ့ ရှာခြင်း (Employer View အတွက် လိုအပ်နိုင်)
    // public Optional<JobApplication> findById(Long id) {
    //    return jobApplicationRepository.findById(id);
    // }
    
}
