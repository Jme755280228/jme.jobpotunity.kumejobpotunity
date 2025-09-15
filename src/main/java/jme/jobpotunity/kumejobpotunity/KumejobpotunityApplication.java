package jme.jobpotunity.kumejobpotunity;

import jme.jobpotunity.kumejobpotunity.entity.Company;
import jme.jobpotunity.kumejobpotunity.entity.JobPosting;
import jme.jobpotunity.kumejobpotunity.repository.CompanyRepository;
import jme.jobpotunity.kumejobpotunity.repository.JobPostingRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class KumejobpotunityApplication {

	public static void main(String[] args) {
		SpringApplication.run(KumejobpotunityApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(CompanyRepository companyRepository, JobPostingRepository jobPostingRepository) {
		return args -> {
			// Company Entities တွေကို ဖန်တီးပြီး သိမ်းဆည်းခြင်း
			Company google = new Company("Google", "Technology", "ရန်ကုန်မြို့");
			Company facebook = new Company("Facebook", "Technology", "မန္တလေးမြို့");

			companyRepository.save(google);
			companyRepository.save(facebook);

			// JobPosting Entities တွေကို ဖန်တီးပြီး သိမ်းဆည်းခြင်း
			JobPosting seniorDev = new JobPosting("Senior Developer", "We are looking for a senior developer...", "ရန်ကုန်မြို့", "၂၀၀၀၀၀၀ ကျပ်", "Full-Time", google);
			JobPosting hrManager = new JobPosting("HR Manager", "We need an HR manager with experience...", "မန္တလေးမြို့", "၁၅၀၀၀၀၀ ကျပ်", "Full-Time", facebook);
			JobPosting intern = new JobPosting("Intern", "We need interns for our team...", "ရန်ကုန်မြို့", "၃၀၀၀၀၀ ကျပ်", "Part-Time", google);

			jobPostingRepository.save(seniorDev);
			jobPostingRepository.save(hrManager);
			jobPostingRepository.save(intern);
		};
	}
}
