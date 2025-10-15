package jme.jobpotunity.kumejobpotunity.repository;

import jme.jobpotunity.kumejobpotunity.domain.job.JobPosting; // <-- package အသစ်သို့ ပြောင်းလဲထားသည်
import jme.jobpotunity.kumejobpotunity.domain.user.User;     // <-- package အသစ်သို့ ပြောင်းလဲထားသည်
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    List<JobPosting> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
        String title,
        String description
    );

    List<JobPosting> findByEmployer(User employer); // <-- User type ကို import အသစ်မှ ရယူသည်

    List<JobPosting> findByEmployer_Id(Long employerId);

    Page<JobPosting> findByIsApproved(boolean isApproved, Pageable pageable);
}

