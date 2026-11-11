package com.marcusantosproject.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Category implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long categoryId;

    @Setter
    private String name;

    @Setter
    private String description;

    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();;

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
