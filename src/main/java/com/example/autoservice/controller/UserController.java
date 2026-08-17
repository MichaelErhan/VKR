package com.example.autoservice.controller;

import com.example.autoservice.model.PartsDelivery;
import com.example.autoservice.model.User;
import com.example.autoservice.service.PartsDeliveryService;
import com.example.autoservice.service.UserService;
import com.example.autoservice.util.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    private final UserService userService;
    private final PartsDeliveryService partsDeliveryService;

    public UserController(UserService userService, PartsDeliveryService partsDeliveryService) {
        this.userService = userService;
        this.partsDeliveryService = partsDeliveryService;
    }

    @GetMapping("/")
    public String showIndexPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findUserByUserName(username).orElse(null);
            if (user != null) {
                model.addAttribute("username", user.getUserName());
            }
        }
        return "index";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            userService.registerUser(user);
            return "redirect:/login"; // Перенаправление на страницу входа
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage()); // Добавляем ошибку в модель
            return "register"; // Возвращаемся на страницу регистрации с ошибкой
        }
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "login";
    }

    @GetMapping("/parts-delivery")
    public String listPartsDeliveries(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findUserByUserName(username).orElse(null);
            if (user != null && user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.MODERATOR)) {
                return "redirect:/parts-delivery/admin";
            }
            model.addAttribute("username", user.getUserName());
        }
        List<PartsDelivery> partsDeliveries = partsDeliveryService.findAllPartsDeliveriesSortedByDate();
        model.addAttribute("partsDeliveries", partsDeliveries);
        return "parts-delivery/list";
    }

    @GetMapping("/parts-delivery/admin")
    public String showPartsDeliveryAdminPanel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findUserByUserName(username).orElse(null);
            model.addAttribute("username", user.getUserName());
        }
        List<PartsDelivery> partsDeliveries = partsDeliveryService.findAllPartsDeliveriesSortedByDate();
        model.addAttribute("partsDeliveries", partsDeliveries);

        return "parts-delivery/admin";
    }

    @GetMapping("/admin_panel")
    public String showAdminPanel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findUserByUserName(username).orElse(null);
            model.addAttribute("username", user.getUserName());
        }
        List<User> userslist = userService.findAllUsers();
        model.addAttribute("users", userslist);
        return "admin/admin_panel";
    }

    @PostMapping("/update")
    public String updateRoles(
            @RequestParam Map<String, String> roles,
            @RequestParam(value = "deleteUser", required = false) Long userIdToDelete) {

        if (userIdToDelete != null) {
            userService.deleteUserById(userIdToDelete); // Удаление пользователя
            return "redirect:/admin_panel"; // Возврат на страницу админки
        }

        // Обновление ролей пользователей
        roles.forEach((key, value) -> {
            try {
                Long userId = Long.parseLong(key.replace("roles[", "").replace("]", ""));
                Role role = Role.valueOf(value); // Преобразуем строку в Enum
                userService.updateUserRole(userId, String.valueOf(role));
            } catch (Exception e) {
                // Логирование или обработка ошибок, если данные некорректны
            }
        });

        return "redirect:/admin_panel";
    }
}