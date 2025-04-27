package com.example.coursework.webserver.handlers;

import com.example.coursework.hibernateControllers.CustomHibernate;
import com.example.coursework.model.Admin;
import com.example.coursework.model.Client;
import com.example.coursework.model.User;
import com.example.coursework.webserver.GsonUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

public class UserHandler implements HttpHandler {
    private final CustomHibernate customHibernate;
    private final Gson gson;

    public UserHandler(CustomHibernate customHibernate) {
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
                    // Get all users or get user by ID
                    String query = exchange.getRequestURI().getQuery();
                    if (query != null && query.startsWith("id=")) {
                        String idStr = query.substring(3);
                        int id = Integer.parseInt(idStr);
                        User user = customHibernate.getById(User.class, id);
                        if (user != null) {
                            response = gson.toJson(user);
                        } else {
                            statusCode = 404;
                            response = "{\"error\": \"User not found\"}";
                        }
                    } else if (query != null && query.startsWith("login=")) {
                        // Find user by login
                        String login = query.substring(6);
                        User user = customHibernate.getUserByCredentials(login, ""); // We only need login for search
                        if (user != null) {
                            response = gson.toJson(user);
                        } else {
                            statusCode = 404;
                            response = "{\"error\": \"User not found\"}";
                        }
                    } else {
                        // Get all users
                        List<User> users = customHibernate.getAllRecords(User.class);
                        response = gson.toJson(users);
                    }
                    break;

                case "POST":
                    // Create new user
                    String requestBody = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                            .lines()
                            .collect(Collectors.joining("\n"));
                    
                    JsonObject jsonObject = JsonParser.parseString(requestBody).getAsJsonObject();
                    String userType = jsonObject.get("userType").getAsString();
                    
                    if ("client".equalsIgnoreCase(userType)) {
                        Client client = new Client(
                                jsonObject.get("name").getAsString(),
                                jsonObject.get("surname").getAsString(),
                                jsonObject.get("login").getAsString(),
                                jsonObject.get("password").getAsString(),
                                jsonObject.get("email").getAsString(),
                                LocalDate.parse(jsonObject.get("birthDate").getAsString()),
                                jsonObject.get("address").getAsString()
                        );
                        customHibernate.create(client);
                        response = gson.toJson(client);
                    } else if ("admin".equalsIgnoreCase(userType)) {
                        Admin admin = new Admin(
                                jsonObject.get("name").getAsString(),
                                jsonObject.get("surname").getAsString(),
                                jsonObject.get("login").getAsString(),
                                jsonObject.get("password").getAsString(),
                                jsonObject.get("email").getAsString(),
                                LocalDate.parse(jsonObject.get("employmentDate").getAsString()),
                                jsonObject.get("phoneNum").getAsString()
                        );
                        customHibernate.create(admin);
                        response = gson.toJson(admin);
                    } else {
                        statusCode = 400;
                        response = "{\"error\": \"Invalid user type. Must be 'client' or 'admin'\"}";
                    }
                    
                    if (statusCode == 200) {
                        statusCode = 201; // Created
                    }
                    break;

                case "PUT":
                    // Update existing user
                    query = exchange.getRequestURI().getQuery();
                    if (query != null && query.startsWith("id=")) {
                        String idStr = query.substring(3);
                        int id = Integer.parseInt(idStr);
                        User existingUser = customHibernate.getById(User.class, id);
                        
                        if (existingUser != null) {
                            requestBody = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                                    .lines()
                                    .collect(Collectors.joining("\n"));
                            
                            jsonObject = JsonParser.parseString(requestBody).getAsJsonObject();
                            
                            // Update common properties
                            if (jsonObject.has("name")) existingUser.setName(jsonObject.get("name").getAsString());
                            if (jsonObject.has("surname")) existingUser.setSurname(jsonObject.get("surname").getAsString());
                            if (jsonObject.has("email")) existingUser.setEmail(jsonObject.get("email").getAsString());
                            if (jsonObject.has("login")) existingUser.setLogin(jsonObject.get("login").getAsString());
                            if (jsonObject.has("password")) existingUser.setPassword(jsonObject.get("password").getAsString());
                            
                            // Update specific properties based on user type
                            if (existingUser instanceof Client client) {
                                if (jsonObject.has("birthDate")) client.setBirthDate(LocalDate.parse(jsonObject.get("birthDate").getAsString()));
                                if (jsonObject.has("address")) client.setAddress(jsonObject.get("address").getAsString());
                            } else if (existingUser instanceof Admin admin) {
                                if (jsonObject.has("phoneNum")) admin.setPhoneNum(jsonObject.get("phoneNum").getAsString());
                                if (jsonObject.has("employmentDate")) admin.setEmploymentDate(LocalDate.parse(jsonObject.get("employmentDate").getAsString()));
                            }
                            
                            customHibernate.update(existingUser);
                            response = gson.toJson(existingUser);
                        } else {
                            statusCode = 404;
                            response = "{\"error\": \"User not found\"}";
                        }
                    } else {
                        statusCode = 400;
                        response = "{\"error\": \"Missing id parameter\"}";
                    }
                    break;

                case "DELETE":
                    // Delete user
                    query = exchange.getRequestURI().getQuery();
                    if (query != null && query.startsWith("id=")) {
                        String idStr = query.substring(3);
                        int id = Integer.parseInt(idStr);
                        User userToDelete = customHibernate.getById(User.class, id);
                        
                        if (userToDelete != null) {
                            customHibernate.delete(User.class, id);
                            response = "{\"success\": true, \"message\": \"User deleted successfully\"}";
                        } else {
                            statusCode = 404;
                            response = "{\"error\": \"User not found\"}";
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