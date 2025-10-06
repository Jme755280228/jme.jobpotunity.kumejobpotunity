package jme.jobpotunity.kumejobpotunity.repository;

import jme.jobpotunity.kumejobpotunity.entity.JobPosting;
import jme.jobpotunity.kumejobpotunity.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    
    List<JobPosting> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
        String title, 
        String description
    );
    
    List<JobPosting> findByEmployerUser(User employer);
    
    List<JobPosting> findByEmployerUser_Id(Long employerId);

    // 🎯 FIX: Service layer မှ ခေါ်ဆိုသော method နှင့် ကိုက်ညီစေရန် ပြင်ဆင်ခြင်း။
    // findByIsApprovedTrue() နှင့် findByIsApprovedFalse() အစား ဤ method တစ်ခုတည်းကို အသုံးပြုပါမည်။
    // Pageable parameter ထည့်သွင်းပြီး Pagination ကိုပါ ထောက်ပံ့ပေးထားပါသည်။
    Page<JobPosting> findByIsApproved(boolean isApproved, Pageable pageable);

}
