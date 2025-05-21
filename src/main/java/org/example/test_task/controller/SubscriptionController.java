package org.example.test_task.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.test_task.entity.Subscription;
import org.example.test_task.service.SubscriptionService;
import org.example.test_task.dto.SubscriptionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class SubscriptionController {
    SubscriptionService subscriptionService;

    //подписать юзера
    @PostMapping("/users/{userId}/subscriptions")
    public ResponseEntity<String> addSubscription(@PathVariable Long userId, @RequestBody Subscription subscription) {
        log.info("Запрос на подписание пользователя с id: {} подпиской: {}", userId, subscription.getTitle());
        subscriptionService.addSubscription(userId, subscription);
        return ResponseEntity.ok("Success");
    }

    //получить подписок юзера
    @GetMapping("/users/{userId}/subscriptions")
    public ResponseEntity<List<SubscriptionDTO>> getSubscriptions(@PathVariable Long userId) {
        log.info("Запрос на получение подписок пользователя с id: {}", userId);
        return ResponseEntity.ok(subscriptionService.getSubscriptions(userId));
    }

    //удалить подписку юзера
    @DeleteMapping("/users/{userId}/subscriptions/{subId}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long userId, @PathVariable Long subId) {
        log.info("Запрос на удаление подписки пользователя с id: {} и id подписки: {}", userId, subId);
        subscriptionService.deleteSubscription(userId, subId);
        return ResponseEntity.noContent().build();
    }

    //топ подписок
    @GetMapping("/subscriptions/top")
    public ResponseEntity<List<SubscriptionDTO>> topSubscriptions() {
        log.info("Запрос на получение топовых подписок");
        return ResponseEntity.ok(subscriptionService.getTopSubscriptions());
    }
}
