package jme.jobpotunity.kumejobpotunity.repository;

import jme.jobpotunity.kumejobpotunity.domain.application.JobApplication; // <-- package အသစ်သို့ ပြောင်းလဲထားသည်
import jme.jobpotunity.kumejobpotunity.domain.job.JobPosting;      // <-- package အသစ်သို့ ပြောင်းလဲထားသည်
import jme.jobpotunity.kumejobpotunity.domain.user.User;            // <-- package အသစ်သို့ ပြောင်းလဲထားသည်
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long>, JpaSpecificationExecutor<JobApplication> {

    // 💡 NOTE: Naming convention အရ findByUserAndJobPosting က ပိုရှင်းလင်းသော်လည်း မူရင်းအတိုင်း ထားပေးသည်
    Optional<JobApplication> findByUserAndJob(User user, JobPosting job);

    List<JobApplication> findByJobPosting(JobPosting job); // <-- field name 'jobPosting' နှင့် ကိုက်ညီအောင် ပြင်ဆင်

    /**
     * 💡 FIX: JobApplication ကို Applicant Profile Data ပါ Fetch Join ဖြင့် တစ်ခါတည်း ဆွဲယူရန်
     */
    @Query("SELECT ja FROM JobApplication ja " +
           "LEFT JOIN FETCH ja.applicantProfile ap " +
           "WHERE ja.id = :id")
    Optional<JobApplication> findByIdWithProfile(@Param("id") Long id);
}

