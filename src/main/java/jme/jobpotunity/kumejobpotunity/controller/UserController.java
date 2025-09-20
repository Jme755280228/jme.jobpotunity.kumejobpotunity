// src/main/java/jme/jobpotunity/kumejobpotunity/controller/UserController.java

package jme.jobpotunity.kumejobpotunity.controller;

import jme.jobpotunity.kumejobpotunity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // login page ကို ပြသပေးမည့် method
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // register form ကို ပြသပေးမည့် method
    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    // form မှ data များကို လက်ခံပြီး user အသစ်ကို database ထဲသို့ သိမ်းဆည်းမည့် method
    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String password, Model model) {
        if (userService.isUsernameTaken(username)) {
            model.addAttribute("error", "ဤအမည်ဖြင့် အကောင့်ရှိပြီးသားဖြစ်ပါသည်။");
            return "register";
        }
        
        userService.registerNewUser(username, password);

        return "redirect:/login";
    }
}
