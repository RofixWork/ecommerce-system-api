package com.rofix.ecommerce_system.repository;

import com.rofix.ecommerce_system.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

    Optional<Product> findBySlugIgnoreCase(String slug);

    Page<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description, Pageable pageable);

    Page<Product> findByPriceGreaterThan(BigDecimal priceIsGreaterThan, Pageable pageable);

    Page<Product> findByPriceLessThan(BigDecimal priceIsLessThan, Pageable pageable);

    Page<Product> findByPriceEquals(BigDecimal price, Pageable pageable);

    Page<Product> findByPriceLessThanEqual(BigDecimal priceBigDecimal, Pageable pageable);

    Page<Product> findByPriceGreaterThanEqual(BigDecimal priceIsGreaterThan, Pageable pageable);

    Page<Product> findByPriceNot(BigDecimal price, Pageable pageable);

    //price and name
    @Query(
            "SELECT pr FROM Product as pr where " +
                    "(LOWER(pr.name) LIKE CONCAT('%', LOWER(:name) ,'%') OR LOWER(pr.description) LIKE CONCAT('%', LOWER(:description) ,'%'))"
                    + " AND pr.price > :price"
    )
    Page<Product> findByNameOrDescriptionAndPriceGreaterThan(@Param("name") String name, @Param("description") String description, @Param("price") BigDecimal price, Pageable pageable);

    @Query(
            "SELECT pr FROM Product as pr where " +
                    "(LOWER(pr.name) LIKE CONCAT('%', LOWER(:name) ,'%') OR LOWER(pr.description) LIKE CONCAT('%', LOWER(:description) ,'%'))"
                    + " AND pr.price < :price"
    )
    Page<Product> findByNameOrDescriptionAndPriceLessThan(String name, String description, BigDecimal price, Pageable pageable);

    @Query(
            "SELECT pr FROM Product as pr where " +
                    "(LOWER(pr.name) LIKE CONCAT('%', LOWER(:name) ,'%') OR LOWER(pr.description) LIKE CONCAT('%', LOWER(:description) ,'%'))"
                    + " AND pr.price = :price"
    )
    Page<Product> findByNameOrDescriptionAndPriceEquals(String name, String description, BigDecimal price, Pageable pageable);

    @Query(
            "SELECT pr FROM Product as pr where " +
                    "(LOWER(pr.name) LIKE CONCAT('%', LOWER(:name) ,'%') OR LOWER(pr.description) LIKE CONCAT('%', LOWER(:description) ,'%'))"
                    + " AND pr.price >= :price"
    )
    Page<Product> findByNameOrDescriptionAndPriceGreaterThanOrEqual(@Param("name") String name, @Param("description") String description, @Param("price") BigDecimal price, Pageable pageable);

    @Query(
            "SELECT pr FROM Product as pr where " +
                    "(LOWER(pr.name) LIKE CONCAT('%', LOWER(:name) ,'%') OR LOWER(pr.description) LIKE CONCAT('%', LOWER(:description) ,'%'))"
                    + " AND pr.price <= :price"
    )
    Page<Product> findByNameOrDescriptionAndPriceLessThanOrEqual(@Param("name") String name, @Param("description") String description, @Param("price") BigDecimal price, Pageable pageable);

    @Query(
            "SELECT pr FROM Product as pr where " +
                    "(LOWER(pr.name) LIKE CONCAT('%', LOWER(:name) ,'%') OR LOWER(pr.description) LIKE CONCAT('%', LOWER(:description) ,'%'))"
                    + " AND pr.price != :price"
    )
    Page<Product> findByNameOrDescriptionAndPriceNotEqual(@Param("name") String name, @Param("description") String description, @Param("price") BigDecimal price, Pageable pageable);
}
