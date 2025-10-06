package jme.jobpotunity.kumejobpotunity.repository;

import jme.jobpotunity.kumejobpotunity.entity.JobApplicantResponse;
import jme.jobpotunity.kumejobpotunity.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JobApplicantResponse Entity အတွက် Repository
 */
@Repository
public interface JobApplicantResponseRepository extends JpaRepository<JobApplicantResponse, Long> {

    // JobApplication တစ်ခုအတွက် Custom Responses အားလုံးကို ရှာဖွေရန်
    List<JobApplicantResponse> findByJobApplication(JobApplication jobApplication);
}


