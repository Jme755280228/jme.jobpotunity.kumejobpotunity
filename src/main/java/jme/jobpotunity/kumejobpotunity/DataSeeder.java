package jme.jobpotunity.kumejobpotunity;

import jme.jobpotunity.kumejobpotunity.entity.ApplicantProfile;
import jme.jobpotunity.kumejobpotunity.entity.Education;
import jme.jobpotunity.kumejobpotunity.entity.JobExperience;
import jme.jobpotunity.kumejobpotunity.entity.User;
import jme.jobpotunity.kumejobpotunity.repository.ApplicantProfileRepository; // << NEW
import jme.jobpotunity.kumejobpotunity.repository.UserRepository;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate; // << NEW
import java.util.Optional; // << NEW

@Component
public class DataSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final ApplicantProfileRepository applicantProfileRepository; // << NEW
    private final PasswordEncoder passwordEncoder;

    // Constructor Injection တွင် ApplicantProfileRepository ထည့်သွင်းခြင်း
    public DataSeeder(UserRepository userRepository, ApplicantProfileRepository applicantProfileRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.applicantProfileRepository = applicantProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 1. Admin Account Seed
        seedAdminUser();

        // 2. Sample Applicant Account and Profile Seed
        seedApplicantProfile();
    }
    
    // Admin User ကို Seed လုပ်ခြင်း
    private void seedAdminUser() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setRole("ADMIN");
            userRepository.save(admin);
            System.out.println("Admin account has been created successfully!");
        }
    }
    
    // Sample Applicant Profile ကို Nested Data ဖြင့် Seed လုပ်ခြင်း
    private void seedApplicantProfile() {
        Optional<User> existingUser = userRepository.findByUsername("applicant@test.com");
        
        if (existingUser.isEmpty()) {
            // User အသစ် ဖန်တီးခြင်း
            User applicantUser = new User();
            applicantUser.setUsername("applicant@test.com");
            applicantUser.setPassword(passwordEncoder.encode("password"));
            applicantUser.setRole("APPLICANT");
            applicantUser = userRepository.save(applicantUser);
            
            // Applicant Profile အသစ် ဖန်တီးခြင်း
            ApplicantProfile profile = new ApplicantProfile();
            profile.setFullName("Aung Myat Thu");
            profile.setEmail(applicantUser.getUsername());
            profile.setPhone("09-123456789");
            profile.setCurrentTitle("Senior Software Developer");
            profile.setTotalExperienceYears(5);
            profile.setProfessionalSummary("5+ years of experience in Java Spring Boot development with a focus on scalable data structures.");
            profile.setSkills("Java, Spring Boot, PostgreSQL, AWS, Microservices");
            profile.setLinkedInUrl("https://linkedin.com/in/aung-myat");
            
            // ---------------------------------
            // Nested Data: Job Experience (Data-Centric Core)
            // ---------------------------------
            
            JobExperience exp1 = new JobExperience();
            exp1.setJobTitle("Senior Backend Dev");
            exp1.setCompanyName("Tech Solutions Co.");
            exp1.setStartDate(LocalDate.of(2022, 1, 1));
            exp1.setEndDate(null); // Current Job
            exp1.setDescription("Led the development of microservices using Spring Cloud and maintained CI/CD pipelines.");
            profile.addExperience(exp1); // Helper method ဖြင့် ချိတ်ဆက်ခြင်း
            
            JobExperience exp2 = new JobExperience();
            exp2.setJobTitle("Junior Java Developer");
            exp2.setCompanyName("Startup Myanmar");
            exp2.setStartDate(LocalDate.of(2019, 6, 1));
            exp2.setEndDate(LocalDate.of(2021, 12, 31));
            exp2.setDescription("Developed RESTful APIs and optimized database queries for performance.");
            profile.addExperience(exp2);
            
            // ---------------------------------
            // Nested Data: Education (Data-Centric Core)
            // ---------------------------------
            
            Education edu1 = new Education();
            edu1.setDegree("B.Sc. (Computer Science)");
            edu1.setSchoolName("Yangon University");
            edu1.setMajor("Software Engineering");
            edu1.setGraduationYear(2019);
            edu1.setGpa(4.5);
            profile.addEducation(edu1); // Helper method ဖြင့် ချိတ်ဆက်ခြင်း

            Education edu2 = new Education();
            edu2.setDegree("Advanced English Certification");
            edu2.setSchoolName("British Council");
            edu2.setGraduationYear(2020);
            profile.addEducation(edu2);
            
            // ---------------------------------
            
            // Profile ကို User နှင့် ချိတ်ဆက်ပြီး Save လုပ်ခြင်း
            profile.setUser(applicantUser);
            applicantUser.setApplicantProfile(profile);
            
            applicantProfileRepository.save(profile); // Cascade ကြောင့် Nested Data များပါ သိမ်းဆည်းသွားမည်
            
            System.out.println("Sample Applicant Profile and Nested Data created successfully!");
        }
    }
}

