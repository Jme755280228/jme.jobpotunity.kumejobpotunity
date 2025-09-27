package jme.jobpotunity.kumejobpotunity.repository;

import jme.jobpotunity.kumejobpotunity.entity.JobPosting;
import jme.jobpotunity.kumejobpotunity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    /**
     * Employer Dashboard အတွက် လိုအပ်သော Method
     * Employer တစ်ဦးတည်းက တင်ထားသော Job များစာရင်းကို ရှာဖွေခြင်း။
     */
    List<JobPosting> findByEmployerUser(User employer); 
    
    /**
     * Job Controller ၏ Public Listing အတွက် အသုံးပြုနိုင်သော Method
     * Job များကို Active Status ဖြင့်သာ ပြသရန်။
     */
    List<JobPosting> findByIsActiveTrue();
    
    /**
     * Job Search အတွက် အသုံးပြုနိုင်သော Method
     */
    List<JobPosting> findByTitleContainingIgnoreCase(String title);
}
