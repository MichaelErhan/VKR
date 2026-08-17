package com.example.autoservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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
    private String content;

    @Getter
    private String mechanicName;

    @Getter
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    // Конструкторы
    public RepairBooking() {}

    public Long getId() {
        return id;
    }
}