package jme.jobpotunity.kumejobpotunity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.security.Principal;

@Controller
@RequestMapping("/admin") // Base URL for all Admin-specific paths
public class AdminController {

    /**
     * Admin Login အောင်မြင်ပြီးနောက် Redirect လုပ်လာမည့် Dashboard
     * URL: /admin/jobs (SecurityConfig တွင် သတ်မှတ်ထားသော Redirect Path)
     * Template Name: admin-jobs-list
     */
    @GetMapping("/jobs")
    public String showAdminDashboard(Model model, Principal principal) {
        // Admin user name ကို Template မှာ ပြသရန်
        model.addAttribute("adminName", principal.getName());
        return "admin-jobs"; 
    }
}

