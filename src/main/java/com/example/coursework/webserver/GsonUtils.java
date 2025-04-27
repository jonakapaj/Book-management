package com.example.coursework.webserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Utility class for creating properly configured Gson instances
 * that can handle Java 8 date/time types correctly
 */
public class GsonUtils {
    
    private GsonUtils() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Creates a standardized Gson instance with proper type adapters for date/time classes
     * to ensure consistent serialization across all handlers
     * 
     * @return A configured Gson instance
     */
    public static Gson createGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .disableHtmlEscaping()
                .create();
    }
}