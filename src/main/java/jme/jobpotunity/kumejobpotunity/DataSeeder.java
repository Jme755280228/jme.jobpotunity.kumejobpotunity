package jme.jobpotunity.kumejobpotunity;

import jme.jobpotunity.kumejobpotunity.entity.User;
import jme.jobpotunity.kumejobpotunity.entity.JobPosting;
import jme.jobpotunity.kumejobpotunity.entity.Company; 
import jme.jobpotunity.kumejobpotunity.repository.UserRepository;
import jme.jobpotunity.kumejobpotunity.repository.JobPostingRepository;
import jme.jobpotunity.kumejobpotunity.repository.CompanyRepository; 
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime; // FIX: Date Type Error ဖြေရှင်းရန် LocalDateTime ကို သုံးသည်
import java.util.Arrays;
import java.util.HashSet;

/**
 * အစမ်းသုံးရန် အချက်အလက်များကို Database ထဲသို့ ထည့်သွင်းပေးသည်
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final JobPostingRepository jobPostingRepository;
    private final CompanyRepository companyRepository; 
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, JobPostingRepository jobPostingRepository, CompanyRepository companyRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jobPostingRepository = jobPostingRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            seedUsers();
        }
        if (companyRepository.count() == 0 && jobPostingRepository.count() == 0) {
            seedCompanyAndJobs();
        }
    }

    private void seedUsers() {
        // 1. ADMIN
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("adminpass"));
        admin.setRoles(new HashSet<>(Arrays.asList("ADMIN")));
        userRepository.save(admin);

        // 2. EMPLOYER
        User employer = new User();
        employer.setUsername("employer");
        employer.setPassword(passwordEncoder.encode("employerpass"));
        employer.setRoles(new HashSet<>(Arrays.asList("EMPLOYER")));
        userRepository.save(employer);

        // 3. APPLICANT
        User applicant = new User();
        applicant.setUsername("applicant");
        applicant.setPassword(passwordEncoder.encode("applicantpass"));
        applicant.setRoles(new HashSet<>(Arrays.asList("APPLICANT")));
        userRepository.save(applicant);
    }

    private void seedCompanyAndJobs() {
        User employer = userRepository.findByUsername("employer").orElseThrow(() -> new RuntimeException("Employer not found"));

        // Sample Company
        Company sampleCompany = new Company();
        sampleCompany.setName("Tech Innovators Inc.");
        sampleCompany.setDescription("A global leader in software solutions.");
        sampleCompany.setContactEmail("contact@techinnovators.com");
        companyRepository.save(sampleCompany);


        // --- Job 1: Approved, Public ---
        JobPosting job1 = new JobPosting();
        job1.setTitle("Senior Full-Stack Developer (Remote)");
        job1.setDescription("Seeking a seasoned developer to lead backend and frontend projects using Spring Boot and React.");
        job1.setLocation("Remote");
        job1.setEmploymentType("Full-Time");
        job1.setEmployerUser(employer);
        
        // FIX: Missing setters များနှင့် Date Type များကို ပြင်ဆင်သည်
        job1.setSalary("2500 USD - 4000 USD");
        job1.setPostedDate(LocalDateTime.now()); 
        job1.setApproved(true); // FIX: setApproved ကိုသာ အသုံးပြုသည်
        job1.setCategory("Software Development");
        job1.setCompany(sampleCompany);
        
        jobPostingRepository.save(job1);


        // --- Job 2: Approved, Public ---
        JobPosting job2 = new JobPosting();
        job2.setTitle("Junior QA Tester");
        job2.setDescription("Entry-level position for testing software applications for bugs and inconsistencies.");
        job2.setLocation("Yangon, Myanmar");
        job2.setEmploymentType("Full-Time");
        job2.setEmployerUser(employer);

        // FIX: Missing setters များနှင့် Date Type များကို ပြင်ဆင်သည်
        job2.setSalary("600,000 MMK - 1,000,000 MMK");
        job2.setPostedDate(LocalDateTime.now().minusDays(3)); 
        job2.setApproved(true); 
        job2.setCategory("Quality Assurance");
        job2.setCompany(sampleCompany);
        
        jobPostingRepository.save(job2);


        // --- Job 3: Pending Approval ---
        JobPosting job3 = new JobPosting();
        job3.setTitle("Digital Marketing Specialist");
        job3.setDescription("Manage and optimize online marketing campaigns, including SEO and social media.");
        job3.setLocation("Mandalay, Myanmar");
        job3.setEmploymentType("Hybrid");
        job3.setEmployerUser(employer);
        
        // FIX: Missing setters များနှင့် Date Type များကို ပြင်ဆင်သည်
        job3.setSalary("1200 USD - 2000 USD");
        job3.setPostedDate(LocalDateTime.now()); 
        job3.setApproved(false); 
        job3.setCategory("Marketing");
        job3.setCompany(sampleCompany);

        jobPostingRepository.save(job3);
    }
}


