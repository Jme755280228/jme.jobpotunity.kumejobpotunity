package jme.jobpotunity.kumejobpotunity.entity;

import jakarta.persistence.*;

/**
 * Job လျှောက်ထားရာတွင် Job Posting မှ တောင်းဆိုသော
 * စိတ်ကြိုက် Field များ (ApplicationField) အတွက် လျှောက်ထားသူ၏ အဖြေများကို သိမ်းဆည်းသည့် Entity.
 * ၎င်းသည် JobApplication တစ်ခုနှင့် ApplicationField တစ်ခုစီကို ချိတ်ဆက်သည်။
 */
@Entity
@Table(name = "job_applicant_responses")
public class JobApplicantResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ဤအဖြေသည် မည်သည့် Job Application နှင့် သက်ဆိုင်သနည်း။ (One-to-Many relationship)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_application_id", nullable = false)
    private JobApplication jobApplication;

    // ဤအဖြေသည် မည်သည့် စံ Field Schema နှင့် သက်ဆိုင်သနည်း။ (EAGER load ဖြင့် Field Name ကို ယူရန်)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "application_field_id", nullable = false)
    private ApplicationField applicationField;

    // လျှောက်ထားသူ၏ အဖြေတန်ဖိုး (STRING အဖြစ် သိမ်းဆည်းထားသည်)
    @Lob // Long text (or file path/number serialized as string)
    private String answerValue;

    // Default Constructor
    public JobApplicantResponse() {}

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JobApplication getJobApplication() {
        return jobApplication;
    }

    public void setJobApplication(JobApplication jobApplication) {
        this.jobApplication = jobApplication;
    }

    public ApplicationField getApplicationField() {
        return applicationField;
    }

    public void setApplicationField(ApplicationField applicationField) {
        this.applicationField = applicationField;
    }

    public String getAnswerValue() {
        return answerValue;
    }

    public void setAnswerValue(String answerValue) {
        this.answerValue = answerValue;
    }
}


