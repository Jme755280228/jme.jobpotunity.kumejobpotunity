package jme.jobpotunity.kumejobpotunity.repository;

import jme.jobpotunity.kumejobpotunity.entity.JobApplication;
import jme.jobpotunity.kumejobpotunity.entity.JobPosting;
import jme.jobpotunity.kumejobpotunity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    /**
     * Business Rule Check: User တစ်ဦးတည်းက တူညီသော Job ကို ထပ်မံလျှောက်ထားခြင်း ရှိ/မရှိ စစ်ဆေးရန်
     * * @param user လျှောက်ထားသူ
     * @param job လျှောက်ထားသော Job
     * @return တွေ့ရှိပါက JobApplication ကို ပြန်ပေးမည်။
     */
    Optional<JobApplication> findByUserAndJob(User user, JobPosting job);

    /**
     * Employer Portal မှ Applicant List အတွက် Job တစ်ခုချင်းစီကို လျှောက်ထားသူများအားလုံးကို ရှာဖွေရန်
     * * @param job JobPosting Entity
     * @return JobApplication List
     */
    List<JobApplication> findByJob(JobPosting job);
}
