package com.example.autoservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Entity
@Table(name = "repair_bookings")
public class RepairBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    private String title;

    @Getter
    private LocalDate publicationDate;

    @Getter
    @Column(precision = 12, scale = 2)
    private BigDecimal cost;

    @Getter
    @ManyToOne
    @JoinColumn(name = "mechanic_id")
    private Employee mechanic;

    @Getter
    @ManyToMany
    @JoinTable(name = "repair_booking_services",
            joinColumns = @JoinColumn(name = "repair_booking_id"),
            inverseJoinColumns = @JoinColumn(name = "service_item_id"))
    private List<ServiceItem> services = new ArrayList<>();

    @Getter
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    public RepairBooking() {}

    public Long getId() {
        return id;
    }

    // Названия выбранных услуг через запятую (для таблицы)
    public String getServicesText() {
        return services.stream().map(ServiceItem::getName).collect(Collectors.joining(", "));
    }

    // Идентификаторы выбранных услуг через запятую (для формы редактирования)
    public String getServiceIdsText() {
        return services.stream().map(s -> String.valueOf(s.getId())).collect(Collectors.joining(","));
    }
}