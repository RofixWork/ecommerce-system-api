package com.rofix.ecommerce_system.entity;

import com.rofix.ecommerce_system.entity.base.BaseEntity;
import com.rofix.ecommerce_system.enums.RoleName;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Table(name = "roles")
@Entity
@NoArgsConstructor
public class Role extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 12)
    @Enumerated(EnumType.STRING)
    private RoleName name;

    public Role(RoleName name) {
        this.name = name;
    }
}
