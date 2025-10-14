package jme.jobpotunity.kumejobpotunity.repository;

import jme.jobpotunity.kumejobpotunity.entity.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {

    // Optional: ApplicantProfile ID ဖြင့် education ရှာဖွေနိုင်ရန်
    // List<Education> findByApplicantProfileId(Long applicantProfileId);
}
