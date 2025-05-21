package org.example.test_task.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.example.test_task.tools.validators.xssSafe.XssSafe;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "subscriptions")
@Data
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false, unique = true)
    @XssSafe
    private String title;

    @ManyToMany(mappedBy = "subscriptions", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    private Integer numberOfUser = 0;
}
