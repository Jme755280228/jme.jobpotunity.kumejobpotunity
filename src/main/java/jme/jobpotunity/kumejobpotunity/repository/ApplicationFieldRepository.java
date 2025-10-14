package jme.jobpotunity.kumejobpotunity.repository;

import jme.jobpotunity.kumejobpotunity.entity.ApplicationField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationFieldRepository extends JpaRepository<ApplicationField, Long> {

    // Optional: JobApplication ID ဖြင့် fields ရှာဖွေနိုင်ရန်
    // List<ApplicationField> findByJobApplicationId(Long jobApplicationId);
}
