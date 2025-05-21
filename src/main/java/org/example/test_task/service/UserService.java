package org.example.test_task.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.test_task.entity.User;
import org.example.test_task.repository.SubscriptionRepository;
import org.example.test_task.repository.UserRepository;
import org.example.test_task.exception.NotFountException;
import org.example.test_task.dto.UserDTO;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    UserRepository userRepository;
    SubscriptionRepository subscriptionRepository;
    public UserDTO getUser(Long userId){
        return userRepository
                .findDtoById(userId)
                .orElseThrow(() -> new NotFountException("Пользователь не найден"));
    }

    @Transactional
    public UserDTO updateUser(Long userId, User userFromNet){
        User userFromDB = userRepository.getUserById(userId)
                .orElseThrow(() -> new NotFountException("Пользователь не найден"));
        userFromDB.setName(userFromNet.getName());
        return UserDTO.builder()
                .id(userFromDB.getId())
                .name(userFromDB.getName())
                .build();
    }

    public UserDTO createUser(User User) {
        User userFromDB = userRepository.save(User);
        return UserDTO.builder()
                .id(userFromDB.getId())
                .name(userFromDB.getName())
                .build();
    }

    @Transactional
    public void deleteUser(Long userId) {
        subscriptionRepository.decrementNumberOfUser(userId);
        userRepository.deleteAllSubscriptionsByUserId(userId);
        userRepository.deleteById(userId);
    }
}
