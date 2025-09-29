// src/main/java/.../service/JobApplicationService.java

package jme.jobpotunity.kumejobpotunity.service;

import jme.jobpotunity.kumejobpotunity.entity.ApplicantProfile;
import jme.jobpotunity.kumejobpotunity.entity.ApplicationStatus;
import jme.jobpotunity.kumejobpotunity.entity.JobApplication;
import jme.jobpotunity.kumejobpotunity.entity.JobPosting;
import jme.jobpotunity.kumejobpotunity.entity.User;
import jme.jobpotunity.kumejobpotunity.repository.JobApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;

    @Autowired
    public JobApplicationService(JobApplicationRepository jobApplicationRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
    }

    /**
     * အလုပ်လျှောက်လွှာ တင်ခြင်း
     */
    public JobApplication applyForJob(JobPosting job, User user, ApplicantProfile applicantProfile) {
        if (applicantProfile == null) {
             throw new IllegalArgumentException("Cannot apply without a complete Applicant Profile.");
        }
        if (jobApplicationRepository.findByUserAndJob(user, job).isPresent()) {
             throw new IllegalStateException("You have already applied for this job.");
        }

        JobApplication jobApplication = new JobApplication();
        jobApplication.setJob(job);
        jobApplication.setUser(user);
        jobApplication.setApplicantProfile(applicantProfile);
        jobApplication.setApplicationDate(LocalDate.now());
        jobApplication.setStatus(ApplicationStatus.APPLIED);

        return jobApplicationRepository.save(jobApplication);
    }


    public List<JobApplication> findApplicationsByJob(JobPosting job) {
        return jobApplicationRepository.findByJob(job);
    }

    public Optional<JobApplication> findById(Long id) {
       return jobApplicationRepository.findById(id);
    }

    /**
     * 💡 FIX: JobApplication ကို Applicant Profile Data ပါ Fetch Join ဖြင့် တစ်ခါတည်း ဆွဲယူရန်
     */
    public Optional<JobApplication> findByIdWithProfile(Long id) {
        return jobApplicationRepository.findByIdWithProfile(id);
    }
}

