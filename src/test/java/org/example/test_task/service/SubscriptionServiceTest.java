package org.example.test_task.service;

import org.example.test_task.dto.SubscriptionDTO;
import org.example.test_task.entity.Subscription;
import org.example.test_task.exception.NotFountException;
import org.example.test_task.repository.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SubscriptionService subscriptionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ----------- addSubscription -----------

    @Test
    void addSubscription_success() {
        Long userId = 1L;
        Long subId = 10L;
        Subscription subscription = new Subscription();
        subscription.setTitle("Premium");

        Subscription subscriptionFromDb = new Subscription();
        subscriptionFromDb.setId(subId);
        subscriptionFromDb.setTitle("Premium");

        when(subscriptionRepository.getSubscriptionByTitle("Premium"))
                .thenReturn(Optional.of(subscriptionFromDb));

        doNothing().when(subscriptionRepository).incrementNumberOfUser(subId);
        doNothing().when(subscriptionRepository).addSubscription(userId, subId);

        subscriptionService.addSubscription(userId, subscription);

        verify(subscriptionRepository).getSubscriptionByTitle("Premium");
        verify(subscriptionRepository).incrementNumberOfUser(subId);
        verify(subscriptionRepository).addSubscription(userId, subId);
    }

    @Test
    void addSubscription_notFound() {
        Long userId = 1L;
        Subscription subscription = new Subscription();
        subscription.setTitle("Unknown");

        when(subscriptionRepository.getSubscriptionByTitle("Unknown"))
                .thenReturn(Optional.empty());

        assertThrows(NotFountException.class, () -> subscriptionService.addSubscription(userId, subscription));
        verify(subscriptionRepository).getSubscriptionByTitle("Unknown");
        verifyNoMoreInteractions(subscriptionRepository);
    }

    // ----------- getSubscriptions -----------

    @Test
    void getSubscriptions_success() {
        Long userId = 1L;
        List<SubscriptionDTO> subs = List.of(
                SubscriptionDTO.builder().id(1L).title("Premium").numberOfUser(100).build(),
                SubscriptionDTO.builder().id(2L).title("Basic").numberOfUser(50).build()
        );

        when(subscriptionRepository.findByIdWithSubscriptions(userId)).thenReturn(subs);

        List<SubscriptionDTO> result = subscriptionService.getSubscriptions(userId);

        assertEquals(2, result.size());
        verify(subscriptionRepository).findByIdWithSubscriptions(userId);
    }

    @Test
    void getSubscriptions_emptyList() {
        Long userId = 1L;
        when(subscriptionRepository.findByIdWithSubscriptions(userId)).thenReturn(List.of());

        assertThrows(NotFountException.class, () -> subscriptionService.getSubscriptions(userId));
        verify(subscriptionRepository).findByIdWithSubscriptions(userId);
    }

    // ----------- deleteSubscription -----------

    @Test
    void deleteSubscription_success() {
        Long userId = 1L;
        Long subId = 2L;

        doNothing().when(subscriptionRepository).decrementNumberOfUser(subId);
        doNothing().when(subscriptionRepository).deleteUserSubscriptionLink(userId, subId);

        subscriptionService.deleteSubscription(userId, subId);

        verify(subscriptionRepository).decrementNumberOfUser(subId);
        verify(subscriptionRepository).deleteUserSubscriptionLink(userId, subId);
    }

    // ----------- getTopSubscriptions -----------

    @Test
    void getTopSubscriptions_success() {
        Subscription s1 = new Subscription();
        s1.setId(1L);
        s1.setTitle("Premium");
        s1.setNumberOfUser(100);

        Subscription s2 = new Subscription();
        s2.setId(2L);
        s2.setTitle("Gold");
        s2.setNumberOfUser(80);

        Subscription s3 = new Subscription();
        s3.setId(3L);
        s3.setTitle("Silver");
        s3.setNumberOfUser(60);

        when(subscriptionRepository.findTop3ByOrderByNumberOfUserDesc()).thenReturn(List.of(s1, s2, s3));

        List<SubscriptionDTO> result = subscriptionService.getTopSubscriptions();

        assertEquals(3, result.size());
        assertEquals("Premium", result.get(0).getTitle());
        verify(subscriptionRepository).findTop3ByOrderByNumberOfUserDesc();
    }

    @Test
    void getTopSubscriptions_empty() {
        when(subscriptionRepository.findTop3ByOrderByNumberOfUserDesc()).thenReturn(List.of());

        assertThrows(NotFountException.class, () -> subscriptionService.getTopSubscriptions());
        verify(subscriptionRepository).findTop3ByOrderByNumberOfUserDesc();
    }
}