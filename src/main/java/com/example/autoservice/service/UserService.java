package com.example.autoservice.service;

import com.example.autoservice.model.User;
import com.example.autoservice.repository.UserRepository;
import com.example.autoservice.util.Role;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Создание учётной записи администратором (пароль хранится в виде хеша BCrypt)
    public void createUser(String userName, String rawPassword, Role role) {
        if (userRepository.findByUserName(userName).isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким именем уже существует");
        }
        User user = new User();
        user.setUserName(userName);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role == null ? Role.MANAGER : role);
        userRepository.save(user);
    }

    // Поиск пользователя по ID
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    // Поиск пользователя по имени
    public Optional<User> findUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    // Удаление пользователя по ID
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
    public void updateUserRole(Long userId, String newRole) {
        // Находим пользователя по его идентификатору
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Обновляем роль пользователя
            user.setRole(Role.valueOf(newRole));

            // Сохраняем изменения
            userRepository.save(user);
        } else {
            throw new RuntimeException("Пользователь с id " + userId + " не найден");
        }
    }
}
