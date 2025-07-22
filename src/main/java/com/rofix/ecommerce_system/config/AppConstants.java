package com.rofix.ecommerce_system.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class AppConstants {
    public final static String PAGE_NUMBER = "1";
    public final static String PAGE_SIZE = "5";
    public final static String SORT_BY = "id";
    public final static String SORT_ORDER = "asc";
    public final static List<String> ALLOWED_PRODUCT_ORDERING_FIELDS = List.of("id", "name", "description", "stock", "price");
    public static final Set<String> ALLOWED_FILE_MIME_TYPES = Set.of("image/jpeg", "image/png", "image/webp");
    public final static String CLOUDINARY_PRODUCTS_FOLDER = "products_image";
}