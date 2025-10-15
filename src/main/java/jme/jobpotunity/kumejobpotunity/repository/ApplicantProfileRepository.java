package jme.jobpotunity.kumejobpotunity.repository;

import jme.jobpotunity.kumejobpotunity.domain.user.ApplicantProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantProfileRepository extends JpaRepository<ApplicantProfile, Long> {
    
    // Optional: User ID ဖြင့် ApplicantProfile ကို ရှာဖွေနိုင်သည့် Custom Query
    // Spring Data JPA မှ အလိုအလျောက် Generate လုပ်ပေးနိုင်သည်။
    // Optional<ApplicantProfile> findByUserId(Long userId);
}
