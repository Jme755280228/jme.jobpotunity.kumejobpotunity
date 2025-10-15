package jme.jobpotunity.kumejobpotunity.service;

import jme.jobpotunity.kumejobpotunity.domain.application.JobApplication; // <-- domain layer import အသစ်
import jme.jobpotunity.kumejobpotunity.domain.enums.ApplicationStatus;    // <-- enum import အသစ်
import jme.jobpotunity.kumejobpotunity.domain.job.JobPosting;      // <-- domain layer import အသစ်
import jme.jobpotunity.kumejobpotunity.domain.user.ApplicantProfile; // <-- domain layer import အသစ်
import jme.jobpotunity.kumejobpotunity.domain.user.User;            // <-- domain layer import အသစ်
import jme.jobpotunity.kumejobpotunity.repository.JobApplicationRepository;
import jme.jobpotunity.kumejobpotunity.repository.JobPostingRepository;
import jme.jobpotunity.kumejobpotunity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
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
    public JobApplication createApplication(Long jobId, Long userId) { // <-- coverLetter ကို ဖယ်ရှား
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        
        JobPosting jobPosting = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job posting not found with id: " + jobId));

        // <-- Logic အသစ်: User မှာ ApplicantProfile ရှိမှ လျှောက်ခွင့်ပြုရန်
        ApplicantProfile applicantProfile = user.getApplicantProfile();
        if (applicantProfile == null) {
            throw new IllegalStateException("User must create an applicant profile before applying for a job.");
        }

        JobApplication application = new JobApplication();
        application.setJobPosting(jobPosting);
        application.setApplicantProfile(applicantProfile); // <-- setUser အစား setApplicantProfile
        application.setStatus(ApplicationStatus.PENDING); // <-- String အစား Enum ကိုသုံး

        return jobApplicationRepository.save(application);
    }

    // ... (findById, findAll, findApplicationsByJobPosting methods are okay with just import changes)

    /**
     * Application Status ကို update လုပ်ခြင်း
     */
    public JobApplication updateStatus(Long applicationId, ApplicationStatus newStatus) { // <-- String -> ApplicationStatus enum
        JobApplication application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found with id: " + applicationId));
        
        application.setStatus(newStatus); // <-- newStatus is now an enum
        return jobApplicationRepository.save(application);
    }
    
    // ... other methods like findById, deleteById
}

