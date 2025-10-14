package jme.jobpotunity.kumejobpotunity.service;

import jme.jobpotunity.kumejobpotunity.entity.ApplicationField;
import jme.jobpotunity.kumejobpotunity.repository.ApplicationFieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Dynamic / extra application fields management
 * (optional for demo)
 */
@Service
public class ApplicationFieldService {

    private final ApplicationFieldRepository applicationFieldRepository;

    @Autowired
    public ApplicationFieldService(ApplicationFieldRepository applicationFieldRepository) {
        this.applicationFieldRepository = applicationFieldRepository;
    }

    // Save or update a field
    public ApplicationField save(ApplicationField field) {
        return applicationFieldRepository.save(field);
    }

    // Find by ID
    public Optional<ApplicationField> findById(Long id) {
        return applicationFieldRepository.findById(id);
    }

    // Delete by ID
    public void deleteById(Long id) {
        applicationFieldRepository.deleteById(id);
    }

    // List all dynamic fields
    public List<ApplicationField> findAll() {
        return applicationFieldRepository.findAll();
    }
}
