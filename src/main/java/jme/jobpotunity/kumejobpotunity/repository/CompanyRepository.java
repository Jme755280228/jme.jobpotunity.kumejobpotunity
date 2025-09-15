package jme.jobpotunity.kumejobpotunity.repository;

import jme.jobpotunity.kumejobpotunity.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

// Company Table အတွက် Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    // JpaRepository ကို extend လုပ်လိုက်တာနဲ့ Company object တွေကို database ထဲမှာ
    // သိမ်းဆည်းတာ၊ ရှာဖွေတာ၊ ဖျက်တာ စတဲ့ အခြေခံ လုပ်ဆောင်ချက်တွေ အလိုလို ရလာပါပြီ။
}

