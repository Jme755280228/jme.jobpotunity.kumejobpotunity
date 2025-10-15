package jme.jobpotunity.kumejobpotunity.service;

import jme.jobpotunity.kumejobpotunity.domain.job.JobPosting; // <-- domain layer import အသစ်
import jme.jobpotunity.kumejobpotunity.domain.user.User;     // <-- domain layer import အသစ်
import jme.jobpotunity.kumejobpotunity.repository.JobPostingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobPostingService {

    private final JobPostingRepository jobPostingRepository;

    @Autowired
    public JobPostingService(JobPostingRepository jobPostingRepository) {
        this.jobPostingRepository = jobPostingRepository;
    }

    public JobPosting save(JobPosting jobPosting) {
        return jobPostingRepository.save(jobPosting);
    }

    public Optional<JobPosting> findById(Long id) {
        return jobPostingRepository.findById(id);
    }

    public void deleteById(Long id) {
        jobPostingRepository.deleteById(id);
    }



    public List<JobPosting> searchByTitleOrDescription(String keyword) {
        return jobPostingRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);
    }

    public List<JobPosting> findByEmployer(User employer) {
        // Repository layer မှာ findByEmployerUser ကို findByEmployer လို့ ပြင်ခဲ့တဲ့အတွက် ဒီမှာလည်း လိုက်ပြင်
        return jobPostingRepository.findByEmployer(employer); // <-- findByEmployerUser -> findByEmployer
    }

    public Page<JobPosting> findByApprovalStatus(boolean isApproved, Pageable pageable) {
        return jobPostingRepository.findByIsApproved(isApproved, pageable);
    }
}

