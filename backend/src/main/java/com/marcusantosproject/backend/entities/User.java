package com.marcusantosproject.backend.entities;

import com.marcusantosproject.backend.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Getter @Setter
    private Long userId;

    @Column(nullable = false, length = 100)
    @Getter @Setter
    private String name;

    @Getter @Setter
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Getter @Setter
    private String passwordHash;

    @Getter @Setter
    @Enumerated(EnumType.STRING)
    private Role role;

    @Getter @Setter
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    public User(String name, String email, String passwordHash, Role role) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }
}
