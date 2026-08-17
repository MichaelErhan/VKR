package com.example.autoservice.controller;

import com.example.autoservice.model.PartsDelivery;
import com.example.autoservice.model.User;
import com.example.autoservice.service.PartsDeliveryService;
import com.example.autoservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
//@RequestMapping("/parts-delivery/admin")
public class PartsDeliveryController {

    private final PartsDeliveryService partsDeliveryService;
    private final UserService userService;

    @Autowired
    public PartsDeliveryController(PartsDeliveryService partsDeliveryService, UserService userService) {
        this.partsDeliveryService = partsDeliveryService;
        this.userService = userService;
    }

    // Показываем форму для добавления доставки запчастей
    @GetMapping("/parts-delivery/admin/add")
    public String showAddPartsDeliveryForm(Model model) {
        model.addAttribute("partsDelivery", new PartsDelivery());
        return "redirect:/parts-delivery/admin";
    }

    // Добавляем новую доставку запчастей в базу данных
    @PostMapping("/parts-delivery/admin/add")
    public String addPartsDelivery(@ModelAttribute PartsDelivery partsDelivery, RedirectAttributes redirectAttributes) {
        partsDeliveryService.savePartsDelivery(partsDelivery);
        redirectAttributes.addFlashAttribute("message", "Представление успешно добавлена!");
        return "redirect:/parts-delivery/admin";
    }

    // Показываем форму для редактирования доставки запчастей
    @GetMapping("/parts-delivery/admin/edit/{id}")
    public String showEditPartsDeliveryForm(@PathVariable Long id, Model model) {
        PartsDelivery partsDelivery = partsDeliveryService.findPartsDeliveryById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid parts delivery Id: " + id));
        model.addAttribute("partsDelivery", partsDelivery);
        return "redirect:/parts-delivery/admin";
    }

    // Редактируем доставку запчастей
    @PostMapping("/parts-delivery/admin/edit")
    public String editPartsDelivery(@ModelAttribute PartsDelivery partsDelivery) {
        partsDeliveryService.savePartsDelivery(partsDelivery);
        return "redirect:/parts-delivery/admin";
    }

    // Удаляем доставку запчастей по ID
    @PostMapping("/parts-delivery/admin/delete/{id}")
    public String deletePartsDelivery(@PathVariable Long id) {
        partsDeliveryService.deletePartsDelivery(id);
        return "redirect:/parts-delivery/admin";
    }

    // Отображение гистограммы
    @GetMapping("/parts-delivery/admin/histogram")
    public String showHistogram(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findUserByUserName(username).orElse(null);
            if (user != null) {
                model.addAttribute("username", user.getUserName());
            }
        }
        Map<LocalDate, Long> histogramData = partsDeliveryService.getPartsDeliveryCountByDay();
        model.addAttribute("histogramData", histogramData);
        return "parts-delivery/histogram";
    }

    // Поиск доставки запчастей по информации
    @GetMapping("/parts-delivery/admin/filter")
    public String searchPartsDeliveries(@RequestParam String searchInput,
                                        @RequestParam(required = false) String partName,
                                        @RequestParam(required = false) String partDescription,
                                        @RequestParam(required = false) String supplierCity,
                                        @RequestParam(required = false) String shipmentDate,
                                        @RequestParam(required = false) String deliveryCity,
                                        @RequestParam(required = false) String deliveryDate,
                                        Model model) {
        String[] params = searchInput.split("\\s*,\\s*");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findUserByUserName(username).orElse(null);
            if (user != null) {
                model.addAttribute("username", user.getUserName());
            }
        }

        if (params.length == 1) {
            partName = params[0];
        } else if (params.length == 2) {
            partName = params[0];
            partDescription = params[1];
        } else if (params.length == 3) {
            partName = params[0];
            partDescription = params[1];
            supplierCity = params[2];
        } else if (params.length == 4) {
            partName = params[0];
            partDescription = params[1];
            supplierCity = params[2];
            shipmentDate = params[3];
        } else if (params.length == 5) {
            partName = params[0];
            partDescription = params[1];
            supplierCity = params[2];
            shipmentDate = params[3];
            deliveryCity = params[4];
        } else if (params.length == 6) {
            partName = params[0];
            partDescription = params[1];
            supplierCity = params[2];
            shipmentDate = params[3];
            deliveryCity = params[4];
            deliveryDate = params[5];
        }

        List<PartsDelivery> partsDeliveries = partsDeliveryService.searchPartsDeliveries(partName, partDescription, supplierCity, shipmentDate, deliveryCity, deliveryDate);
        model.addAttribute("partsDeliveries", partsDeliveries);
        return "parts-delivery/admin";
    }

    // Сортировка по дате доставки запчастей
    @GetMapping("/parts-delivery/admin/sort")
    public String sortPartsDeliveriesByDate(@RequestParam(defaultValue = "asc") String order, Model model) {
        List<PartsDelivery> partsDeliveries;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findUserByUserName(username).orElse(null);
            if (user != null) {
                model.addAttribute("username", user.getUserName());
            }
        }
        if ("desc".equalsIgnoreCase(order)) {
            partsDeliveries = partsDeliveryService.findAllPartsDeliveriesSortedByDateDesc();
        } else {
            partsDeliveries = partsDeliveryService.findAllPartsDeliveriesSortedByDate();
        }
        model.addAttribute("partsDeliveries", partsDeliveries);
        model.addAttribute("order", order);
        return "parts-delivery/admin";
    }


// для юзера фильтровка и поиск

    @RequestMapping("/parts-delivery/filter")
    public String filterPartsDeliveries(@RequestParam String searchInput,
                                        @RequestParam(required = false) String partName,
                                        @RequestParam(required = false) String partDescription,
                                        @RequestParam(required = false) String supplierCity,
                                        @RequestParam(required = false) String shipmentDate,
                                        @RequestParam(required = false) String deliveryCity,
                                        @RequestParam(required = false) String deliveryDate,
                                        Model model) {
        String[] params = searchInput.split("\\s*,\\s*");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findUserByUserName(username).orElse(null);
            if (user != null) {
                model.addAttribute("username", user.getUserName());
            }
        }

        if (params.length == 1) {
            partName = params[0];
        } else if (params.length == 2) {
            partName = params[0];
            partDescription = params[1];
        } else if (params.length == 3) {
            partName = params[0];
            partDescription = params[1];
            supplierCity = params[2];
        } else if (params.length == 4) {
            partName = params[0];
            partDescription = params[1];
            supplierCity = params[2];
            shipmentDate = params[3];
        } else if (params.length == 5) {
            partName = params[0];
            partDescription = params[1];
            supplierCity = params[2];
            shipmentDate = params[3];
            deliveryCity = params[4];
        } else if (params.length == 6) {
            partName = params[0];
            partDescription = params[1];
            supplierCity = params[2];
            shipmentDate = params[3];
            deliveryCity = params[4];
            deliveryDate = params[5];
        }

        List<PartsDelivery> partsDeliveries = partsDeliveryService.searchPartsDeliveries(partName, partDescription, supplierCity, shipmentDate, deliveryCity, deliveryDate);
        model.addAttribute("partsDeliveries", partsDeliveries);
        return "parts-delivery/list";
    }

    // Сортировка по дате доставки запчастей
    @GetMapping("/parts-delivery/sort")
    public String sortPartsDeliveriesByDateUser(@RequestParam(defaultValue = "asc") String order, Model model) {
        List<PartsDelivery> partsDeliveries;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findUserByUserName(username).orElse(null);
            if (user != null) {
                model.addAttribute("username", user.getUserName());
            }
        }
        if ("desc".equalsIgnoreCase(order)) {
            partsDeliveries = partsDeliveryService.findAllPartsDeliveriesSortedByDateDesc();
        } else {
            partsDeliveries = partsDeliveryService.findAllPartsDeliveriesSortedByDate();
        }
        model.addAttribute("partsDeliveries", partsDeliveries);
        model.addAttribute("order", order);
        return "parts-delivery/list";
    }
}