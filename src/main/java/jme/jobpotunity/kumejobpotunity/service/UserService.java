package jme.jobpotunity.kumejobpotunity.service;

import jme.jobpotunity.kumejobpotunity.domain.enums.UserRole; // <-- enum import အသစ်
import jme.jobpotunity.kumejobpotunity.domain.user.User;      // <-- domain layer import အသစ်
import jme.jobpotunity.kumejobpotunity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set; // <-- Set import ထည့်သွင်း

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * ✅ email ရှိပြီးသားလား စစ်ဆေးခြင်း
     */
    public boolean isEmailTaken(String email) { // <-- username -> email
        return userRepository.findByEmail(email).isPresent(); // <-- findByUsername -> findByEmail
    }

    /**
     * ✅ အသုံးပြုသူအသစ်ကို စာရင်းသွင်းခြင်း (Default Role: APPLICANT)
     */
    public User registerNewUser(String email, String password, Set<UserRole> roles) { // <-- username -> email, roles type ပြောင်း
        User newUser = new User();
        newUser.setEmail(email); // <-- setUsername -> setEmail
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRoles(roles); // <-- String "USER" အစား enum Set ကို တိုက်ရိုက်သုံး

        return userRepository.save(newUser);
    }

    /**
     * ✅ email ဖြင့် User ရှာဖွေရန်
     */
    public Optional<User> findByEmail(String email) { // <-- username -> email
        return userRepository.findByEmail(email); // <-- findByUsername -> findByEmail
    }

    /**
     * ✅ User ID ဖြင့် ရှာဖွေရန်
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * ✅ အသုံးပြုသူအားလုံးကို ပြသရန်
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * ✅ User ကို ဖျက်ရန်
     */
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}

