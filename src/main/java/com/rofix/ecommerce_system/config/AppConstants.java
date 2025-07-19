package com.rofix.ecommerce_system.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppConstants {
    public final static String PAGE_NUMBER = "1";
    public final static String PAGE_SIZE = "5";
    public final static String SORT_BY = "id";
    public final static String SORT_ORDER = "asc";
    public final static List<String> ALLOWED_PRODUCT_ORDERING_FIELDS = new ArrayList<>(Arrays.asList("id", "name", "description", "stock", "price"));
}