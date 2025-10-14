package jme.jobpotunity.kumejobpotunity;

import jme.jobpotunity.kumejobpotunity.entity.*;
import jme.jobpotunity.kumejobpotunity.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final JobPostingRepository jobPostingRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final JobApplicantResponseRepository jobApplicantResponseRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository,
                      CompanyRepository companyRepository,
                      JobPostingRepository jobPostingRepository,
                      JobApplicationRepository jobApplicationRepository,
                      JobApplicantResponseRepository jobApplicantResponseRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.jobPostingRepository = jobPostingRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.jobApplicantResponseRepository = jobApplicantResponseRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) seedUsers();
        if (companyRepository.count() == 0 && jobPostingRepository.count() == 0) seedCompaniesAndJobs();
        if (jobApplicationRepository.count() == 0) seedApplicationsAndResponses();
        System.out.println("Seeding completed!");
    }

    private void seedUsers() {
        User admin = new User();
        admin.setEmail("admin@example.com");
        admin.setPassword(passwordEncoder.encode("adminpass"));
        admin.setRoles(new HashSet<>(Arrays.asList("ADMIN")));
        userRepository.save(admin);

        User employer = new User();
        employer.setEmail("employer@example.com");
        employer.setPassword(passwordEncoder.encode("employerpass"));
        employer.setRoles(new HashSet<>(Arrays.asList("EMPLOYER")));
        userRepository.save(employer);

        User applicant = new User();
        applicant.setEmail("applicant@example.com");
        applicant.setPassword(passwordEncoder.encode("applicantpass"));
        applicant.setRoles(new HashSet<>(Arrays.asList("APPLICANT")));

        ApplicantProfile profile = new ApplicantProfile();
        profile.setUser(applicant);
        profile.setFullName("John Doe");
        profile.setPhone("0912345678");
        profile.setAddress("Yangon, Myanmar");
        profile.setSummary("Passionate Java developer with 3 years of experience.");

        Education edu1 = new Education();
        edu1.setApplicantProfile(profile);
        edu1.setSchoolName("Yangon University");
        edu1.setDegree("B.Sc Computer Science");
        edu1.setGraduationYear(2020);

        JobExperience exp1 = new JobExperience();
        exp1.setApplicantProfile(profile);
        exp1.setCompanyName("Tech Innovators");
        exp1.setPosition("Junior Developer");
        exp1.setStartYear(2020);
        exp1.setEndYear(2022);

        profile.getEducations().add(edu1);
        profile.getJobExperiences().add(exp1);

        userRepository.save(applicant);
    }

    private void seedCompaniesAndJobs() {
        User employer = userRepository.findByEmail("employer@example.com")
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        Company company = new Company();
        company.setName("Tech Innovators Inc.");
        company.setDescription("A global leader in software solutions.");
        company.setContactEmail("contact@techinnovators.com");
        companyRepository.save(company);

        JobPosting job1 = new JobPosting();
        job1.setTitle("Senior Full-Stack Developer");
        job1.setDescription("Lead backend and frontend projects using Spring Boot and React.");
        job1.setEmployer(employer);
        job1.setApproved(true);
        job1.setPostedDate(LocalDateTime.now());
        job1.setCompany(company);
        jobPostingRepository.save(job1);

        JobPosting job2 = new JobPosting();
        job2.setTitle("Junior QA Tester");
        job2.setDescription("Entry-level position for testing software applications.");
        job2.setEmployer(employer);
        job2.setApproved(true);
        job2.setPostedDate(LocalDateTime.now().minusDays(3));
        job2.setCompany(company);
        jobPostingRepository.save(job2);
    }

    private void seedApplicationsAndResponses() {
        User applicant = userRepository.findByEmail("applicant@example.com")
                .orElseThrow(() -> new RuntimeException("Applicant not found"));

        JobPosting job1 = jobPostingRepository.findByTitle("Senior Full-Stack Developer")
                .orElseThrow(() -> new RuntimeException("Job not found"));

        JobApplication application1 = new JobApplication();
        application1.setApplicantProfile(applicant.getApplicantProfile());
        application1.setJobPosting(job1);
        jobApplicationRepository.save(application1);

        JobApplicantResponse response1 = new JobApplicantResponse();
        response1.setJobApplication(application1);
        response1.setMessage("Your profile looks great. We will contact you for interview.");
        jobApplicantResponseRepository.save(response1);
    }
}
