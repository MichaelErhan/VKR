package com.example.autoservice.controller;

import com.example.autoservice.model.RepairBooking;
import com.example.autoservice.model.User;
import com.example.autoservice.service.RepairBookingService;
import com.example.autoservice.service.UserService;
import com.example.autoservice.util.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/repairs")
public class RepairBookingController {

    private final RepairBookingService repairBookingService;
    private final UserService userService;

    public RepairBookingController(RepairBookingService repairBookingService, UserService userService) {
        this.repairBookingService = repairBookingService;
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String showAdminPanel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findUserByUserName(username).orElse(null);
            if (user != null) {
                model.addAttribute("username", user.getUserName());
            }
        }
        List<RepairBooking> repairBookings = repairBookingService.findAllRepairBookings();
        model.addAttribute("repairBookings", repairBookings);
        return "repairs/admin";
    }

    @GetMapping("/add")
    public String showAddRepairBookingForm(Model model) {
        model.addAttribute("repairBooking", new RepairBooking());
        return "redirect:/repairs/admin";
    }

    @PostMapping("/add")
    public String addRepairBooking(@ModelAttribute RepairBooking repairBooking, Principal principal) {
        repairBooking.setAuthor(userService.findUserByUserName(principal.getName()).orElseThrow());
        repairBookingService.saveRepairBooking(repairBooking);
        return "redirect:/repairs/admin";
    }

    @GetMapping("/edit/{id}")
    public String showEditRepairBookingForm(@PathVariable Long id, Model model) {
        RepairBooking repairBooking = repairBookingService.findRepairBookingById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid repair booking Id: " + id));
        model.addAttribute("repairBooking", repairBooking);
        return "redirect:/repairs/admin";
    }

    @PostMapping("/edit")
    public String editRepairBooking(@ModelAttribute RepairBooking repairBooking, Principal principal) {
        repairBooking.setAuthor(userService.findUserByUserName(principal.getName()).orElseThrow());
        repairBookingService.saveRepairBooking(repairBooking);
        return "redirect:/repairs/admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteRepairBooking(@PathVariable Long id) {
        repairBookingService.deleteRepairBooking(id);
        return "redirect:/repairs/admin";
    }

    @GetMapping("/")
    public String showRepairHome(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findUserByUserName(username).orElse(null);
            if (user != null && user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.MODERATOR)) {
                return "redirect:/repairs/admin";
            }
            model.addAttribute("username", user.getUserName());
        }
        List<RepairBooking> repairBookings = repairBookingService.findAllRepairBookings();
        model.addAttribute("repairBookings", repairBookings);
        return "repairs/repair_home";
    }

    @GetMapping("/admin/filter")
    public String searchRepairBookings(@RequestParam(required = false) String title,
                                       @RequestParam(required = false) String publicationDate,
                                       @RequestParam(required = false) String author,
                                       @RequestParam(required = false) String content,
                                       @RequestParam(required = false) String mechanicName,
                                       Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findUserByUserName(username).orElse(null);
            if (user != null) {
                model.addAttribute("username", user.getUserName());
            }
        }

        List<RepairBooking> repairBookings = repairBookingService.searchRepairBookings(title, publicationDate, author, content, mechanicName);
        model.addAttribute("repairBookings", repairBookings);
        return "repairs/admin";
    }

    @GetMapping("/filter")
    public String searchRepairBookingsUser(@RequestParam(required = false) String title,
                                           @RequestParam(required = false) String publicationDate,
                                           @RequestParam(required = false) String author,
                                           @RequestParam(required = false) String content,
                                           @RequestParam(required = false) String mechanicName,
                                           Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findUserByUserName(username).orElse(null);
            if (user != null) {
                model.addAttribute("username", user.getUserName());
            }
        }

        List<RepairBooking> repairBookings = repairBookingService.searchRepairBookings(title, publicationDate, author, content, mechanicName);
        model.addAttribute("repairBookings", repairBookings);
        return "/repairs/repair_home";
    }
}