package com.example.autoservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Entity
@Table(name = "clients")
public class Clients {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    private String fullName;

    @Getter
    private String carBrandAndPlate;

    @Getter
    private String phoneNumber;

    // Конструкторы
    public Clients() {}

    public Long getId() {
        return id;
    }
}