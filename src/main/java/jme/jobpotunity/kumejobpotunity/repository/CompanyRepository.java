// src/main/java/.../repository/CompanyRepository.java

package jme.jobpotunity.kumejobpotunity.repository;

import jme.jobpotunity.kumejobpotunity.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional; // Optional import

public interface CompanyRepository extends JpaRepository<Company, Long> {
    
    // 💡 FIX: CompanyService က ခေါ်တဲ့ Method ကို ထည့်သွင်းရန်
    Optional<Company> findByName(String name);
}

