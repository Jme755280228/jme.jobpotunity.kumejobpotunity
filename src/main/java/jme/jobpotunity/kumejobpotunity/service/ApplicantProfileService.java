package jme.jobpotunity.kumejobpotunity.service;

import jme.jobpotunity.kumejobpotunity.entity.ApplicantProfile;
import jme.jobpotunity.kumejobpotunity.repository.ApplicantProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApplicantProfileService {

    private final ApplicantProfileRepository applicantProfileRepository;

    @Autowired
    public ApplicantProfileService(ApplicantProfileRepository applicantProfileRepository) {
        this.applicantProfileRepository = applicantProfileRepository;
    }

    public ApplicantProfile save(ApplicantProfile profile) {
        return applicantProfileRepository.save(profile);
    }

    public Optional<ApplicantProfile> findById(Long id) {
        return applicantProfileRepository.findById(id);
    }

    public void deleteById(Long id) {
        applicantProfileRepository.deleteById(id);
    }

    // Optional: User ID ဖြင့် Profile ရှာဖို့
    public Optional<ApplicantProfile> findByUserId(Long userId) {
        // Repository မှ findByUserId method define လုပ်ထားရမယ်
        return Optional.empty();
    }
}
