package com.example.autoservice.config;

import com.example.autoservice.repository.UserRepository;
import com.example.autoservice.service.UserService;
import com.example.autoservice.util.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserService userService;

    @Value("${app.admin.username:admin}")
    private String adminUsername;

    @Value("${app.admin.password:admin}")
    private String adminPassword;

    public AdminInitializer(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        // Администратор создаётся только если в базе нет ни одного пользователя
        if (userRepository.count() == 0) {
            userService.createUser(adminUsername, adminPassword, Role.ADMIN);
            System.out.println("Создан администратор по умолчанию: " + adminUsername
                    + ". Смените пароль после первого входа.");
        }
    }
}