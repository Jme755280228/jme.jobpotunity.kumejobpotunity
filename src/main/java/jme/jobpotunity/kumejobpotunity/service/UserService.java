// src/main/java/jme/jobpotunity/kumejobpotunity/service/UserService.java

package jme.jobpotunity.kumejobpotunity.service;

import jme.jobpotunity.kumejobpotunity.entity.User;
import jme.jobpotunity.kumejobpotunity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * ✅ username ရှိပြီးသားလား စစ်ဆေးခြင်း
     */
    public boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    /**
     * ✅ အသုံးပြုသူအသစ်ကို စာရင်းသွင်းခြင်း (Default Role: USER)
     */
    public User registerNewUser(String username, String password) {
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRoles(new HashSet<>()); // initialize HashSet
        newUser.getRoles().add("USER");

        return userRepository.save(newUser);
    }

    /**
     * ✅ username ဖြင့် User ရှာဖွေရန်
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
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
    public java.util.List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * ✅ User ကို ဖျက်ရန်
     */
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
