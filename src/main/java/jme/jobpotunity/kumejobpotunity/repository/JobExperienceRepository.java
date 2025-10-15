package jme.jobpotunity.kumejobpotunity.repository;

import jme.jobpotunity.kumejobpotunity.domain.user.JobExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobExperienceRepository extends JpaRepository<JobExperience, Long> {

    // Optional: ApplicantProfile ID ဖြင့် experience ရှာဖွေနိုင်ရန်
    // List<JobExperience> findByApplicantProfileId(Long applicantProfileId);
}
