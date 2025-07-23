package com.rofix.ecommerce_system.config;

import com.rofix.ecommerce_system.entity.Category;
import com.rofix.ecommerce_system.entity.Role;
import com.rofix.ecommerce_system.entity.User;
import com.rofix.ecommerce_system.enums.RoleName;
import com.rofix.ecommerce_system.repository.CategoryRepository;
import com.rofix.ecommerce_system.repository.RoleRepository;
import com.rofix.ecommerce_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final List<String> ecommerceCategories = new ArrayList<>(Arrays.asList(
            "Electronics",
            "Fashion",
            "Home & Garden",
            "Health & Beauty",
            "Sports & Outdoors",
            "Toys & Games",
            "Automotive",
            "Books & Media",
            "Jewelry & Accessories",
            "Groceries",
            "Baby Products",
            "Pet Supplies",
            "Office Supplies",
            "Furniture",
            "Music & Instruments",
            "Art & Craft Supplies",
            "Mobile Phones & Accessories",
            "Computers & Software",
            "Shoes",
            "Watches"
    ));
    private final CategoryRepository categoryRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    @Override
    public void run(String... args) throws Exception {
        // --- Save Categories ---
        List<Category> categories = new ArrayList<>();
        for (String category : ecommerceCategories) {
            categories.add(new Category(category, "bla bla bla"));
        }
        categoryRepository.saveAll(categories);
        log.info("Categories Saved in the DB...");

        // --- Save Roles ---
        roleRepository.saveAll(Set.of(
                new Role(RoleName.ADMIN),
                new Role(RoleName.CUSTOMER),
                new Role(RoleName.SELLER)
        ));
        log.info("Roles Saved in the DB...");

        // --- Load the Admin Role from DB again (Managed) ---
//        Role adminRole = roleRepository.findByRoleName("admin")
//                .orElseThrow(() -> new RuntimeException("Role Not Found"));

//        // --- Create Admin User ---
//        User user = new User();
//        user.setUsername("admin");
//        user.setPhone("0678765654");
//        user.setEmail("admin@gmail.com");
//        user.setPassword(passwordEncoder.encode("admin"));
//        user.getRoles().add(adminRole); // Now managed
//        userRepository.save(user);
//        log.info("Admin User Saved in the DB...");
    }

}
