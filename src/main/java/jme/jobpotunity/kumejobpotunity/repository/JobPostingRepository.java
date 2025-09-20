package jme.jobpotunity.kumejobpotunity.repository;

import jme.jobpotunity.kumejobpotunity.entity.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
// JobPosting Table အတွက် Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    // JobPosting object တွေကို database ထဲမှာ ကိုင်တွယ်ဖို့အတွက်
    // အခြေခံ CRUD methods တွေ အကုန်ရရှိပါပြီ။
    
    // အလုပ်အမည် (title) သို့မဟုတ် တည်နေရာ (location) ပေါ်မူတည်ပြီး ရှာဖွေနိုင်သည်
    List<JobPosting> findByTitleContainingIgnoreCaseOrLocationContainingIgnoreCase(String title, String location);
    
}

