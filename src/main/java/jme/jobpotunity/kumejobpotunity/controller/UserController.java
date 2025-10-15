package jme.jobpotunity.kumejobpotunity.controller;

import jme.jobpotunity.kumejobpotunity.domain.enums.UserRole;
import jme.jobpotunity.kumejobpotunity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Login page ကို ပြသပေးမည့် method
     */
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    /**
     * Register form ကို ပြသပေးမည့် method
     */
    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    /**
     * Form မှ data များကို လက်ခံပြီး user အသစ်ကို database ထဲသို့ သိမ်းဆည်းမည့် method
     */
    @PostMapping("/register")
    public String registerUser(@RequestParam String email, @RequestParam String password, @RequestParam String role, Model model) {
        if (userService.isEmailTaken(email)) {
            model.addAttribute("error", "ဤအီးမေးလ်ဖြင့် အကောင့်ရှိပြီးသားဖြစ်ပါသည်။");
            return "register";
        }

        // Form ကနေလာတဲ့ role value (APPLICANT or EMPLOYER) အပေါ်မူတည်ပြီး Role သတ်မှတ်
        UserRole userRole = "EMPLOYER".equalsIgnoreCase(role) ? UserRole.ROLE_EMPLOYER : UserRole.ROLE_APPLICANT;

        userService.registerNewUser(email, password, Set.of(userRole));

        return "redirect:/login?registered";
    }
}


