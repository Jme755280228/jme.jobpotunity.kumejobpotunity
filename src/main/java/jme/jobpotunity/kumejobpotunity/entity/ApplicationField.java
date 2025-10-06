package jme.jobpotunity.kumejobpotunity.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "application_fields")
public class ApplicationField {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fieldName;
    private String fieldType;
    private String fieldCategory;
    private boolean required;

    // 🎯 FIX 3.1: JobPosting နှင့် Many-to-One relationship ကို ထည့်သွင်းခြင်း
    // ApplicationField တစ်ခုသည် JobPosting တစ်ခုနှင့် သက်ဆိုင်သည်။
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_posting_id")
    private JobPosting jobPosting;
    
    // --- Constructors ---
    public ApplicationField() {
    }

    public ApplicationField(String fieldName, String fieldType, String fieldCategory) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.fieldCategory = fieldCategory;
        this.required = false; 
    }

    // --- Getters and Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldCategory() {
        return fieldCategory;
    }

    public void setFieldCategory(String fieldCategory) {
        this.fieldCategory = fieldCategory;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    // 🎯 FIX 3.2: EmployerController မှ ခေါ်ဆိုသော setJobPosting() method ကို ထည့်သွင်းခြင်း
    public JobPosting getJobPosting() {
        return jobPosting;
    }

    public void setJobPosting(JobPosting jobPosting) {
        this.jobPosting = jobPosting;
    }
}
