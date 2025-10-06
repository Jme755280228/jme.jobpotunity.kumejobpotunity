package jme.jobpotunity.kumejobpotunity.repository;

import jme.jobpotunity.kumejobpotunity.entity.JobApplication;
import jme.jobpotunity.kumejobpotunity.entity.JobPosting;
import jme.jobpotunity.kumejobpotunity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // 🎯 NEW IMPORT
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

// 🎯 UPDATED: JpaSpecificationExecutor<JobApplication> ကို ထပ်တိုးခြင်း
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long>, JpaSpecificationExecutor<JobApplication> {

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


