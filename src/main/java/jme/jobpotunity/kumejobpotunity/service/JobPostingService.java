package jme.jobpotunity.kumejobpotunity.service;

import jme.jobpotunity.kumejobpotunity.entity.JobPosting;
import jme.jobpotunity.kumejobpotunity.repository.JobPostingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class JobPostingService {

    @Autowired
    private JobPostingRepository jobPostingRepository;

    // JobPosting အားလုံးကို database မှ ရယူမည်
    public List<JobPosting> findAllJobPostings() {
        return jobPostingRepository.findAll();
    }

    // JobPosting အသစ်တစ်ခုကို database ထဲသို့ ထည့်သွင်းမည်
    public JobPosting saveJobPosting(JobPosting jobPosting) {
        return jobPostingRepository.save(jobPosting);
    }
}

