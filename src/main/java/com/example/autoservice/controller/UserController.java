package com.example.autoservice.controller;

import com.example.autoservice.model.PartsDelivery;
import com.example.autoservice.model.User;
import com.example.autoservice.service.PartsDeliveryService;
import com.example.autoservice.service.UserService;
import com.example.autoservice.util.Role;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    private void addUsername(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            User user = userService.findUserByUserName(authentication.getName()).orElse(null);
            if (user != null) {
                model.addAttribute("username", user.getUserName());
            }
        }
    }

    @GetMapping("/")
    public String showIndexPage(Model model) {
        addUsername(model);
        return "index";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/parts-delivery")
    public String listPartsDeliveries() {
        return "redirect:/parts-delivery/admin";
    }

    @GetMapping("/parts-delivery/admin")
    public String showPartsDeliveryAdminPanel(Model model) {
        addUsername(model);
        List<PartsDelivery> partsDeliveries = partsDeliveryService.findAllPartsDeliveriesSortedByDate();
        model.addAttribute("partsDeliveries", partsDeliveries);
        return "parts-delivery/admin";
    }

    @GetMapping("/admin_panel")
    public String showAdminPanel(Model model) {
        addUsername(model);
        model.addAttribute("users", userService.findAllUsers());
        return "admin/admin_panel";
    }

    @PostMapping("/admin_panel/add")
    public String addUser(@RequestParam String userName,
                          @RequestParam String password,
                          @RequestParam Role role,
                          RedirectAttributes redirectAttributes) {
        try {
            userService.createUser(userName.trim(), password, role);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin_panel";
    }

    @PostMapping("/update")
    public String updateRoles(
            @RequestParam Map<String, String> roles,
            @RequestParam(value = "deleteUser", required = false) Long userIdToDelete,
            RedirectAttributes redirectAttributes) {

        if (userIdToDelete != null) {
            try {
                userService.deleteUserById(userIdToDelete);
            } catch (DataIntegrityViolationException e) {
                redirectAttributes.addFlashAttribute("error",
                        "Пользователя нельзя удалить: он является автором заказ-нарядов.");
            }
            return "redirect:/admin_panel";
        }

        // Обновление ролей пользователей
        roles.forEach((key, value) -> {
            try {
                Long userId = Long.parseLong(key.replace("roles[", "").replace("]", ""));
                Role role = Role.valueOf(value);
                userService.updateUserRole(userId, String.valueOf(role));
            } catch (Exception e) {
                // Параметры формы, не относящиеся к ролям, пропускаются
            }
        });

        return "redirect:/admin_panel";
    }
}