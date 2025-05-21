package org.example.test_task.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SubscriptionDTO {
    Long id;
    String title;
    Integer numberOfUser;
}
