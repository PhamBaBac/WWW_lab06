package vn.edu.iuh.fit.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import vn.edu.iuh.fit.models.Post;
import vn.edu.iuh.fit.models.User;
import vn.edu.iuh.fit.service.UserService;

import java.time.Instant;
import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "user/login_form";
    }
    @PostMapping("/login")
    public String login(@ModelAttribute("user") User user, Model model, HttpSession session) {
        Optional<User> validatedUser = userService.validateUser(user.getEmail(), user.getPassword());
        if (validatedUser.isPresent()) {
            session.setAttribute("user", validatedUser.get());
            return "redirect:/posts";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "user/login_form";
        }
    }
    @GetMapping("/register")
    public String showCreatePostForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "user/register";
    }
    @PostMapping("register")
    public String createPost(@ModelAttribute("user") User user) {
        user.setRegisteredAt(Instant.now());
        userService.saveUser(user);
        return "redirect:/login";
    }
}

