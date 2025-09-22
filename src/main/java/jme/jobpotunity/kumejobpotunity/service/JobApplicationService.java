// src/main/java/jme/jobpotunity/kumejobpotunity/service/JobApplicationService.java

package jme.jobpotunity.kumejobpotunity.service;

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

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    public void applyForJob(JobPosting job, User user, String applicantName, String applicantEmail, String applicantPhone, String cvFilePath) {
        JobApplication jobApplication = new JobApplication();
        jobApplication.setJob(job);
        jobApplication.setUser(user);
        jobApplication.setApplicationDate(LocalDate.now());
        jobApplication.setApplicantName(applicantName);
        jobApplication.setApplicantEmail(applicantEmail);
        jobApplication.setApplicantPhone(applicantPhone);
        jobApplication.setCvFilePath(cvFilePath);
        
        jobApplicationRepository.save(jobApplication);
    }
    
     
    // Find applications for a specific job
    public List<JobApplication> findApplicationsByJob(JobPosting job) {
        return jobApplicationRepository.findByJob(job);
    }
    
}
