// src/main/java/.../repository/JobPostingRepository.java (Final Code)

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
     * Public Job Listing အတွက် အသုံးပြုနိုင်သော Method
     * Job များကို Active Status ဖြင့်သာ ပြသရန်။
     */
    List<JobPosting> findByIsActiveTrue();
    
    /**
     * 💡 NEW: Home Page အတွက် Feature လုပ်ရန် Job များကို ရယူခြင်း
     * Query: TOP 3 Jobs where isActive is True, ordered by the most recent postedDate
     */
    List<JobPosting> findTop3ByIsActiveTrueOrderByPostedDateDesc();

    /**
     * Job Search အတွက် အသုံးပြုနိုင်သော Method
     */
    List<JobPosting> findByTitleContainingIgnoreCase(String title);
}

