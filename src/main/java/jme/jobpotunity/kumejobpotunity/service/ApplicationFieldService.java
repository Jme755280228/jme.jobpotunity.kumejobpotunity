package jme.jobpotunity.kumejobpotunity.service;

import jme.jobpotunity.kumejobpotunity.entity.ApplicationField;
import jme.jobpotunity.kumejobpotunity.repository.ApplicationFieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * ApplicationField များကို စီမံခန့်ခွဲသည့် Service
 * CommandLineRunner ကို အသုံးပြု၍ Application စတင်သည်နှင့် စံ Field များကို Database ထဲသို့ တစ်ခါတည်းထည့်သွင်းပေးသည်။
 */
@Service
public class ApplicationFieldService implements CommandLineRunner {

    private final ApplicationFieldRepository applicationFieldRepository;

    @Autowired
    public ApplicationFieldService(ApplicationFieldRepository applicationFieldRepository) {
        this.applicationFieldRepository = applicationFieldRepository;
    }

    /**
     * Application စတင်ချိန်တွင် စံ Application Field များကို ထည့်သွင်းပေးခြင်း
     */
    @Override
    public void run(String... args) throws Exception {
        if (applicationFieldRepository.count() == 0) {

            // စံအဖြစ် သတ်မှတ်မည့် Application Fields များ
            List<ApplicationField> initialFields = Arrays.asList(
                // အခြေခံ အချက်အလက်များ
                new ApplicationField("Profile Picture", "FILE", "General"),
                new ApplicationField("Expected Salary", "NUMBER", "General"),
                new ApplicationField("Years of Experience", "NUMBER", "General"),
                new ApplicationField("Resume/CV Upload", "FILE", "General"),
                new ApplicationField("Cover Letter", "TEXTAREA", "General"),

                // IT/Tech အတွက်
                new ApplicationField("GitHub/Portfolio URL", "TEXT", "Tech"),
                new ApplicationField("Key Programming Languages", "TEXTAREA", "Tech"),
                new ApplicationField("Database Experience", "TEXT", "Tech"),

                // Service/Hospitality အတွက်
                new ApplicationField("Food Safety Certification", "BOOLEAN", "Hospitality"),
                new ApplicationField("Valid Driver's License", "BOOLEAN", "Hospitality"),
                new ApplicationField("Shift Availability", "TEXTAREA", "Hospitality"),

                // အခြား
                new ApplicationField("Professional References (Contact Info)", "TEXTAREA", "Other")
            );

            applicationFieldRepository.saveAll(initialFields);
            System.out.println("✅ Initial Application Fields (Standard Schema) created successfully.");
        }
    }

    /**
     * Available Fields အားလုံးကို ပြန်ယူခြင်း
     */
    public List<ApplicationField> findAllFields() {
        return applicationFieldRepository.findAll();
    }

    /**
     * Field တစ်ခုကို ID ဖြင့် ရှာဖွေခြင်း
     */
    public ApplicationField findById(Long id) {
        return applicationFieldRepository.findById(id).orElse(null);
    }

    /**
     * 🎯 FIX 1.1: Field Category (ဥပမာ- Tech, Hospitality) အလိုက် Field များကို ရှာဖွေခြင်း
     */
    public List<ApplicationField> findByCategory(String category) {
        // Repository မှ findByFieldCategory method ကို အသုံးပြုခြင်း
        return applicationFieldRepository.findByFieldCategory(category);
    }
}


