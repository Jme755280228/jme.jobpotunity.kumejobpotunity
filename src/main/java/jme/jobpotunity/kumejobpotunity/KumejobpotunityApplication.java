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

}
