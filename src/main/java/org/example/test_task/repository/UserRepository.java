package org.example.test_task.repository;

import jakarta.transaction.Transactional;
import org.example.test_task.entity.User;
import org.example.test_task.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    //получить без подписок и сразу маппить в UserDTO
    @Query("SELECT new org.example.test_task.dto.UserDTO(u.id, u.name) FROM org.example.test_task.entity.User u WHERE u.id = :id")
    Optional<UserDTO> findDtoById(@Param("id") Long id);

    //получить пользователя полностью
    Optional<User> getUserById(Long id);

    //удалить связи между юзером и подписками
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM user_subscription WHERE user_id = :userId;", nativeQuery = true)
    void deleteAllSubscriptionsByUserId(@Param("userId") Long userId);
}
