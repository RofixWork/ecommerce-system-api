package com.rofix.ecommerce_system.entity;

import com.rofix.ecommerce_system.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "reviews", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "product_id"})
})
@Data
@NoArgsConstructor
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Check(constraints = "rating >= 1 AND rating <= 5")
    private Integer rating;

    @Lob
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Review(Integer rating, String comment, Product product, User user) {
        this.rating = rating;
        this.comment = comment;
        this.product = product;
        this.user = user;
    }

    public Review(Integer rating, String comment, Product product) {
        this.rating = rating;
        this.comment = comment;
        this.product = product;
    }
}
