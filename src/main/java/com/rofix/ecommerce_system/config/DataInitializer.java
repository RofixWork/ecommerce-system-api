package com.rofix.ecommerce_system.config;

import com.rofix.ecommerce_system.entity.Category;
import com.rofix.ecommerce_system.entity.Role;
import com.rofix.ecommerce_system.enums.RoleName;
import com.rofix.ecommerce_system.repository.CategoryRepository;
import com.rofix.ecommerce_system.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

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


    @Override
    public void run(String... args) throws Exception {
        List<Category> categories = new ArrayList<>();
        for (String category : ecommerceCategories) {
            Category newCategory = new Category(category, "bla bla bla bla bla bla bla");
            categories.add(newCategory);
        }
        categoryRepository.saveAll(categories);
        log.info("Categories Saved in the DB...");

//        --------------------------- roles ---------------------------
        Set<Role> roles = Set.of(
                new Role(RoleName.ADMIN),
                new Role(RoleName.CUSTOMER),
                new Role(RoleName.SELLER)
        );

        roleRepository.saveAll(roles);
        log.info("Roles Saved in the DB...");

    }
}
