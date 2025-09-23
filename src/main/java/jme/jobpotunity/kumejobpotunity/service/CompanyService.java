package jme.jobpotunity.kumejobpotunity.service;

import jme.jobpotunity.kumejobpotunity.entity.Company;
import jme.jobpotunity.kumejobpotunity.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public List<Company> findAllCompanies() {
        return companyRepository.findAll();
    }

    public Optional<Company> findByName(String name) {
        return companyRepository.findByName(name);
    }
    
    public Company save(Company company) {
        return companyRepository.save(company);
    }
}
