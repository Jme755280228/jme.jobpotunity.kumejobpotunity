// src/main/java/jme/jobpotunity/kumejobpotunity/service/JobApplicationService.java

package jme.jobpotunity.kumejobpotunity.service;

import jme.jobpotunity.kumejobpotunity.entity.JobApplication;
import jme.jobpotunity.kumejobpotunity.entity.JobPosting;
import jme.jobpotunity.kumejobpotunity.entity.User;
import jme.jobpotunity.kumejobpotunity.repository.JobApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobApplicationService {

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    public void applyForJob(JobPosting job, User user) {
        JobApplication application = new JobApplication();
        application.setJob(job);
        application.setUser(user);
        jobApplicationRepository.save(application);
    }
}
