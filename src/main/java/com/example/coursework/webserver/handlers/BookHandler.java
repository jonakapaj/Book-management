package com.example.coursework.webserver.handlers;

import com.example.coursework.hibernateControllers.CustomHibernate;
import com.example.coursework.model.Book;
import com.example.coursework.model.Publication;
import com.example.coursework.model.enums.Format;
import com.example.coursework.model.enums.Genre;
import com.example.coursework.model.enums.Language;
import com.example.coursework.webserver.GsonUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class BookHandler implements HttpHandler {
    private final CustomHibernate customHibernate;
    private final Gson gson;

    public BookHandler(CustomHibernate customHibernate) {
        this.customHibernate = customHibernate;
        this.gson = GsonUtils.createGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String response = "";
        int statusCode = 200;

        try {
            switch (method) {
                case "GET":
                    // Get all books or get book by ID
                    String query = exchange.getRequestURI().getQuery();
                    if (query != null && query.startsWith("id=")) {
                        String idStr = query.substring(3);
                        int id = Integer.parseInt(idStr);
                        Book book = customHibernate.getById(Book.class, id);
                        if (book != null) {
                            response = gson.toJson(book);
                        } else {
                            statusCode = 404;
                            response = "{\"error\": \"Book not found\"}";
                        }
                    } else {
                        // Get all publications and filter to only return books
                        List<Publication> allPublications = customHibernate.getAllRecords(Publication.class);
                        List<Book> books = allPublications.stream()
                                .filter(p -> p instanceof Book)
                                .map(p -> (Book) p)
                                .collect(Collectors.toList());
                        response = gson.toJson(books);
                    }
                    break;

                case "POST":
                    // Create new book
                    String requestBody = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                            .lines()
                            .collect(Collectors.joining("\n"));
                    
                    JsonObject jsonObject = gson.fromJson(requestBody, JsonObject.class);
                    
                    Book newBook = new Book(
                            jsonObject.get("title").getAsString(),
                            jsonObject.get("description").getAsString(),
                            jsonObject.get("author").getAsString(),
                            Format.valueOf(jsonObject.get("format").getAsString()),
                            Genre.valueOf(jsonObject.get("genre").getAsString()),
                            Language.valueOf(jsonObject.get("language").getAsString()),
                            jsonObject.get("isbn").getAsString(),
                            jsonObject.get("publisher").getAsString()
                    );
                    
                    customHibernate.create(newBook);
                    statusCode = 201;
                    response = gson.toJson(newBook);
                    break;

                case "PUT":
                    // Update existing book
                    query = exchange.getRequestURI().getQuery();
                    if (query != null && query.startsWith("id=")) {
                        String idStr = query.substring(3);
                        int id = Integer.parseInt(idStr);
                        Book existingBook = customHibernate.getById(Book.class, id);
                        
                        if (existingBook != null) {
                            requestBody = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                                    .lines()
                                    .collect(Collectors.joining("\n"));
                            
                            jsonObject = gson.fromJson(requestBody, JsonObject.class);
                            
                            if (jsonObject.has("title")) existingBook.setTitle(jsonObject.get("title").getAsString());
                            if (jsonObject.has("description")) existingBook.setDescription(jsonObject.get("description").getAsString());
                            if (jsonObject.has("author")) existingBook.setAuthor(jsonObject.get("author").getAsString());
                            if (jsonObject.has("format")) existingBook.setFormat(Format.valueOf(jsonObject.get("format").getAsString()));
                            if (jsonObject.has("genre")) existingBook.setGenre(Genre.valueOf(jsonObject.get("genre").getAsString()));
                            if (jsonObject.has("language")) existingBook.setLanguage(Language.valueOf(jsonObject.get("language").getAsString()));
                            if (jsonObject.has("isbn")) existingBook.setIsbn(jsonObject.get("isbn").getAsString());
                            if (jsonObject.has("publisher")) existingBook.setPublisher(jsonObject.get("publisher").getAsString());
                            
                            customHibernate.update(existingBook);
                            response = gson.toJson(existingBook);
                        } else {
                            statusCode = 404;
                            response = "{\"error\": \"Book not found\"}";
                        }
                    } else {
                        statusCode = 400;
                        response = "{\"error\": \"Missing id parameter\"}";
                    }
                    break;

                case "DELETE":
                    // Delete book
                    query = exchange.getRequestURI().getQuery();
                    if (query != null && query.startsWith("id=")) {
                        String idStr = query.substring(3);
                        int id = Integer.parseInt(idStr);
                        Book bookToDelete = customHibernate.getById(Book.class, id);
                        
                        if (bookToDelete != null) {
                            customHibernate.delete(Book.class, id);
                            response = "{\"success\": true, \"message\": \"Book deleted successfully\"}";
                        } else {
                            statusCode = 404;
                            response = "{\"error\": \"Book not found\"}";
                        }
                    } else {
                        statusCode = 400;
                        response = "{\"error\": \"Missing id parameter\"}";
                    }
                    break;

                default:
                    statusCode = 405;
                    response = "{\"error\": \"Method not allowed\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            statusCode = 500;
            response = "{\"error\": \"" + e.getMessage() + "\"}";
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