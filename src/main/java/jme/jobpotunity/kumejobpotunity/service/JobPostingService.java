package jme.jobpotunity.kumejobpotunity.service;

import jme.jobpotunity.kumejobpotunity.entity.JobPosting;
import jme.jobpotunity.kumejobpotunity.repository.JobPostingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobPostingService {

    @Autowired
    private JobPostingRepository jobPostingRepository;

    // အလုပ်အားလုံးကို database မှ ရှာဖွေပြီး ပြန်ပေးသည်။
    public List<JobPosting> findAllJobPostings() {
        return jobPostingRepository.findAll();
    }
    
    // search functionality အတွက် method အသစ်
    // title (သို့) location ကို query ဖြင့် ရှာဖွေပေးသည်။
    public List<JobPosting> searchJobPostings(String query) {
        return jobPostingRepository.findByTitleContainingIgnoreCaseOrLocationContainingIgnoreCase(query, query);
    }

    // Job Posting အသစ် သို့မဟုတ် ရှိပြီးသား Job Posting ကို database ထဲသို့ သိမ်းဆည်းသည်။
    public void saveJobPosting(JobPosting jobPosting) {
        jobPostingRepository.save(jobPosting);
    }
    
    // ID ဖြင့် Job Posting တစ်ခုကို ရှာဖွေသည်။
    public JobPosting findById(Long id) {
        Optional<JobPosting> result = jobPostingRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new RuntimeException("Job Posting not found with id: " + id);
        }
    }
    
    // ID ဖြင့် Job Posting ကို database မှ ဖျက်ပစ်သည်။
    public void deleteJobPosting(Long id) {
        jobPostingRepository.deleteById(id);
    }
}
