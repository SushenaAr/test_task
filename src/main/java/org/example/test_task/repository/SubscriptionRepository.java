package org.example.test_task.repository;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.example.test_task.entity.Subscription;
import org.example.test_task.dto.SubscriptionDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    //удалить связь между юзером и подпиской
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM user_subscription WHERE user_id = :userId AND subscription_id = :subId; ", nativeQuery = true)
    void deleteUserSubscriptionLink(@Param("userId") Long userId, @Param("subId") Long subId);

    //получить подписки юзера и сразу смаппить в SubscriptionDTO
    @Query("""
        SELECT new org.example.test_task.dto.SubscriptionDTO(s.id, s.title, s.numberOfUser)
        FROM org.example.test_task.entity.User u
        JOIN u.subscriptions s
        WHERE u.id = :userId
    """)
    List<SubscriptionDTO> findByIdWithSubscriptions(@Param("userId") Long userId);

    //добавить связь между юзером и подпиской
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO user_subscription (user_id, subscription_id) " +
            "VALUES (:userId, :subId);", nativeQuery = true)
    void addSubscription(@Param("userId") Long userId, @Param("subId") Long subId);

    //инкрементировать количество подписок
    @Transactional
    @Modifying
    @Query(value = "UPDATE subscriptions " +
            "SET number_of_user=number_of_user+1 " +
            "WHERE id = :subId;", nativeQuery = true)
    void incrementNumberOfUser(@Param("subId") Long subId);

    //декрементировать количество подписок
    @Transactional
    @Modifying
    @Query(value = "UPDATE subscriptions " +
            "SET number_of_user=number_of_user-1 " +
            "WHERE id = :subId;", nativeQuery = true)
    void decrementNumberOfUser(@Param("subId") Long subId);

    //получить по title подписку
    Optional<Subscription> getSubscriptionByTitle(String title);

    //3 топовых подписки в порядке убывания
    List<Subscription> findTop3ByOrderByNumberOfUserDesc();
}
