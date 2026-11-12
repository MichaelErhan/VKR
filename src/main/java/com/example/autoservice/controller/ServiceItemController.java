package com.example.autoservice.controller;

import com.example.autoservice.model.ServiceItem;
import com.example.autoservice.model.User;
import com.example.autoservice.service.ServiceItemService;
import com.example.autoservice.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/services")
public class ServiceItemController {

    private final ServiceItemService serviceItemService;
    private final UserService userService;

    public ServiceItemController(ServiceItemService serviceItemService, UserService userService) {
        this.serviceItemService = serviceItemService;
        this.userService = userService;
    }

    @GetMapping({"", "/"})
    public String showServices(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            User user = userService.findUserByUserName(authentication.getName()).orElse(null);
            if (user != null) {
                model.addAttribute("username", user.getUserName());
            }
        }
        model.addAttribute("services", serviceItemService.findAllServiceItems());
        return "services/services";
    }

    @PostMapping("/add")
    public String addService(@ModelAttribute ServiceItem serviceItem) {
        serviceItemService.saveServiceItem(serviceItem);
        return "redirect:/services";
    }

    @PostMapping("/edit")
    public String editService(@ModelAttribute ServiceItem serviceItem) {
        serviceItemService.saveServiceItem(serviceItem);
        return "redirect:/services";
    }

    @PostMapping("/delete/{id}")
    public String deleteService(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            serviceItemService.deleteServiceItem(id);
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error",
                    "Услугу нельзя удалить: она выбрана в заказ-нарядах.");
        }
        return "redirect:/services";
    }
}