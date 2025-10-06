package jme.jobpotunity.kumejobpotunity.service;

import jme.jobpotunity.kumejobpotunity.entity.JobPosting;
import jme.jobpotunity.kumejobpotunity.entity.User;
import jme.jobpotunity.kumejobpotunity.repository.JobPostingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JobPostingService {

    private final JobPostingRepository jobPostingRepository;

    @Autowired
    public JobPostingService(JobPostingRepository jobPostingRepository) {
        this.jobPostingRepository = jobPostingRepository;
    }

    public List<JobPosting> findAll() {
        return jobPostingRepository.findAll();
    }

public Page<JobPosting> findAll(Pageable pageable) {
    return jobPostingRepository.findAll(pageable);
}

    public Optional<JobPosting> findById(Long id) {
        return jobPostingRepository.findById(id);
    }

    public JobPosting save(JobPosting jobPosting) {
        if (jobPosting.getId() == null) {
            jobPosting.setPostedDate(LocalDateTime.now()); 
            jobPosting.setApproved(false); 
        }
        return jobPostingRepository.save(jobPosting);
    }

    public void deleteById(Long id) {
        jobPostingRepository.deleteById(id);
    }

    public List<JobPosting> findByEmployerUser(User employerUser) {
        return jobPostingRepository.findByEmployerUser(employerUser);
    }

    /**
     * အတည်ပြုပြီးသား Job များကိုသာ ပြသသည် (Public Facing)
     */
    public Page<JobPosting> findApprovedJobs(Pageable pageable) {
        return jobPostingRepository.findByIsApproved(true, pageable);
    }

    /**
     * Admin Dashboard တွင် အတည်ပြုရန် စောင့်ဆိုင်းနေသော Job များကို ပြသသည်
     * 🎯 FIX: Repository နှင့် ကိုက်ညီစေရန် Pageable ကို လက်ခံပြီး Page<JobPosting> ကို return လုပ်ပါမည်။
     */
    public Page<JobPosting> findPendingJobs(Pageable pageable) {
        return jobPostingRepository.findByIsApproved(false, pageable);
    }

    /**
     * Admin မှ Job ကို အတည်ပြုပေးခြင်း
     */
    public Optional<JobPosting> approveJob(Long jobId) {
        return jobPostingRepository.findById(jobId).map(job -> {
            job.setApproved(true);
            return jobPostingRepository.save(job);
        });
    }

    /**
     * Admin မှ Job ကို ငြင်းပယ်ခြင်း (Reject)
     */
    public Optional<JobPosting> rejectJob(Long jobId) {
        // Reject လုပ်ရင် approved status ကို false ပြန်ထားတာ မှန်ကန်ပါတယ်
        return jobPostingRepository.findById(jobId).map(job -> {
            job.setApproved(false); 
            return jobPostingRepository.save(job);
        });
    }

}

