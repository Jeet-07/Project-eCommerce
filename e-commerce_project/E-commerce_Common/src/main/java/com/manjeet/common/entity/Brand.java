package com.manjeet.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "brands")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(nullable = false, length = 45, unique = true)
    private String name;

    @Column(nullable = false, length = 128)
    private String logo;

    @ManyToMany
    @JoinTable(
            name = "brands_categories",
            joinColumns = @JoinColumn(name = "brand_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    public Brand() { }

    public Brand(String name) {
        this.name = name;
        this.logo = "brand-logo.png";
    }

    public Brand(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Transient
    public String getLogoPath() {
        if (this.id == null || this.logo == null) return "/images/image-default.png";

        return "/brand-logos/" + this.id + "/" + this.logo;
    }
}
