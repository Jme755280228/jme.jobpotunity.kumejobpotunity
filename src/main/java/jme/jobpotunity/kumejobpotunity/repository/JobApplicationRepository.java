// src/main/java/.../repository/JobApplicationRepository.java

package jme.jobpotunity.kumejobpotunity.repository;

import jme.jobpotunity.kumejobpotunity.entity.JobApplication;
import jme.jobpotunity.kumejobpotunity.entity.JobPosting;
import jme.jobpotunity.kumejobpotunity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    Optional<JobApplication> findByUserAndJob(User user, JobPosting job);

    List<JobApplication> findByJob(JobPosting job);
    
    /**
     * 💡 FIX: JobApplication ကို Applicant Profile Data ပါ Fetch Join ဖြင့် တစ်ခါတည်း ဆွဲယူရန်
     */
    @Query("SELECT ja FROM JobApplication ja " +
           "LEFT JOIN FETCH ja.applicantProfile ap " +
           "WHERE ja.id = :id")
    Optional<JobApplication> findByIdWithProfile(@Param("id") Long id);
}

