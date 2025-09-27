package jme.jobpotunity.kumejobpotunity.service;

import jme.jobpotunity.kumejobpotunity.entity.JobPosting;
import jme.jobpotunity.kumejobpotunity.entity.User;
import jme.jobpotunity.kumejobpotunity.repository.JobPostingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class JobPostingService {

    private final JobPostingRepository jobPostingRepository;
    
    @Autowired
    public JobPostingService(JobPostingRepository jobPostingRepository) {
        this.jobPostingRepository = jobPostingRepository;
    }

    /**
     * Job အသစ်ကို သိမ်းဆည်းခြင်း သို့မဟုတ် ရှိပြီးသား Job ကို ပြင်ဆင်ခြင်း
     */
    public JobPosting save(JobPosting jobPosting) {
        if (jobPosting.getPostedDate() == null) {
            jobPosting.setPostedDate(LocalDate.now());
        }
        if (jobPosting.getIsActive() == null) {
            jobPosting.setIsActive(true);
        }
        return jobPostingRepository.save(jobPosting);
    }

    /**
     * Job ID ဖြင့် ရှာဖွေခြင်း
     */
    public Optional<JobPosting> findById(Long id) {
        return jobPostingRepository.findById(id);
    }

    /**
     * Public Job Listing အတွက် Active ဖြစ်သော Job အားလုံးကို ပြန်ပေးခြင်း
     */
    public List<JobPosting> findAllActive() {
        return jobPostingRepository.findByIsActiveTrue();
    }
    
    /**
     * JobController မှ Job Listing အတွက် ခေါ်ယူနိုင်သော Method
     */
    public List<JobPosting> findAll() {
        return jobPostingRepository.findAll();
    }

    /**
     * Employer Dashboard အတွက် Job List ကို ပြန်ပေးခြင်း (EmployerController မှ ခေါ်သည်)
     */
    public List<JobPosting> findByEmployerUser(User employer) {
        // Business Logic ကို လိုအပ်သလို ထည့်သွင်းနိုင်သည် (ဥပမာ- Sort By Posted Date)
        return jobPostingRepository.findByEmployerUser(employer);
    }
    
    /**
     * Job ကို Database မှ ဖျက်ခြင်း
     */
    public void deleteById(Long id) {
        jobPostingRepository.deleteById(id);
    }
}
