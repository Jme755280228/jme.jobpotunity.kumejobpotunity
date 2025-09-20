// src/main/java/jme/jobpotunity/kumejobpotunity/service/UserService.java

package jme.jobpotunity.kumejobpotunity.service;

import jme.jobpotunity.kumejobpotunity.entity.User;
import jme.jobpotunity.kumejobpotunity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // username ရှိပြီးသားလား စစ်ဆေးသည်
    public boolean isUsernameTaken(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent();
    }

    // user အသစ်ကို database ထဲသို့ သိမ်းဆည်းသည်
    public void registerNewUser(String username, String password) {
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password)); // password ကို encrypt လုပ်သည်
        newUser.setRole("USER"); // default role ကို USER အဖြစ် သတ်မှတ်သည်
        userRepository.save(newUser);
    }

    // Add this method to find a user by their username
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
