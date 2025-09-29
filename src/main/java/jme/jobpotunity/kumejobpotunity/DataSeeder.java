// src/main/java/.../DataSeeder.java

package jme.jobpotunity.kumejobpotunity;

import jme.jobpotunity.kumejobpotunity.entity.ApplicantProfile;
import jme.jobpotunity.kumejobpotunity.entity.Education;
import jme.jobpotunity.kumejobpotunity.entity.JobExperience;
import jme.jobpotunity.kumejobpotunity.entity.User;
import jme.jobpotunity.kumejobpotunity.entity.JobPosting;
import jme.jobpotunity.kumejobpotunity.entity.Company;

import jme.jobpotunity.kumejobpotunity.repository.ApplicantProfileRepository;
import jme.jobpotunity.kumejobpotunity.repository.UserRepository;
import jme.jobpotunity.kumejobpotunity.repository.JobPostingRepository;
import jme.jobpotunity.kumejobpotunity.repository.CompanyRepository;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class DataSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final ApplicantProfileRepository applicantProfileRepository;
    private final JobPostingRepository jobPostingRepository;
    private final CompanyRepository companyRepository; 
    private final PasswordEncoder passwordEncoder;

    // Constructor Injection
    public DataSeeder(UserRepository userRepository,
                      ApplicantProfileRepository applicantProfileRepository,
                      JobPostingRepository jobPostingRepository,
                      CompanyRepository companyRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.applicantProfileRepository = applicantProfileRepository;
        this.jobPostingRepository = jobPostingRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        seedAdminUser();
        seedApplicantProfile();
        seedJobPostings();
    }

    // ----------------------------------------------------
    // Seeding Methods 
    // ----------------------------------------------------

    private void seedAdminUser() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setRole("ADMIN");
            userRepository.save(admin);
            System.out.println("Admin account (admin/password) has been created successfully!");
        }
    }

    private void seedApplicantProfile() {
        Optional<User> existingUser = userRepository.findByUsername("applicant@test.com");

        if (existingUser.isEmpty()) {
            User applicantUser = new User();
            applicantUser.setUsername("applicant@test.com");
            applicantUser.setPassword(passwordEncoder.encode("password"));
            applicantUser.setRole("APPLICANT");
            applicantUser = userRepository.save(applicantUser);

            ApplicantProfile profile = new ApplicantProfile();
            profile.setFullName("Aung Myat Thu");
            profile.setEmail(applicantUser.getUsername());
            profile.setCurrentTitle("Senior Backend Developer");


            // Nested Data: Job Experience (1)
            JobExperience exp1 = new JobExperience();
            exp1.setJobTitle("Senior Backend Dev");
            exp1.setCompanyName("Tech Solutions Co.");
            exp1.setStartDate(LocalDate.of(2022, 1, 1));
            profile.addExperience(exp1);

            // Nested Data: Education (1)
            Education edu1 = new Education();
            edu1.setDegree("B.Sc. (Computer Science)");
            edu1.setSchoolName("Yangon University");
            edu1.setGraduationYear(2019);
            profile.addEducation(edu1);

            profile.setUser(applicantUser);
            applicantProfileRepository.save(profile);

            System.out.println("Sample Applicant Profile (applicant@test.com/password) created successfully!");
        }
    }

    private void seedJobPostings() {
        if (jobPostingRepository.count() == 0) {
            User adminUser = userRepository.findByUsername("admin")
                              .orElseThrow(() -> new IllegalStateException("Admin user must be seeded first."));

            // Sample Company Data
            Company sampleCompany = new Company();
            sampleCompany.setName("Global Tech Inc.");
            sampleCompany.setIndustry("Software Development");
            sampleCompany.setLocation("Yangon");

            sampleCompany = companyRepository.save(sampleCompany);

            // Job 1: Senior Java Backend Engineer (Remote)
            JobPosting job1 = new JobPosting();
            job1.setTitle("Senior Java Backend Engineer");
            job1.setDescription("Develop and maintain high-traffic APIs using Spring Boot and Postgres.");
            job1.setLocation("Yangon, Myanmar (Remote)");
            job1.setSalary("2500 USD - 4000 USD");
            job1.setPostedDate(LocalDate.now());
            job1.setIsActive(true);
            
            // 💡 NEW FILTERING DATA
            job1.setJobType("Remote"); 
            job1.setCategory("Software Development");

            job1.setEmployerUser(adminUser);
            job1.setCompany(sampleCompany);

            jobPostingRepository.save(job1);

            // Job 2: Junior QA Tester (Full-Time)
            JobPosting job2 = new JobPosting();
            job2.setTitle("Junior QA Tester");
            job2.setDescription("Perform manual and automated testing for web applications.");
            job2.setLocation("Mandalay, Myanmar");
            job2.setSalary("600,000 MMK - 1,000,000 MMK");
            job2.setPostedDate(LocalDate.now().minusDays(3));
            job2.setIsActive(true);
            
            // 💡 NEW FILTERING DATA
            job2.setJobType("Full-Time");
            job2.setCategory("Quality Assurance");

            job2.setEmployerUser(adminUser);
            job2.setCompany(sampleCompany);

            jobPostingRepository.save(job2);
            
            // Job 3: Digital Marketing Specialist (Hybrid) - New for variety
            JobPosting job3 = new JobPosting();
            job3.setTitle("Digital Marketing Specialist");
            job3.setDescription("Develop and execute digital marketing campaigns across various channels.");
            job3.setLocation("Yangon, Myanmar");
            job3.setSalary("1200 USD - 2000 USD");
            job3.setPostedDate(LocalDate.now());
            job3.setIsActive(true);
            
            // 💡 NEW FILTERING DATA
            job3.setJobType("Hybrid");
            job3.setCategory("Marketing");
            
            job3.setEmployerUser(adminUser);
            job3.setCompany(sampleCompany);
            
            jobPostingRepository.save(job3);


            System.out.println("Sample Job Postings created successfully! (3 Jobs)");
        }
    }
}

