package com.example.autoservice.model;

import com.example.autoservice.util.Role;
import com.example.autoservice.util.RoleConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Entity
@Table(name = "users")
public class User {
    private Long id;

    @Getter
    @Column(unique = true, nullable = false)
    private String userName;

    @Getter
    @Column(nullable = false)
    private String password;

    @Getter
    @Column(nullable = false)
    @Convert(converter = RoleConverter.class)
    private Role role;

    // Конструкторы
    public User() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

}