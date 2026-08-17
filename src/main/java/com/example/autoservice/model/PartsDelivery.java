package com.example.autoservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Entity
@Table(name = "parts_deliveries")
public class PartsDelivery {
    private Long id;

    @Getter
    private String partName;

    @Getter
    private String partDescription;

    @Getter
    private String supplierCity;

    @Getter
    private LocalDate shipmentDate;

    @Getter
    private String deliveryCity;

    @Getter
    private LocalDate deliveryDate;

    // Конструкторы
    public PartsDelivery() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

}