package jme.jobpotunity.kumejobpotunity.entity;

import jakarta.persistence.*;

/**
 * ApplicationField Entity
 * -----------------------
 * ✅ Each field belongs to a JobApplication
 * ✅ Stores dynamic form data (e.g., resume, cover letter, answers)
 */
@Entity
@Table(name = "application_fields")
public class ApplicationField extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_application_id", nullable = false)
    private JobApplication jobApplication;

    @Column(nullable = false, length = 100)
    private String fieldName;

    @Column(length = 2000)
    private String fieldValue;

    // ------------------------
    // Constructors
    // ------------------------
    public ApplicationField() {}

    public ApplicationField(JobApplication jobApplication, String fieldName, String fieldValue) {
        this.jobApplication = jobApplication;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    // ------------------------
    // Getters & Setters
    // ------------------------
    public JobApplication getJobApplication() { return jobApplication; }
    public void setJobApplication(JobApplication jobApplication) { this.jobApplication = jobApplication; }

    public String getFieldName() { return fieldName; }
    public void setFieldName(String fieldName) { this.fieldName = fieldName; }

    public String getFieldValue() { return fieldValue; }
    public void setFieldValue(String fieldValue) { this.fieldValue = fieldValue; }
}
