package com.marcusantosproject.backend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Getter
    private Long userId;

    @Column(nullable = false, length = 100)
    @Getter @Setter
    private String name;

    @Getter @Setter
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Setter
    private String passwordHash;

    public User(String name, String email, String passwordHash) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
    }
}
