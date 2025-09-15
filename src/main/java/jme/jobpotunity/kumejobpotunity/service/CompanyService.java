package jme.jobpotunity.kumejobpotunity.service;

import jme.jobpotunity.kumejobpotunity.entity.Company;
import jme.jobpotunity.kumejobpotunity.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service // Spring ကို ဒီ class ဟာ Service component ဖြစ်ကြောင်း ပြောပြတာပါ
public class CompanyService {

    @Autowired // Repository ကို ဒီမှာ အသုံးပြုဖို့အတွက် ခေါ်ယူလိုက်တာပါ
    private CompanyRepository companyRepository;

    // Company အားလုံးကို database မှ ရယူမည်
    public List<Company> findAllCompanies() {
        return companyRepository.findAll();
    }

    // Company အသစ်တစ်ခုကို database ထဲသို့ ထည့်သွင်းမည်
    public Company saveCompany(Company company) {
        return companyRepository.save(company);
    }
}

