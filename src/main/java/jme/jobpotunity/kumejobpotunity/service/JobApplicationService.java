// src/main/java/jme/jobpotunity/kumejobpotunity/service/JobApplicationService.java

package jme.jobpotunity.kumejobpotunity.service;

import jme.jobpotunity.kumejobpotunity.entity.JobApplication;
import jme.jobpotunity.kumejobpotunity.entity.JobPosting;
import jme.jobpotunity.kumejobpotunity.entity.User;
import jme.jobpotunity.kumejobpotunity.repository.JobApplicationRepository;
import jme.jobpotunity.kumejobpotunity.repository.JobPostingRepository;
import jme.jobpotunity.kumejobpotunity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobPostingRepository jobPostingRepository;
    private final UserRepository userRepository;

    @Autowired
    public JobApplicationService(JobApplicationRepository jobApplicationRepository,
                                 JobPostingRepository jobPostingRepository,
                                 UserRepository userRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.jobPostingRepository = jobPostingRepository;
        this.userRepository = userRepository;
    }

    /**
     * Job Application အသစ်တစ်ခု ဖန်တီးခြင်း
     */
    public JobApplication createApplication(Long jobId, Long userId, String coverLetter) {
        Optional<JobPosting> jobPostingOpt = jobPostingRepository.findById(jobId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (jobPostingOpt.isEmpty() || userOpt.isEmpty()) {
            throw new IllegalArgumentException("Job posting or user not found.");
        }

        JobApplication application = new JobApplication();
        application.setJobPosting(jobPostingOpt.get());
        application.setUser(userOpt.get());
        application.setCoverLetter(coverLetter);
        application.setStatus("PENDING"); // Default status

        return jobApplicationRepository.save(application);
    }

    /**
     * Job Application ကို ID ဖြင့် ရှာဖွေခြင်း
     */
    public Optional<JobApplication> findById(Long id) {
        return jobApplicationRepository.findById(id);
    }

    /**
     * Job Application အားလုံးကို ရှာဖွေခြင်း
     */
    public List<JobApplication> findAll() {
        return jobApplicationRepository.findAll();
    }

    /**
     * တစ်ဦးချင်းစီအတွက် Job Application များကို ရှာဖွေခြင်း
     */
    public List<JobApplication> findApplicationsByUser(User user) {
        return jobApplicationRepository.findByUser(user);
    }

    /**
     * အလုပ်ခန့်အပ်သူ (employer) အနေနဲ့ Job Posting တစ်ခုအတွက် လျှောက်လွှာများ ရှာခြင်း
     */
    public List<JobApplication> findApplicationsByJob(JobPosting jobPosting) {
        return jobApplicationRepository.findByJobPosting(jobPosting);
    }

    /**
     * Application Status ကို update လုပ်ခြင်း (e.g. ACCEPTED, REJECTED)
     */
    public JobApplication updateStatus(Long applicationId, String newStatus) {
        Optional<JobApplication> optionalApp = jobApplicationRepository.findById(applicationId);
        if (optionalApp.isEmpty()) {
            throw new IllegalArgumentException("Application not found");
        }
        JobApplication application = optionalApp.get();
        application.setStatus(newStatus);
        return jobApplicationRepository.save(application);
    }

    /**
     * Application ကို ဖျက်ခြင်း
     */
    public void deleteById(Long id) {
        jobApplicationRepository.deleteById(id);
    }
}
