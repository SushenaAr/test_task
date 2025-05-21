package org.example.test_task.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.test_task.entity.Subscription;
import org.example.test_task.repository.SubscriptionRepository;
import org.example.test_task.exception.NotFountException;
import org.example.test_task.dto.SubscriptionDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class SubscriptionService {
    SubscriptionRepository subscriptionRepository;

    //подписать юзера
    @Transactional
    public void addSubscription(Long userId, Subscription subscription) {
        Long idSubscriptionFromDB = subscriptionRepository.getSubscriptionByTitle(
                        subscription.getTitle())
                .orElseThrow(() -> new NotFountException("Подписка не найдена"))
                .getId();
        subscriptionRepository.incrementNumberOfUser(idSubscriptionFromDB);
        subscriptionRepository.addSubscription(
                userId,
                idSubscriptionFromDB
        );
    }

    //получить подписки юзера
    public List<SubscriptionDTO> getSubscriptions(Long userId) {
        List<SubscriptionDTO> subscriptions = subscriptionRepository.findByIdWithSubscriptions(userId);
        if (subscriptions.isEmpty()) {
            throw new NotFountException("Подписок не найдено");
        }
        return subscriptions;
    }

    //удалить подписку юзера
    @Transactional
    public void deleteSubscription(Long userId, Long subId) {
        subscriptionRepository.decrementNumberOfUser(subId);
        subscriptionRepository.deleteUserSubscriptionLink(userId, subId);
    }

    //получить топ подписок
    public List<SubscriptionDTO> getTopSubscriptions() {
        List<SubscriptionDTO> subscriptionDTOS = subscriptionRepository.findTop3ByOrderByNumberOfUserDesc().stream()
                .map(subscription -> SubscriptionDTO.builder()
                        .id(subscription.getId())
                        .title(subscription.getTitle())
                        .numberOfUser(subscription.getNumberOfUser())
                        .build())
                .toList();
        if (subscriptionDTOS.isEmpty()) {
            throw new NotFountException("Подписок не найдено");
        }
        return subscriptionDTOS;
    }
}
