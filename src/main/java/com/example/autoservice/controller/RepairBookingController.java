package com.example.autoservice.controller;

import com.example.autoservice.model.RepairBooking;
import com.example.autoservice.model.ServiceItem;
import com.example.autoservice.model.User;
import com.example.autoservice.service.EmployeeService;
import com.example.autoservice.service.RepairBookingService;
import com.example.autoservice.service.ServiceItemService;
import com.example.autoservice.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/repairs")
public class RepairBookingController {

    private final RepairBookingService repairBookingService;
    private final UserService userService;
    private final EmployeeService employeeService;
    private final ServiceItemService serviceItemService;

    public RepairBookingController(RepairBookingService repairBookingService,
                                   UserService userService,
                                   EmployeeService employeeService,
                                   ServiceItemService serviceItemService) {
        this.repairBookingService = repairBookingService;
        this.userService = userService;
        this.employeeService = employeeService;
        this.serviceItemService = serviceItemService;
    }

    // Общие данные для страницы заказ-нарядов: имя пользователя и справочники для форм
    private void fillCommonModel(Model model, List<RepairBooking> repairBookings) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            User user = userService.findUserByUserName(authentication.getName()).orElse(null);
            if (user != null) {
                model.addAttribute("username", user.getUserName());
            }
        }
        model.addAttribute("repairBookings", repairBookings);
        model.addAttribute("employees", employeeService.findAllEmployees());
        model.addAttribute("services", serviceItemService.findAllServiceItems());
    }

    // Привязка выбранного механика, услуг и стоимости к заказ-наряду
    private void applySelection(RepairBooking repairBooking, Long mechanicId, List<Long> serviceIds) {
        repairBooking.setMechanic(mechanicId == null ? null
                : employeeService.findEmployeeById(mechanicId).orElse(null));

        List<ServiceItem> selected = (serviceIds == null || serviceIds.isEmpty())
                ? new ArrayList<>()
                : new ArrayList<>(serviceItemService.findServiceItemsByIds(serviceIds));
        repairBooking.setServices(selected);

        // Если стоимость не введена, берём сумму цен выбранных услуг
        if (repairBooking.getCost() == null) {
            BigDecimal total = selected.stream()
                    .map(ServiceItem::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            repairBooking.setCost(total);
        }
    }

    @GetMapping("/")
    public String showRepairHome() {
        return "redirect:/repairs/admin";
    }

    @GetMapping("/admin")
    public String showAdminPanel(Model model) {
        fillCommonModel(model, repairBookingService.findAllRepairBookings());
        return "repairs/admin";
    }

    @PostMapping("/add")
    public String addRepairBooking(@ModelAttribute RepairBooking repairBooking,
                                   @RequestParam(required = false) Long mechanicId,
                                   @RequestParam(required = false) List<Long> serviceIds,
                                   Principal principal) {
        applySelection(repairBooking, mechanicId, serviceIds);
        repairBooking.setAuthor(userService.findUserByUserName(principal.getName()).orElseThrow());
        repairBookingService.saveRepairBooking(repairBooking);
        return "redirect:/repairs/admin";
    }

    @PostMapping("/edit")
    public String editRepairBooking(@ModelAttribute RepairBooking repairBooking,
                                    @RequestParam(required = false) Long mechanicId,
                                    @RequestParam(required = false) List<Long> serviceIds,
                                    Principal principal) {
        applySelection(repairBooking, mechanicId, serviceIds);
        repairBooking.setAuthor(userService.findUserByUserName(principal.getName()).orElseThrow());
        repairBookingService.saveRepairBooking(repairBooking);
        return "redirect:/repairs/admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteRepairBooking(@PathVariable Long id) {
        repairBookingService.deleteRepairBooking(id);
        return "redirect:/repairs/admin";
    }

    @GetMapping("/admin/filter")
    public String searchRepairBookings(@RequestParam(required = false) String title,
                                       @RequestParam(required = false) String publicationDate,
                                       @RequestParam(required = false) String author,
                                       @RequestParam(required = false) String serviceName,
                                       @RequestParam(required = false) String mechanicName,
                                       Model model) {
        fillCommonModel(model, repairBookingService.searchRepairBookings(
                title, publicationDate, author, serviceName, mechanicName));
        return "repairs/admin";
    }
}