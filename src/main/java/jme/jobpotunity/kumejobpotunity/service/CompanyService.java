// src/main/java/.../service/CompanyService.java (CRUD Methods များပါ ထည့်သွင်းပြီး)

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
     * Company အသစ် ဖန်တီးခြင်း သို့မဟုတ် ရှိပြီးသား Company ကို update လုပ်ခြင်း
     */
    public Company save(Company company) {
        return companyRepository.save(company);
    }

    /**
     * Company ID ဖြင့် ရှာဖွေခြင်း
     */
    public Optional<Company> findById(Long id) {
        return companyRepository.findById(id);
    }

    /**
     * Company Name ဖြင့် ရှာဖွေခြင်း (DataSeeder တွင် လိုအပ်သည်)
     */
    public Optional<Company> findByName(String name) {
        // CompanyRepository တွင် findByName(String) method ကို define လုပ်ထားရန် လိုအပ်သည်
        return companyRepository.findByName(name); 
    }

    /**
     * Company အားလုံးကို ရှာဖွေခြင်း
     */
    public List<Company> findAll() {
        return companyRepository.findAll();
    }
    
    /**
     * Company ID ဖြင့် ဖျက်ပစ်ခြင်း
     */
    public void deleteById(Long id) {
        companyRepository.deleteById(id);
    }
}

