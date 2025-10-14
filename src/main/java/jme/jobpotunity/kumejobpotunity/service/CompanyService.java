// src/main/java/jme/jobpotunity/kumejobpotunity/service/CompanyService.java

package jme.jobpotunity.kumejobpotunity.service;

import jme.jobpotunity.kumejobpotunity.entity.Company;
import jme.jobpotunity.kumejobpotunity.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    /**
     * ✅ Company အသစ်ကို သိမ်းခြင်း သို့မဟုတ် ရှိပြီးသား company ကို update လုပ်ခြင်း
     */
    public Company save(Company company) {
        return companyRepository.save(company);
    }

    /**
     * ✅ Company ID ဖြင့် ရှာဖွေရန်
     */
    public Optional<Company> findById(Long id) {
        return companyRepository.findById(id);
    }

    /**
     * ✅ Company Name ဖြင့် ရှာဖွေရန် (DataSeeder မှလည်း သုံးနိုင်)
     */
    public Optional<Company> findByName(String name) {
        return companyRepository.findByName(name);
    }

    /**
     * ✅ Company အားလုံးကို ပြသရန်
     */
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    /**
     * ✅ Company ကို ဖျက်ရန်
     */
    public void deleteById(Long id) {
        companyRepository.deleteById(id);
    }
}
