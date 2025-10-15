package jme.jobpotunity.kumejobpotunity.domain.application; // <-- package အသစ်

import jakarta.persistence.*;
import jme.jobpotunity.kumejobpotunity.domain.base.BaseEntity;
import jme.jobpotunity.kumejobpotunity.domain.user.User; // <-- import path အသစ်

import java.time.LocalDateTime;

@Entity
@Table(name = "job_applicant_responses")
public class JobApplicantResponse extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_application_id", nullable = false)
    private JobApplication jobApplication;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responder_id", nullable = false) // Employer or Admin
    private User responder;

    @Column(length = 2000)
    private String message;

    @Column(nullable = false)
    private LocalDateTime responseDate = LocalDateTime.now();

    // Constructors
    public JobApplicantResponse() {}

    // Getters and Setters
    public JobApplication getJobApplication() {
        return jobApplication;
    }

    public void setJobApplication(JobApplication jobApplication) {
        this.jobApplication = jobApplication;
    }

    public User getResponder() {
        return responder;
    }

    public void setResponder(User responder) {
        this.responder = responder;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(LocalDateTime responseDate) {
        this.responseDate = responseDate;
    }
}

