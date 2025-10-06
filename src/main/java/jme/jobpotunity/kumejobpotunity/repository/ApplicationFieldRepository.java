package jme.jobpotunity.kumejobpotunity.repository;

import jme.jobpotunity.kumejobpotunity.entity.ApplicationField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ApplicationField Entity အတွက် Repository
 */
@Repository
public interface ApplicationFieldRepository extends JpaRepository<ApplicationField, Long> {

    // FIX: ApplicationField Entity တွင် 'category' မရှိပဲ 'fieldCategory' ရှိသောကြောင့်
    // method နာမည်ကို findByFieldCategory ဟု ပြင်ဆင်လိုက်သည်။
    List<ApplicationField> findByFieldCategory(String category);

    // Field Name ဖြင့် ရှာဖွေရန်
    ApplicationField findByFieldName(String fieldName);
}


