package jme.jobpotunity.kumejobpotunity.repository;

import jme.jobpotunity.kumejobpotunity.entity.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;

// JobPosting Table အတွက် Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    // JobPosting object တွေကို database ထဲမှာ ကိုင်တွယ်ဖို့အတွက်
    // အခြေခံ CRUD methods တွေ အကုန်ရရှိပါပြီ။
}

