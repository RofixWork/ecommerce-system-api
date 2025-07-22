package com.rofix.ecommerce_system.repository;

import com.rofix.ecommerce_system.entity.Role;
import com.rofix.ecommerce_system.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query("SELECT r FROM Role as r WHERE LOWER(r.name) = LOWER(:name)")
    Optional<Role> findByRoleName(@Param("name") String name);
}
