package jme.jobpotunity.kumejobpotunity.service;

import jme.jobpotunity.kumejobpotunity.entity.ApplicantProfile;
import jme.jobpotunity.kumejobpotunity.entity.ApplicationStatus;
import jme.jobpotunity.kumejobpotunity.entity.JobApplicantResponse;
import jme.jobpotunity.kumejobpotunity.entity.JobApplication;
import jme.jobpotunity.kumejobpotunity.entity.JobPosting;
import jme.jobpotunity.kumejobpotunity.entity.User;
import jme.jobpotunity.kumejobpotunity.repository.JobApplicationRepository;
import jme.jobpotunity.kumejobpotunity.repository.JobApplicantResponseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobApplicantResponseRepository jobApplicantResponseRepository;

    @Autowired
    public JobApplicationService(JobApplicationRepository jobApplicationRepository,
                                 JobApplicantResponseRepository jobApplicantResponseRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.jobApplicantResponseRepository = jobApplicantResponseRepository;
    }

    /**
     * Job Application အသစ်တစ်ခုကို Custom Field Responses များနှင့်အတူ သိမ်းဆည်းခြင်း
     * @param job JobPosting entity
     * @param user Applicant User entity
     * @param profile ApplicantProfile entity
     * @param responses Custom responses list
     * @param cvPath CV File Path
     * @return Saved JobApplication entity
     */
    @Transactional
    public JobApplication createApplicationWithResponses(JobPosting job, User user, ApplicantProfile profile, List<JobApplicantResponse> responses, String cvPath) {

        // 1. Job Application Entity ကို ဖန်တီးခြင်း
        JobApplication application = new JobApplication();
        application.setJob(job);
        application.setUser(user);
        application.setApplicantProfile(profile);
        application.setApplicationDate(LocalDate.now());
        application.setStatus(ApplicationStatus.APPLIED);
        
        // CV File Path ကို ထည့်သွင်းခြင်း
        application.setCvPath(cvPath);

        // 2. Job Application ကို အရင်ဆုံး သိမ်းဆည်းခြင်း (ID ရရှိရန်)
        JobApplication savedApplication = jobApplicationRepository.save(application);

        // 3. Custom Responses များကို Application နှင့် ချိတ်ဆက်ပြီး သိမ်းဆည်းခြင်း
        if (responses != null && !responses.isEmpty()) {
            for (JobApplicantResponse response : responses) {
                response.setJobApplication(savedApplication);
                jobApplicantResponseRepository.save(response);
            }
        }

        return savedApplication;
    }
    
    /**
     * Jobတစ်ခုအတွက် လျှောက်ထားသူများကို Status, အတွေ့အကြုံနှင့် Keyword များဖြင့် စစ်ထုတ်ခြင်း
     * (Digital CV (Structured Data) ကို အခြေခံသော Filtering)
     */
    @Transactional(readOnly = true)
    public List<JobApplication> findApplicationsByJobAndFilters(JobPosting job, String status, Integer minExp, String keyword) {

        // 1. String status ကို Enum အဖြစ် ပြောင်းလဲခြင်း
        ApplicationStatus applicationStatus = null;
        if (status != null && !status.trim().isEmpty()) {
            try {
                applicationStatus = ApplicationStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Invalid status string, ignore status filter
            }
        }

        // 2. Dynamic Specification ကိုတည်ဆောက်ခြင်း
        Specification<JobApplication> spec = buildFilterSpecification(job, applicationStatus, minExp, keyword);

        // 3. Repository ကိုခေါ်ယူပြီး Filtering ပြုလုပ်ခြင်း
        return jobApplicationRepository.findAll(spec);
    }

    // ... (buildFilterSpecification remains the same) ...

    /**
     * 🎯 JPA Specification Builder: Dynamic Query Logic ကို တည်ဆောက်သည်
     */
    private Specification<JobApplication> buildFilterSpecification(JobPosting job, ApplicationStatus status, Integer minExp, String keyword) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Job Filter (MUST be applied)
            predicates.add(criteriaBuilder.equal(root.get("job"), job));

            // 2. Status Filter (Optional)
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            // 3. Minimum Experience Filter (Optional)
            if (minExp != null && minExp > 0) {
                // Join to ApplicantProfile
                Join<JobApplication, ApplicantProfile> profileJoin = root.join("applicantProfile");
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(profileJoin.get("totalExperienceYears"), minExp));
            }

            // 4. Keyword Search Filter (Optional, Multi-field search)
            if (keyword != null && !keyword.trim().isEmpty()) {
                String searchLike = "%" + keyword.toLowerCase() + "%";
                
                // Join to ApplicantProfile (if not already joined by minExp)
                Join<JobApplication, ApplicantProfile> profileJoin = root.join("applicantProfile");
                
                // Join to JobApplicantResponse
                Join<JobApplication, JobApplicantResponse> responsesJoin = root.join("responses");

                // Create OR conditions for keyword
                Predicate keywordSearch = criteriaBuilder.or(
                    // Search in ApplicantProfile (skills)
                    criteriaBuilder.like(criteriaBuilder.lower(profileJoin.get("skills")), searchLike),
                    // Search in ApplicantProfile (summary)
                    criteriaBuilder.like(criteriaBuilder.lower(profileJoin.get("professionalSummary")), searchLike),
                    // Search in JobApplicantResponse (answerValue)
                    criteriaBuilder.like(criteriaBuilder.lower(responsesJoin.get("answerValue")), searchLike)
                );
                
                predicates.add(keywordSearch);
                
                // NOTE: Query တွင် Distinct ပြုလုပ်ရန် လိုအပ်သည် (Responses join ကြောင့် Duplication ဖြစ်နိုင်သည်)
                query.distinct(true);
            }
            
            // Result order: ID descending (Newest applications first)
            query.orderBy(criteriaBuilder.desc(root.get("id")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }


    /**
     * 🎯 NEW METHOD: လျှောက်လွှာ၏ အခြေအနေ (Status) ကို ပြောင်းလဲခြင်း။
     * @param applicationId ပြောင်းလဲလိုသော လျှောက်လွှာ၏ ID
     * @param newStatusName ပြောင်းလဲလိုသော အခြေအနေ (String: e.g., "REVIEWING", "INTERVIEW")
     * @return အောင်မြင်စွာ သိမ်းဆည်းပြီးသော JobApplication entity
     * @throws IllegalArgumentException Application ID သို့မဟုတ် Status Name မှားယွင်းပါက
     */
    @Transactional
    public JobApplication updateApplicationStatus(Long applicationId, String newStatusName) {
        
        JobApplication application = jobApplicationRepository.findById(applicationId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid application ID: " + applicationId));
        
        try {
            ApplicationStatus newStatus = ApplicationStatus.valueOf(newStatusName.toUpperCase());
            application.setStatus(newStatus);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status name provided: " + newStatusName);
        }
        
        return jobApplicationRepository.save(application);
    }


    // --- Existing Methods ---

    public Optional<JobApplication> findById(Long id) {
        return jobApplicationRepository.findById(id);
    }

    public Optional<JobApplication> findByIdWithProfile(Long id) {
        return jobApplicationRepository.findByIdWithProfile(id); // Custom fetch join method
    }

    public Optional<JobApplication> findByJobAndUser(JobPosting job, User user) {
        return jobApplicationRepository.findByUserAndJob(user, job);
    }

    public List<JobApplication> findApplicationsByJob(JobPosting job) {
        return jobApplicationRepository.findByJob(job);
    }

    public JobApplication save(JobApplication application) {
        return jobApplicationRepository.save(application);
    }

    /**
     * JobApplication တစ်ခုအတွက် Custom Responses များကို ရယူခြင်း
     */
    public List<JobApplicantResponse> findResponsesByApplication(JobApplication application) {
        return jobApplicantResponseRepository.findByJobApplication(application);
    }
}


