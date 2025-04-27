package com.example.coursework.webserver.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Handler for root endpoint
 * Provides basic information about available API endpoints
 */
public class RootHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "Book Management API\n\n" +
                "Available endpoints:\n" +
                "- GET /api/books - List all books\n" +
                "- GET /api/books/available - List available books\n" +
                "- GET /api/comments - List comments\n" +
                "- GET /api/users - List users\n";
        
        exchange.getResponseHeaders().set("Content-Type", "text/plain");
        exchange.sendResponseHeaders(200, response.getBytes().length);
        
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}