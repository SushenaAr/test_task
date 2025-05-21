package org.example.test_task.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.test_task.entity.User;
import org.example.test_task.service.UserService;
import org.example.test_task.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Slf4j
public class UserController {
    UserService userService;

    //создание пользователя
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid User user){
        log.info("Запрос на создание пользователя {}", user.getName());
        return ResponseEntity.ok(userService.createUser(user));
    }

    //получить пользователя
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long userId){
        log.info("Запрос на получение пользователя с id: {}", userId);
        return ResponseEntity.ok(userService.getUser(userId));
    }

    //обновить пользователя
    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId, @Valid @RequestBody User user){
        log.info("Запрос на обновление пользователя с id: {}", userId);
        return ResponseEntity.ok(userService.updateUser(userId, user));
    }

    //удалить пользователя и все связи с подписками
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        log.info("Запрос на удаление пользователя с id: {}", userId);
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
