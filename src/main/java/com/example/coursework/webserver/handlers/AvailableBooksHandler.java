package com.example.coursework.webserver.handlers;

import com.example.coursework.hibernateControllers.CustomHibernate;
import com.example.coursework.model.Publication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class AvailableBooksHandler implements HttpHandler {
    private final CustomHibernate customHibernate;
    private final Gson gson;

    public AvailableBooksHandler(CustomHibernate customHibernate) {
        this.customHibernate = customHibernate;
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String response = "";
        int statusCode = 200;

        // Only allow GET requests for available books
        if (!"GET".equals(method)) {
            statusCode = 405;
            response = "{\"error\": \"Method not allowed\"}";
        } else {
            try {
                // Get all available publications
                List<Publication> availablePublications = customHibernate.getAvailablePublications();
                response = gson.toJson(availablePublications);
            } catch (Exception e) {
                e.printStackTrace();
                statusCode = 500;
                response = "{\"error\": \"" + e.getMessage() + "\"}";
            }
        }

        // Set response headers
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.getBytes(StandardCharsets.UTF_8).length);
        
        // Write response
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }
}